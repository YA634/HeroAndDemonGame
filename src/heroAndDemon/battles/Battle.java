package heroAndDemon.battles;

import java.util.Map;
import java.util.Random;

import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Creature;
import heroAndDemon.models.Creature.Param;
import heroAndDemon.models.Skill;
import heroAndDemon.models.Skill.Attribute;
import heroAndDemon.models.Skill.SkillType;

public class Battle {
	public int start(Creature demon, String name) {
		Creature hero = skillselect(demon, name);
		int btlResult = battle(hero, demon);
		return btlResult;
	}

	private Creature skillselect(Creature demon, String name) {
		Creature hero = Creature.createHero(name);
		hero.showParameter();
		System.out.println();
		System.out.println("== 取得可能なスキル ==");
		int indexNum = 0;
		for (Skill skill : Skill.values()) {
			System.out.println((indexNum + 1) + ":" + skill.getName());
			indexNum++;
		}
		InputUtil input = new InputUtil();
		Skill[] skillSet = new Skill[3];
		for (int i = 0; i < hero.getSkillSet().length; i++) {
			skillSet[i] = input.readSkill((i + 1) + "つ目のスキルを選択してください");
			System.out.println(skillSet[i]);
		}
		hero.setSkillSet(skillSet);
		return hero;
	}

	private int battle(Creature hero, Creature demon) {
		InputUtil input = new InputUtil();
		int turn = 0;
		while (hero.isLive() && demon.isLive()) {
			demon.showAA("demon");
			sleep(1);
			int MenuSelect = 2;
			System.out.println();
			System.out.println("== メニュー ==");
			while (MenuSelect == 2) {
				showMenu(0, hero);
				MenuSelect = input.readMenuChoice("数字を入力→", 2);
				if (MenuSelect == 1) {//ポーズ選択の場合
					showMenu(1, hero);
					MenuSelect = input.readMenuChoice("数字を入力→", 3);
					if (MenuSelect == 0) {//サレンダー
						hero.setLive(false);
					} else if (MenuSelect == 1) {//ゲームをやめる
						return 4;
					}
				} else if (MenuSelect == 0) {//スキル選択の場合
					showMenu(2, hero);
					MenuSelect = input.readMenuChoice("数字を入力→", 3 + hero.getSkillSet().length);
					if (MenuSelect == 0) {//戻る選択の場合
						MenuSelect = 2;
					}
				}
			}
			turn++;
			if (hero.isLive()) {
				//firstSkill
				if (turn == 0) {
					//demon.useSkill(firstSkill);
				}
				Creature p1 = spdJudge(hero, demon);
				if (p1 == hero) {
					useSkill(MenuSelect, hero, demon);
					sleep(1);
					if (demon.isLive()) {
						useSkill(MenuSelect, demon, hero);
					}
					sleep(1);
				} else {
					useSkill(MenuSelect, demon, hero);
					sleep(1);
					if (hero.isLive()) {
						useSkill(MenuSelect, hero, demon);
					}
					sleep(1);
				}
				hero.showParameter();
				sleep(1);
			}
		}
		if (!demon.isLive()) { //勇者勝ち
			return 1;
		} else if (!hero.isLive()) { //魔王勝ち
			return 2;
		} else { //なし
			return 3;
		}
	}

