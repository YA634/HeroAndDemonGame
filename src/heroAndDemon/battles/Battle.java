package heroAndDemon.battles;

import java.util.Random;

import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Creature;
import heroAndDemon.models.Creature.Param;
import heroAndDemon.models.Creature.SkillSet;

public class Battle {
	public boolean start(Creature demon, String name) {
		Creature hero = skillselect(demon, name);
		int btResult = battle(hero, demon);
		return hero.isLive();
	}

	private Creature skillselect(Creature demon, String name) {
		Creature hero = Creature.createHero(name);
		hero.showParameter();
		System.out.println();
		System.out.println("== 取得可能なスキル ==");
		int indexNum = 0;
		for (SkillSet skill : SkillSet.values()) {
			System.out.println((indexNum + 1) + ":" + skill.getname());
			indexNum++;
		}
		InputUtil input = new InputUtil();
		SkillSet[] skillSet = new SkillSet[3];
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
		return 1;
	}

	private void useSkill(int skillNum, Creature p1, Creature p2) {
		if (skillNum == 1) {
			System.out.println(p1.getName() + "は" + p2.getName() + "に殴りかかった！");

		}
	}

	private void showMenu(int option, Creature hero) {
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
				System.out.println((4 + i) + ":" + hero.getSkillSet()[i]);
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

	private int dmgJudge(int atk, int def, int option) {
		Random r = new Random();
		if (option == 1) {
			int dmg = atk + r.nextInt(100) - def;
			return dmg;
		}
		return 0;
	}

	private void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
