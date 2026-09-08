package heroAndDemon.models;

public enum Skill {
	ION("イオナズン", SkillType.ATTACK, 80, 1), BKT("バイキルト", SkillType.BUFF, 2, 3), PPT("パルプンテ", SkillType.SPECIAL, 0, 1);

	private final String name;
	private final SkillType type;
	private final int power;
	private final int dulation;

	public enum SkillType {
		ATTACK, HEAL, BUFF, DEBUFF, SPECIAL
	}

	private Skill(String name, SkillType type, int power, int dulation) {
		this.name = name;
		this.type = type;
		this.power = power;
		this.dulation = dulation;
	}

	public String getName() {
		return name;
	}

	public SkillType getType() {
		return type;
	}

	public int getPower() {
		return power;
	}

	public int getDulation() {
		return dulation;
	}
}
