package heroAndDemon.inputs;

import java.util.Scanner;

import heroAndDemon.models.Creature.SkillSet;

public class InputUtil {
	private Scanner scan = new Scanner(System.in);

	public void close() {
		scan.close();
	}

	public SkillSet readSkill(String prompt) {
		System.out.println(prompt);
		return SkillSet.values()[scan.nextInt()];
	}

	public int readMenuChoice(String prompt) {
		System.out.println(prompt);
		return scan.nextInt();
	}

	public int readPauseChoice(String prompt) {
		System.out.println(prompt);
		return scan.nextInt();
	}
}