	private void useSkill(int skillNum, Creature p1, Creature p2) {
		if (skillNum == 1) {
			System.out.println(p1.getName() + "は" + p2.getName() + "に殴りかかった！");
			int dmg = dmgJudge(p1.getBtlParam().get(Param.ATK), p2.getBtlParam().get(Param.DEF), 1);
			System.out.println(dmg + "ダメージ！！");
			p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		} else if (skillNum == 2) {
			System.out.println(p1.getName() + "は自身の守りを固めた！");
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 2);
			p1.setEfDulation(Skill.MMR, Skill.MMR.getDulation());
		} else {
			Skill skill = p1.getSkillSet()[skillNum - 3];
			System.out.println(p1.getName() + "は" + skill.getName() + "を使った");
			//色々処理
			if (skill.getType() == SkillType.ATTACK) {
				int dmg = dmgJudge(p1.getBtlParam().get(Param.ATK), p2.getBtlParam().get(Param.DEF), skill.getPower());
				p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
			} else if (skill.getType() == SkillType.HEAL) {
				healAction(p1, skill);
			} else if (skill.getType() == SkillType.BUFF) {
				bfAction(p1, skill);
			} else if (skill.getType() == SkillType.DEBUFF) {
				dbfAction(p2, skill);
			} else if (skill.getType() == SkillType.CONERROR) {
				conErrorAction(p1, p2, skill);
			} else if (skill.getType() == SkillType.SPECIAL) {
				specialAction(p1, p2, skill);
			}
		}
	}

	private void useSkill(int skillNum, Creature p1, Creature[] creatures) {

	}

	private void showMenu(int option, Creature hero) {
		Skill[] skillSet = new Skill[3];
		if (option == 0) {
			System.out.println("1:スキル");
			System.out.println("2:ポーズ");
		} else if (option == 1) {
			System.out.println("1:サレンダー");
			System.out.println("2:バトルを終了");
			System.out.println("3:戻る");
		} else if (option == 2) {
			System.out.println(" 1:戻る");
			System.out.println("2:殴る");
			System.out.println("3:身を守る");
			for (int i = 0; i < hero.getSkillSet().length; i++) {
				System.out.println((4 + i) + ":" + hero.getSkillSet()[i].getName());
			}
		} else if (option == 1 || option == 3 || option == 4) {

		}
	}

	private Creature spdJudge(Creature hero, Creature demon) {
		Random r = new Random();
		int hSPD = hero.getBtlParam().get(Param.SPD) + r.nextInt(30);
		int dSPD = demon.getBtlParam().get(Param.SPD) + r.nextInt(30);
		if (hSPD > dSPD) {
			return hero;
		} else {
			return demon;
		}
	}

	private boolean avoidJudge(Creature p1, Creature p2) {
		Random r = new Random();
		int p1Spd = p1.getBtlParam().get(Param.SPD) + r.nextInt(30);
		int p2Spd = p2.getBtlParam().get(Param.SPD) + r.nextInt(30);
		if (p1Spd > p2Spd + 30) {
			return true;
		} else {
			return false;
		}
	}

	private int dmgJudge(int atk, int def, int option) {
		//クリティカル、オーバークリティカルを追加
		Random r = new Random();
		if (option == 1) {
			int dmg = atk + r.nextInt(100) - def;
			return dmg;
		}
		return 0;
	}

	private void healAction(Creature p, Skill sk) {
		int dfHP = p.getDftParam().get(Param.HP);
		int btHP = p.getBtlParam().get(Param.HP);
		if (sk.getPower() > 5) {
			p.setBtlParam(Param.HP, dfHP);
			System.out.println(p.getName() + "はHPを全回復した！");
		} else {
			if (dfHP > btHP + dfHP * sk.getPower() / 10) {
				p.setBtlParam(Param.HP, btHP + dfHP * sk.getPower() / 10);
			} else {
				p.setBtlParam(Param.HP, dfHP);
			}
		}
	}

	private void bfAction(Creature p, Skill sk) {
		Map<Param, Integer> dfP = p.getDftParam();
		Map<Param, Integer> btP = p.getBtlParam();
		if (sk.getAtrbt() == Attribute.ATKBF) {
			p.setBtlParam(Param.DEF, btP.get(Param.DEF) * sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		} else if (sk.getAtrbt() == Attribute.DEFBF) {
			p.setBtlParam(Param.DEF, btP.get(Param.DEF) * sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		}
	}

	private void dbfAction(Creature p, Skill sk) {
		Map<Param, Integer> dfP = p.getDftParam();
		Map<Param, Integer> btP = p.getBtlParam();
		if (sk.getAtrbt() == Attribute.ATKDBF) {
			p.setBtlParam(Param.DEF, btP.get(Param.DEF) / sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		} else if (sk.getAtrbt() == Attribute.DEFDBF) {
			p.setBtlParam(Param.DEF, btP.get(Param.DEF) / sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		}
	}

	private void conErrorAction(Creature p1, Creature p2, Skill sk) {
		if (sk.getAtrbt() == Attribute.SLEEP) {
			System.out.println("");
		} else if (sk.getAtrbt() == Attribute.POISON) {
			System.out.println();
		} else if (sk.getAtrbt() == Attribute.CONFUSION) {
			System.out.println();
		}
	}

	private void conErrorAction(Creature p1, Creature[] p2, Skill sk) {
		if (sk.getAtrbt() == Attribute.SLEEP) {
			System.out.println("");
		} else if (sk.getAtrbt() == Attribute.POISON) {
			System.out.println();
		} else if (sk.getAtrbt() == Attribute.CONFUSION) {
			System.out.println();
		}
	}

	private void specialAction(Creature p1, Creature p2, Skill skill) {
		if (skill.getName() == "パルプンテ") {
			System.out.println("ランダムな効果が発生！");
		}
	}

	private void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
