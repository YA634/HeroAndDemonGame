package heroAndDemon.inputs;

import java.util.Scanner;

public class InputUtil {
	private Scanner scan = new Scanner(System.in);

	public void close() {
		scan.close();
	}

	public String readSkill(String prompt) {
		System.out.println(prompt);
		return scan.nextLine();
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
