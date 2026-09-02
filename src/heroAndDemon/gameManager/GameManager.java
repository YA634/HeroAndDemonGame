package heroAndDemon.gameManager;

import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Creature;
import heroAndDemon.models.Creature.Category;
import heroAndDemon.models.Creature.SkillSet;

public class GameManager {
	public void start() {
		Creature demon = createDemon();
		Creature hero = createHero();
		demon.setParameter(Category.DEMON);
		hero.setParameter(Category.HERO);
		System.out.println();
		System.out.println("   勇者と魔王ゲーム   ");
		System.out.println();
		hero.showParameter();
		System.out.println();
		System.out.println("== 取得可能なスキル ==");
		int i = 0;
		for (SkillSet skill : SkillSet.values()) {
			System.out.println(i + ":" + skill.getname());
			i++;
		}
		InputUtil input = new InputUtil();
		SkillSet skill1 = input.readSkill("一つ目のスキルを選択してください");
		System.out.println(skill1);
		SkillSet skill2 = input.readSkill("二つ目のスキルを選択してください");
		System.out.println(skill2);
		SkillSet skill3 = input.readSkill("三つ目のスキルを選択してください");
		System.out.println(skill3);
		SkillSet[] skillSet = new SkillSet[3];
		skillSet[0] = skill1;
		skillSet[1] = skill2;
		skillSet[2] = skill3;
	}

	private void showPause() {
		System.out.println();
		System.out.println("== Pause ==");
	}

	private Creature createDemon() {
		Creature demon = new Creature(Creature.Category.DEMON);
		return demon;
	}

	private Creature createHero() {
		Creature hero = new Creature(Creature.Category.HERO);
		return hero;
	}
}
