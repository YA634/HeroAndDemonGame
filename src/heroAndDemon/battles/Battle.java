package heroAndDemon.battles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
		InputUtil input = new InputUtil();
		Creature hero = Creature.createHero(name);
		hero.showParameter();
		System.out.println("1:easy　2:hard");
		int mode = input.readMenuChoice("モードを選択してください", 2);
		System.out.println("== 取得可能なスキル ==");

		if (mode == 0) {
			//全ての選択肢から取得
			int indexNum = 0;
			for (Skill skill : Skill.values()) {
				if (skill == Skill.NGR || skill == Skill.MMR) {
					continue;
				}
				indexNum++;
				System.out.println((indexNum) + ":" + skill.getName());
			}
			Skill[] skillSet = new Skill[3];
			for (int i = 0; i < hero.getSkillSet().length; i++) {
				skillSet[i] = input.readSkill((i + 1) + "つ目のスキルを選択してください");
				System.out.println(skillSet[i]);
			}
			hero.setSkillSet(skillSet);
		} else if (mode == 1) {
			//		3つの選択肢から取得
			Skill[] skillSet = new Skill[3];
			List<Skill> skillList = new ArrayList<>();
			for (Skill skill : Skill.values()) {
				if (skill == Skill.NGR || skill == Skill.MMR) {
					continue;
				}
				skillList.add(skill);
			}
			List<Skill> selected = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				List<Skill> skillList2 = new ArrayList<>(skillList);
				skillList2.removeAll(selected);
				Collections.shuffle(skillList2);
				for (int k = 0; k < 3; k++) {
					System.out.println((k + 1) + ":" + skillList2.get(k).getName());
				}
				Skill sk = input.readSkill((i + 1) + "つ目のスキルを選択してください", skillList2);
				selected.add(sk);
				skillSet[i] = sk;
			}

			hero.setSkillSet(skillSet);
		}
		return hero;
	}

	private int battle(Creature hero, Creature demon) {
		InputUtil input = new InputUtil();
		Random r = new Random();
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
				if (turn == 1) {
					//demon.useSkill(firstSkill);
				}
				Creature p1 = spdJudge(hero, demon);
				Creature p2;
				if (p1 == hero) {
					p2 = demon;
				} else {
					p2 = hero;
				}
				if (p1.getEfDulation().containsKey(Attribute.SLEEP)) {
					System.out.println(p1.getName() + "はまだ眠っている！");
				} else {
					if (p1.getEfDulation().containsKey(Attribute.CONFUSION)) {
						int con = r.nextInt(2);
						if (con == 0) {
							int cDmg = p1.getBtlParam().get(Param.HP) * 3 / 100;
							System.out.println(p1.getName() + "は混乱して自身に攻撃を放った！");
							System.out.println(cDmg + "ダメージ！!");
							p1.setBtlParam(Param.HP, p1.getBtlParam().get(Param.HP) - cDmg);
						} else {
							useSkill(MenuSelect, p1, p2);
						}
					} else {
						useSkill(MenuSelect, p1, p2);
					}
					sleep(1);
					isLiveJudge(demon);
					isLiveJudge(hero);
					if (demon.isLive() && hero.isLive()) {
						if (p2.getEfDulation().containsKey(Attribute.SLEEP)) {
							System.out.println(p2.getName() + "はまだ眠っている！");
						} else {
							if (p2.getEfDulation().containsKey(Attribute.CONFUSION)) {
								int con = r.nextInt(2);
								if (con == 0) {
									int cDmg = p2.getBtlParam().get(Param.HP) * 3 / 100;
									System.out.println(p2.getName() + "は混乱して自身に攻撃を放った！");
									System.out.println(cDmg + "ダメージ！!");
									p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - cDmg);
								} else {
									useSkill(MenuSelect, p2, p1);
								}
							} else {
								useSkill(MenuSelect, p2, p1);
							}
						}
					}
					sleep(1);
					isLiveJudge(demon);
					isLiveJudge(hero);
				}
				sleep(1);
				//			} else {
				//				Creature p2 = hero;
				//				if (p1.getEfDulation().containsKey(Attribute.SLEEP)) {
				//					System.out.println(p1.getName() + "はまだ眠っている！");
				//				} else {
				//					if (p1.getEfDulation().containsKey(Attribute.CONFUSION)) {
				//						int con = r.nextInt(2);
				//						if (con == 0) {
				//							int cDmg = p1.getBtlParam().get(Param.HP) * 3 / 100;
				//							System.out.println(p1.getName() + "は混乱して自身に攻撃を放った！");
				//							System.out.println(cDmg + "ダメージ！!");
				//							p1.setBtlParam(Param.HP, p1.getBtlParam().get(Param.HP) - cDmg);
				//						} else {
				//							useSkill(MenuSelect, demon, hero);
				//						}
				//					} else {
				//						useSkill(MenuSelect, demon, hero);
				//					}
				//					isLiveJudge(hero);
				//					isLiveJudge(demon);
				//					sleep(1);
				//					if (hero.isLive() && demon.isLive()) {
				//						useSkill(MenuSelect, hero, demon);
				//					}
				//					isLiveJudge(demon);
				//				}
				sleep(1);
				endFaze(hero);
				endFaze(demon);
				isLiveJudge(hero);
				isLiveJudge(demon);
				hero.showParameter();
				demon.showParameter();
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
		Random r = new Random();
		if (p1.getName().equals("魔王")) {
			skillNum = r.nextInt(6) + 1;
		}
		if (skillNum == 1) {
			System.out.println(p1.getName() + "は" + p2.getName() + "に殴りかかった！");
			int dmg = dmgJudge(p1, p2, 1, Skill.NGR);
			System.out.println(dmg + "ダメージ！！");
			p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		} else if (skillNum == 2) {
			System.out.println(p1.getName() + "は自身の守りを固めた！");
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 2);
			p1.setEfDulation(Skill.MMR, Skill.MMR.getDulation());
		} else {
			Skill skill = p1.getSkillSet()[skillNum - 3];
			if (p1.getBtlParam().get(Param.MP) < skill.getUseMP()) {
				System.out.println("MPが足りない！");
			} else {
				p1.setBtlParam(Param.MP, p1.getBtlParam().get(Param.MP) - skill.getUseMP());
				System.out.println(p1.getName() + "は" + skill.getName() + "を使った");
				//色々処理
				if (skill.getType() == SkillType.ATTACK) {
					int dmg = dmgJudge(p1, p2, skill.getPower(), skill);
					System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
					p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
				} else if (skill.getType() == SkillType.HEAL) {
					healAction(p1, skill);
				} else if (skill.getType() == SkillType.BUFF) {
					bfAction(p1, skill);
				} else if (skill.getType() == SkillType.DEBUFF) {
					dbfAction(p1, p2, skill);
				} else if (skill.getType() == SkillType.CONERROR) {
					conErrorAction(p1, p2, skill);
				} else if (skill.getType() == SkillType.SPECIAL) {
					specialAction(p1, p2, skill);
				}
			}
		}
	}

	private void useSkill(int skillNum, Creature p1, Creature[] creatures) {
		//対複数　今後実装予定
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

	private boolean avoidJudge(Creature p1, Creature p2, int option) {
		Random r = new Random();
		int p1Spd = p1.getBtlParam().get(Param.SPD) + r.nextInt(50);
		int p2Spd = p2.getBtlParam().get(Param.SPD) + r.nextInt(50);
		if (p1Spd > p2Spd + option) {
			//回避成功
			return true;
		} else {
			//回避失敗
			return false;
		}
	}

	private int dmgJudge(Creature p1, Creature p2, int option, Skill sk) {
		//クリティカル、オーバークリティカルを追加
		Random r = new Random();
		if (avoidJudge(p1, p2, 50)) {
			System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			return 0;
		} else {
			int cri = r.nextInt(10);
			int oCri = r.nextInt(10);
			int dmg;
			if (cri == 0) {
				if (oCri == 0) {
					System.out.println("オーバークリティカル！！！");
					dmg = (p1.getBtlParam().get(Param.ATK) + option + r.nextInt(100)) * 8
							- p2.getBtlParam().get(Param.DEF);
				} else {
					System.out.println("クリティカル！！");
					dmg = (p1.getBtlParam().get(Param.ATK) + option + r.nextInt(100)) * 4
							- p2.getBtlParam().get(Param.DEF);
				}
			} else {
				dmg = p1.getBtlParam().get(Param.ATK) + option + r.nextInt(100) - p2.getBtlParam().get(Param.DEF);
			}
			if (dmg < 0) {
				return 0;
			}
			return dmg;
		}
	}

	private void isLiveJudge(Creature p) {
		int hp = p.getBtlParam().get(Param.HP);
		if (hp <= 0) {
			p.setBtlParam(Param.HP, 0);
			p.setLive(false);
		}
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
			p.setBtlParam(Param.ATK, btP.get(Param.ATK) * sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		} else if (sk.getAtrbt() == Attribute.DEFBF) {
			p.setBtlParam(Param.DEF, btP.get(Param.DEF) * sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		}
	}

	private void dbfAction(Creature p1, Creature p2, Skill sk) {
		Map<Param, Integer> dfP = p2.getDftParam();
		Map<Param, Integer> btP = p2.getBtlParam();
		if (avoidJudge(p1, p2, 50)) {
			System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
		} else {
			if (sk.getAtrbt() == Attribute.ATKDBF) {
				p2.setBtlParam(Param.ATK, btP.get(Param.ATK) / sk.getPower());
				p2.setEfDulation(sk, sk.getDulation());
			} else if (sk.getAtrbt() == Attribute.DEFDBF) {
				p2.setBtlParam(Param.DEF, btP.get(Param.DEF) / sk.getPower());
				p2.setEfDulation(sk, sk.getDulation());
			}
		}
	}

	private void conErrorAction(Creature p1, Creature p2, Skill sk) {
		if (sk.getAtrbt() == Attribute.SLEEP) {
			if (avoidJudge(p1, p2, 20)) {
				System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			} else {
				System.out.println(p2.getName() + "は眠ってしまった！");
				p2.setEfDulation(sk, sk.getDulation());
			}
		} else if (sk.getAtrbt() == Attribute.POISON) {
			if (avoidJudge(p1, p2, 40)) {
				System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			} else {
				System.out.println(p2.getName() + "は毒状態になってしまった！");
				p2.setEfDulation(sk, sk.getDulation());
			}
		} else if (sk.getAtrbt() == Attribute.CONFUSION) {
			if (avoidJudge(p1, p2, 40)) {
				System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			} else {
				System.out.println(p2.getName() + "は混乱状態になってしまった！");
				p2.setEfDulation(sk, sk.getDulation());
			}
		}
	}

	private void conErrorAction(Creature p1, Creature[] p2, Skill sk) {
		//対複数　今後実装予定
		if (sk.getAtrbt() == Attribute.SLEEP) {
			System.out.println("");
		} else if (sk.getAtrbt() == Attribute.POISON) {
			System.out.println();
		} else if (sk.getAtrbt() == Attribute.CONFUSION) {
			System.out.println();
		}
	}

	private void specialAction(Creature p1, Creature p2, Skill skill) {
		if (skill == Skill.PPT) {
			System.out.println("ランダムな効果が発生！");
		}
	}

	private int standbyFaze(Creature p1) {
		//いらないかも　今後使うかもだから残す
		return 0;
	}

	private boolean removeConErrorJudge(Creature p1, Attribute atr, int du) {
		//trueなら解除、falseなら解除失敗
		Random r = new Random();
		if (du <= 0) {
			int tf = r.nextInt(2);
			if (tf == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private void endFaze(Creature p1) {
		Map<Skill, Integer> pEfD = p1.getEfDulation();
		Set<Skill> pSkills = pEfD.keySet();
		Iterator<Skill> it = pEfD.keySet().iterator();
		while (it.hasNext()) {
			Skill sk = it.next();
			//		for (Skill sk : pSkills) {
			Attribute atrbt = sk.getAtrbt();
			int du = pEfD.get(sk);
			if (atrbt == Attribute.POISON) {
				int pDmg = p1.getBtlParam().get(Param.HP) * 3 / 100;
				System.out.println(p1.getName() + "は毒によって" + pDmg + "ダメージを受けた！");
				p1.setBtlParam(Param.HP, p1.getBtlParam().get(Param.HP) - pDmg);
				if (du == 0) {
					boolean remC = removeConErrorJudge(p1, atrbt, du);
					if (remC) {
						//解除成功の場合
						//						pEfD.remove(sk);
						it.remove();
						System.out.println(p1.getName() + "は毒の治癒に成功した！");
					} else {
						System.out.println(p1.getName() + "はまだ毒状態だ！");
					}
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "はまだ毒状態だ！");
				}
			} else if (atrbt == Attribute.SLEEP) {
				if (du == 0) {
					boolean remC = removeConErrorJudge(p1, atrbt, du);
					if (remC) {
						//解除成功の場合
						//						pEfD.remove(sk);
						it.remove();
						System.out.println(p1.getName() + "は眠りから醒めた！");
					} else {
						System.out.println(p1.getName() + "はまだ眠っている！");
					}
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "はまだ眠っている！");
				}
			} else if (atrbt == Attribute.CONFUSION) {
				if (du == 0) {
					boolean remC = removeConErrorJudge(p1, atrbt, du);
					if (remC) {
						//解除成功の場合
						//						pEfD.remove(sk);
						it.remove();
						System.out.println(p1.getName() + "は混乱から覚めた！");
					} else {
						System.out.println(p1.getName() + "はまだ混乱している！");
					}
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "はまだ混乱している！");
				}
			} else if (atrbt == Attribute.ATKBF || atrbt == Attribute.DEFBF || atrbt == Attribute.ATKDBF
					|| atrbt == Attribute.DEFDBF) {
				if (du == 0) {
					it.remove();
					System.out.println(p1.getName() + "は" + sk.getName() + "の効果が消えた！");
				} else {
					pEfD.put(sk, du - 1);
				}
			}
		}
	}

	private void sleep(int second) {
		//second秒待機するやつ
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
