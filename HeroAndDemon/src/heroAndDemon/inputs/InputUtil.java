package heroAndDemon.inputs;

import java.util.InputMismatchException;
import java.util.Scanner;

import heroAndDemon.models.Creature.SkillSet;

public class InputUtil {
	private Scanner scan = new Scanner(System.in);

	public void close() {
		scan.close();
	}

	public SkillSet readSkill(String prompt) {
		while (true) {
			System.out.println(prompt);
			int index = scan.nextInt() - 1;
			try {
				if (index >= 0 && index < SkillSet.values().length) {
					return SkillSet.values()[index];
				}
				System.out.println("1~" + (SkillSet.values().length) + "までの数字を入力してください");
			} catch (InputMismatchException e) {
				// TODO: handle exception
				System.out.println("数字を入力してください");
				scan.nextLine(); // 無効な入力をバッファから読み捨てる
			}
		}

	}

	public int readMenuChoice(String prompt, int menuAmount) {
		System.out.println(prompt);
		while (true) {
			try {
				int choice = scan.nextInt() - 1;
				scan.nextLine();
				if (choice >= 0 && choice < menuAmount) {
					return choice;
				}
				System.out.println("1~" + menuAmount + "までの数字を入力してください");
			} catch (InputMismatchException e) {
				System.out.println("数字を入力してください");
				scan.nextLine(); // 無効な入力をバッファから読み捨てる
			}
		}
	}

	public int readPauseChoice(String prompt) {
		System.out.println(prompt);
		return scan.nextInt();
	}

	public String readString(String prompt) {
		System.out.println(prompt);
		return scan.nextLine();
	}
}
