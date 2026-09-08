package heroAndDemon.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Creature {
	public enum Category {
		HERO, DEMON
	}

	private Category category;

	public enum Param {
		HP, MP, ATK, DEF, SPD, MAG, LUK
	}

	private Map<Param, Integer> dftParam = new HashMap<>();
	private Map<Param, Integer> btlParam = new HashMap<>();
	private boolean isLive;

	private Skill[] skillSet;

	private String name;

	public Creature(Category category, String name) {
		super();
		this.category = category;
		this.skillSet = new Skill[3];
		this.isLive = true;
		this.name = name;
	}

	public Map<Param, Integer> getDftParam() {
		return dftParam;
	}

	public Map<Param, Integer> getBtlParam() {
		return btlParam;
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
			this.btlParam = parameterD;
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
			this.btlParam = parameterH;
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

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public static Creature createDemon() {
		Creature demon = new Creature(Creature.Category.DEMON, "魔王");
		demon.setParameter(Category.DEMON);
		Skill[] skills = new Skill[3];
		Random r = new Random();
		for (int i = 0; i < demon.getSkillSet().length; i++) {
			skills[i] = Skill.values()[r.nextInt(Skill.values().length)];
		}
		return demon;
	}

	public static Creature createHero(String name) {
		Creature hero = new Creature(Creature.Category.HERO, name);
		hero.setParameter(Category.HERO);
		return hero;
	}

	public void showAA(String category) {
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
