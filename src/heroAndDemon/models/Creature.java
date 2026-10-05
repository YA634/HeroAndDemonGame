package heroAndDemon.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Creature {
	public enum Category {
		HERO, DEMON, SERVANTH, SERVANTD
	}

	//simobeを追加
	public enum Jinei {
		HERO, DEMON
	}

	public enum ServantName {
		スライム("スライム"),
		;

		private final String name;

		private ServantName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	//勇者か魔王か
	private Category category;

	public enum Param {
		HP, MP, ATK, DEF, SPD, MAG, LUK
	}

	//初期HP
	private Map<Param, Integer> dftParam = new HashMap<>();
	//バトル時HP
	private Map<Param, Integer> btlParam = new HashMap<>();
	//生存orNot
	private boolean isLive;
	//効果の持続ターン数
	private Map<Skill, Integer> efDulation = new HashMap<>();
	//所持スキル
	private Skill[] skillSet;
	//ファーストスキル
	private FirstSkill firstSkill;
	//食いしばり
	private int guts = 0;
	//名前
	private String name;
	//下僕の名前
	private int servantNum;
	//陣営
	private Jinei jinei;

	public Creature(Category category, String name) {
		super();
		this.category = category;
		this.skillSet = new Skill[3];
		this.isLive = true;
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public Map<Param, Integer> getDftParam() {
		return dftParam;
	}

	public Map<Param, Integer> getBtlParam() {
		return btlParam;
	}

	public Map<Skill, Integer> getEfDulation() {
		return efDulation;
	}

	public void setEfDulation(Skill skill, Integer num) {
		this.efDulation.put(skill, num);
	}

	public void resetEfDulation() {
		this.efDulation = new HashMap<>();
	}

	public void setBtlParam(Param p, Integer num) {
		this.btlParam.put(p, num);
	}

	public void showParameter() {
		System.out.println("== " + name + "の強さ ==");
		System.out.println(
				"HP: " + btlParam.get(Param.HP) + "/" + dftParam.get(Param.HP) +
						" MP: " + btlParam.get(Param.MP) + "/" + dftParam.get(Param.MP) +
						" ATK: " + btlParam.get(Param.ATK) + "(" + dftParam.get(Param.ATK) + ")" +
						" DEF: " + btlParam.get(Param.DEF) + "(" + dftParam.get(Param.DEF) + ")" +
						" MAG: " + btlParam.get(Param.MAG) + "(" + dftParam.get(Param.MAG) + ")" +
						" SPD: " + btlParam.get(Param.SPD) + "(" + dftParam.get(Param.SPD) + ")" +
						" LUK: " + btlParam.get(Param.LUK) + "(" + dftParam.get(Param.LUK) + ")");
	}

	public void setParameter(Category category) {
		//勇者と魔王のパラム設定
		Random random = new Random();
		if (category == Category.DEMON) {
			Map<Param, Integer> parameterD = new HashMap<>();
			parameterD = new HashMap<>(Map.of(
					Param.HP, random.nextInt(8000) + 2000,
					Param.MP, random.nextInt(8000) + 2000,
					Param.ATK, random.nextInt(40) + 60,
					Param.DEF, random.nextInt(40) + 60,
					Param.SPD, random.nextInt(40) + 60,
					Param.MAG, random.nextInt(40) + 60,
					Param.LUK, random.nextInt(20) + 30));
			this.dftParam = parameterD;
			this.btlParam = new HashMap<>(parameterD);
		} else if (category == Category.HERO) {
			Map<Param, Integer> parameterH = new HashMap<>();
			parameterH = new HashMap<>(Map.of(
					Param.HP, random.nextInt(800) + 200,
					Param.MP, random.nextInt(800) + 200,
					Param.ATK, random.nextInt(90) + 10,
					Param.DEF, random.nextInt(90) + 10,
					Param.SPD, random.nextInt(90) + 10,
					Param.MAG, random.nextInt(90) + 10,
					Param.LUK, random.nextInt(100) + 10));
			int sp = random.nextInt(7);
			if (sp == 0) {
				parameterH.put(Param.HP, parameterH.get(Param.HP) + random.nextInt(8000));
			} else if (sp == 1) {
				parameterH.put(Param.MP, parameterH.get(Param.MP) + random.nextInt(8000));
			} else if (sp == 2) {
				parameterH.put(Param.ATK, parameterH.get(Param.ATK) + random.nextInt(100));
			}
			this.dftParam = parameterH;
			this.btlParam = new HashMap<>(parameterH);
		}
	}

	public void setParameter(String name) {
		//下僕のパラム設定
		Random random = new Random();
		Map<Param, Integer> parameterS = new HashMap<>();
		if (name.equals("スライム")) {
			parameterS = new HashMap<>(Map.of(
					Param.HP, random.nextInt(400) + 200,
					Param.MP, random.nextInt(400) + 200,
					Param.ATK, random.nextInt(20) + 30,
					Param.DEF, random.nextInt(20) + 30,
					Param.SPD, random.nextInt(20) + 30,
					Param.MAG, random.nextInt(20) + 30,
					Param.LUK, random.nextInt(20) + 30));
			this.dftParam = parameterS;
			this.btlParam = new HashMap<>(parameterS);
		}
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public Skill[] getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Skill[] skillSet) {
		this.skillSet = skillSet;
	}

	public FirstSkill getFirstSkill() {
		return firstSkill;
	}

	public void setFirstSkill(FirstSkill firstSkill) {
		this.firstSkill = firstSkill;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public int getGuts() {
		return guts;
	}

	public void setGuts(int guts) {
		this.guts = guts;
	}

	public Jinei getJinei() {
		return jinei;
	}

	public void setJinei(Jinei jinei) {
		this.jinei = jinei;
	}

	public int getServantNum() {
		return servantNum;
	}

	public void setServantNum(int servantNum) {
		this.servantNum = servantNum;
	}

	public String getServantName() {
		if (this.category == Category.DEMON || this.category == Category.HERO) {
			return this.name;
		} else {
			return this.name + this.servantNum;
		}
	}

	public static Creature createDemon() {
		Creature demon = new Creature(Creature.Category.DEMON, "魔王");
		demon.setParameter(Category.DEMON);
		Skill[] skills = new Skill[3];
		FirstSkill firstSkill = FirstSkill.MOU;
		Random r = new Random();
		for (int i = 0; i < demon.getSkillSet().length; i++) {
			skills[i] = Skill.values()[r.nextInt(Skill.values().length)];
		}
		demon.setSkillSet(skills);
		demon.setJinei(Jinei.DEMON);
		return demon;
	}

	public static Creature createHero(String name) {
		Creature hero = new Creature(Creature.Category.HERO, name);
		hero.setParameter(Category.HERO);
		hero.setJinei(Jinei.HERO);
		return hero;
	}

	public static Creature createServant(Creature p, ServantName sevName, List<Creature> servants) {
		Random r = new Random();
		Creature servant = null;
		String name = sevName.getName();
		if (p.getJinei() == Jinei.DEMON) {
			servant = new Creature(Creature.Category.SERVANTD, name);
		} else if (p.getJinei() == Jinei.HERO) {
			servant = new Creature(Creature.Category.SERVANTH, name);
		}
		servant.setParameter(name);
		int sevCount = 1;
		for (Creature sev : servants) {
			if (sev.getName().equals(name)) {
				sevCount += 1;
			}
		}
		servant.setServantNum(sevCount);
		if (name.equals("スライム")) {
			Skill[] sks = new Skill[1];
			for (int i = 0; i < servant.getSkillSet().length; i++) {
				sks[i] = Skill.values()[r.nextInt(Skill.values().length)];
			}
			servant.setSkillSet(sks);
		}
		return servant;
	}

	public void showAA(String category) {
		//アスキーアート(いらない、こんなのにいっぱい時間をかけたのは失敗だったかも)
		//微妙にコレじゃない感がある、直したいけどそんな時間ない
		if (category == "demon") {
			System.out.println("""
						　　　　　　 , ―-　＿　　　　　　 ＿ -― ､
						　　　　　　 ヽ　 　　　＼＿＿／　 　 　　ﾉ
						　　　　　 　 　＼ 　　　　　　　　　　　／
						　　　　　　　　　 ヽ.　,―､　 ,―､　　/　 　 　　, ―-o､
						　　　　　　 　 ／　l　|＿_ V＿_ |　/ヽヽ　 　　､三｀　 二つ
						 　 　　　　 〃　　ヽ八_･_八_･_八/　 ヽヽ　　 　　} （
						 　 , -l⌒ヽ＼_ 　＿|　,､＿＿,､　|　　 ／―- ､ 　　） ）
					　 　 |　 ヽ　｀ｰ一´_ 人 ｀二二´ ノ _／○　　　 |,-（ （
					 　 ￣￣　 　 　 ﾉ) ＼ 　 |　|　 ／　○　 　 ／'V~(￣ヽ
						　　  l二= 　 　 　 ﾉ　○　＼V／　 ○　 　　 ﾉ 　l_ (　 ｝
						　　 ( __ -― 7　 /|ヽ　 ○, ― ､○　 　　　 / 　　/ (＿ノ
						　 　　 　　 ｀-´/　|　 　{（°）}　　　　　 　　 /／|　||
						　 　 |　 　 　 | /　　 |　　｀ｰ－´　　　　 　 /´　 ﾉ ﾉ|
						　 　 |　 　 　 /　 　　|　　　　　　　　　　 / 　　{ { |
						　 　 }　 　　 {　 　 　 ヾ＼　 　　 ＿ノ　　| 　　 | | |
						　 　 {　 　　|　　　　　 |　　￣￣　　　　　　　  　| | |
					  		ヽ_ 　 |　　　　　　｀ ――　　　　　 　 　  _| |ノ
						　 　 （　￣ >―----　　　　　　　　　　　 　 ￣　   | |)
						　 　　　￣ ｀ ―-――――----――――----------------´￣|」

						                    ⚪️
						                    大
									""");
		}
	}
}
