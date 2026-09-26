package heroAndDemon.models;

public enum Skill implements UsableSkill {
	NGR("殴る", SkillType.ATTACK, 10, 1, 0, Attribute.NONE),
	MMR("身を守る", SkillType.BUFF, 2, 3, 0, Attribute.DEFBF),
	ION("イオナズン(60)", SkillType.MAGIC, 80, 1, 60, Attribute.THUNDER),
	BKT("バイキルト(10)", SkillType.BUFF, 2, 3, 10, Attribute.ATKBF),
	RKN("ルカナン(10)", SkillType.DEBUFF, 2, 3, 10, Attribute.DEFDBF),
	DKI("どくの息(10)", SkillType.CONERROR, 0, 3, 10, Attribute.POISON),
	MER("メラ(10)", SkillType.MAGIC, 20, 1, 10, Attribute.FIRE),
	MHD("マヒャド(10)", SkillType.MAGIC, 20, 1, 10, Attribute.ICE),
	HIM("ホイミ(10)", SkillType.HEAL, 0, 1, 10, Attribute.NONE),
	RRH("ラリホー(30)", SkillType.CONERROR, 0, 3, 30, Attribute.SLEEP),
	MDP("メダパニ(10)", SkillType.CONERROR, 0, 3, 10, Attribute.CONFUSION),
	MGT("メガンテ(HP全て)", SkillType.SPECIAL, 0, 0, 0, Attribute.NONE),
	MDT("マダンテ(MP全て)", SkillType.SPECIAL, 0, 0, 0, Attribute.NONE),
	SSK("死の宣告(99)", SkillType.CONERROR, 0, 20, 99, Attribute.DEATH), //20ターン後に相手確定で死亡
	PPT("パルプンテ(0)(未実装)", SkillType.SPECIAL, 0, 1, 0, Attribute.NONE),
	//	NKM("仲間をよぶ(50)(未実装)", SkillType.SPECIAL, 0, 0, 50, Attribute.NONE), //未実装
	//	MTO("メテオ(50)(未実装)", SkillType.SPECIAL, 0, 1, 50, Attribute.NONE)//大小の隕石をよぶ、敵味方全体にダメージ
	//  TGM("つるぎのまい(0)(未実装)", SkillType.SPECIAL, 20, 1, 0, Attribute.NONE),//4回攻撃
	//案　仲間を呼ぶ　敵味方全員に付与する効果、ダメージ　自傷して強力なやつ ターン経過毎に強くなっていくやつ
	;

	private final String name;
	private final SkillType type;
	private final int power;
	private final int dulation;
	private final int useMP;
	private final Attribute atrbt;

	private Skill(String name, SkillType type, int power, int dulation, int useMP, Attribute atrbt) {
		this.name = name;
		this.type = type;
		this.power = power;
		this.dulation = dulation;
		this.useMP = useMP;
		this.atrbt = atrbt;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SkillType getType() {
		return type;
	}

	@Override
	public int getPower() {
		return power;
	}

	@Override
	public int getDulation() {
		return dulation;
	}

	@Override
	public int getUseMP() {
		return useMP;
	}

	@Override
	public Attribute getAtrbt() {
		return atrbt;
	}

}
