package heroAndDemon;

import heroAndDemon.gameManager.GameManager;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		GameManager gm = new GameManager();
		try {
			gm.start();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("何かしらの想定していないエラーが発生しました");
			System.out.println("終了します");
		}
	}

}
