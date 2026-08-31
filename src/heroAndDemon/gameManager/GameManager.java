package heroAndDemon.gameManager;

import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Creature;
import heroAndDemon.models.Creature.Category;

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
		System.out.println(hero.getSkillSet());
		InputUtil input = new InputUtil();
		String skill1 = input.readSkill("一つ目のスキルを選択してください");
		String skill2 = input.readSkill("二つ目のスキルを選択してください");
		String skill3 = input.readSkill("三つ目のスキルを選択してください");
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
