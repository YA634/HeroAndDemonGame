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

	private Map<Param, Integer> parameter = new HashMap<>();
	private boolean isLive;

	private enum SkillSet {
		ION("イオナズン"), BKT("バイキルト"), PPT("パルプンテ");

		private final String name;

		SkillSet(String name) {
			this.name = name;
		}

		public String getname() {
			return name;
		}
	};

	private SkillSet[] skillSet;

	public Creature(Category category) {
		super();
		this.category = category;
		this.isLive = true;
	}

	public Map<Param, Integer> getParameter() {
		return parameter;
	}

	public void showParameter() {
		System.out.println("== HEROの強さ ==");
		System.out.println(
				"HP: " + parameter.get(Param.HP) +
						" MP: " + parameter.get(Param.MP) +
						" ATK: " + parameter.get(Param.ATK) +
						" DEF: " + parameter.get(Param.DEF) +
						" MAG: " + parameter.get(Param.MAG) +
						" SPD: " + parameter.get(Param.SPD) +
						" LUK: " + parameter.get(Param.LUK));
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
			this.parameter = parameterD;
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
			this.parameter = parameterH;
		}
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public SkillSet[] getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(SkillSet[] skillSet) {
		this.skillSet = skillSet;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
