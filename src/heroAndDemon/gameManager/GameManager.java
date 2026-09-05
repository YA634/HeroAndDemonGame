package heroAndDemon.gameManager;

import heroAndDemon.battles.Battle;
import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Creature;

public class GameManager {
	public void start() {
		Creature demon = Creature.createDemon();
		System.out.println();
		System.out.println("   勇者と魔王ゲーム   ");
		System.out.println();
		InputUtil input = new InputUtil();
		int ans = 2;
		while (ans != 0 && ans != 1) {
			ans = input.readMenuChoice("ゲームを始める 1:yes 2:はい", 2);
		}
		String name = input.readString("勇者の名前を入力してください(Enterでスキップ)");
		if (name == "") {
			name = "†混沌の魔術師(カオスルーラー)† ニャルラトホテプ";
		}
		boolean isLive = false;
		int limit = 3;
		while (!isLive) {
			limit--;
			Battle btl = new Battle();
			isLive = btl.start(demon, name);
			if (limit == 0) {
				break;
			}
			if (demon.isLive() == false) {
				break;
			}
		}
		showResult();
	}

	private void showPause() {
		System.out.println();
		System.out.println("== Pause ==");
	}

	private void showResult() {
		System.out.println();
	}
}
