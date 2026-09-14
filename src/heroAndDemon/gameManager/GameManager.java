package heroAndDemon.gameManager;

import heroAndDemon.battles.Battle;
import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Creature;

public class GameManager {
	public void start() {
		int th = 1;
		String name0 = "";
		while (th < 2) {
			Creature demon = Creature.createDemon();
			System.out.println();
			System.out.println("   勇者と魔王ゲーム   ");
			System.out.println();
			InputUtil input = new InputUtil();
			String name = "";
			if (th == 1) {
				int ans = 2;
				while (ans != 0 && ans != 1) {
					ans = input.readMenuChoice("ゲームを始める 1:yes 2:はい", 2);
				}
				name = input.readString("勇者の名前を入力してください(Enterでスキップ)");
				if (name == "") {
					name = "†混沌の魔術師(カオスルーラー)† ニャルラトホテプ";
				}
			} else if (th == 0) {
				name = name0;
			}
			boolean isLive = false;
			int limit = 3;
			int btlResult = 0;
			while (!isLive) {
				limit--;
				Battle btl = new Battle();
				btlResult = btl.start(demon, name);
				if (btlResult == 1) { //勇者勝ち
					break;
				} else if ((btlResult == 2 || btlResult == 4) && limit == 0) {
					break;
				}
			}
			showResult(btlResult, name);
			th = input.readMenuChoice("もう一度同じ設定で遊ぶ→ 1:はい 2:いいえ 3:このゲームを終了する", 3);
			if (th == 0) {
				name0 = name;
			}
		}
	}

	private void showResult(int btlResult, String name) {
		if (btlResult == 1) {
			System.out.println("テレッテッテレー");
			System.out.println("勇者" + name + "が魔王を打ち倒した！");
			System.out.println("世界に平和は戻り、祝福が勇者" + name + "を包んだ！");
		} else if (btlResult == 2 || btlResult == 4) {
			System.out.println("チーン");
			System.out.println("勇者" + name + "は命を落とした...");
			System.out.println("世界は魔王に支配され、深い暗黒に包まれた...");
		}
	}
}
