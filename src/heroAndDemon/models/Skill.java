package heroAndDemon.models;

public enum Skill {
	ION("イオナズン", SkillType.ATTACK, 80, 1, Attribute.THUNDER), BKT("バイキルト", SkillType.BUFF, 2, 3, Attribute.ATKBF), PPT(
			"パルプンテ", SkillType.SPECIAL, 0, 1,
			Attribute.NONE), NGR("殴る", SkillType.ATTACK, 10, 1, Attribute.NONE), MMR("身を守る", SkillType.BUFF, 2, 3,
					Attribute.DEFBF), DKI("どくの息", SkillType.CONERROR, 0, 3, Attribute.POISON), MER("メラ",
							SkillType.ATTACK, 20, 1, Attribute.FIRE), NKM("仲間をよぶ", SkillType.SPECIAL, 0, 0,
									Attribute.NONE), HIM("ホイミ", SkillType.HEAL, 0, 1, Attribute.NONE);
	//案　仲間を呼ぶ　敵味方全員に付与する効果、ダメージ　自傷して強力なやつ

	private final String name;
	private final SkillType type;
	private final int power;
	private final int dulation;
	private final Attribute atrbt;

	public enum SkillType {
		ATTACK, HEAL, BUFF, DEBUFF, CONERROR, SPECIAL
	}

	public enum Attribute {
		FIRE, THUNDER, ICE, ATKBF, DEFBF, ATKDBF, DEFDBF, NONE, POISON, SLEEP, CONFUSION
	}

	private Skill(String name, SkillType type, int power, int dulation, Attribute atrbt) {
		this.name = name;
		this.type = type;
		this.power = power;
		this.dulation = dulation;
		this.atrbt = atrbt;
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

	public Attribute getAtrbt() {
		return atrbt;
	}

}
