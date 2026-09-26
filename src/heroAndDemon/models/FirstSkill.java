package heroAndDemon.models;

public enum FirstSkill implements UsableSkill {
	MOU("魔王の覇気(F)(未実装)", SkillType.FIRST, 0, 0, 0, Attribute.NONE),
	BNS("バーニングソウル(F)(未実装)", SkillType.FIRST, 0, 0, 0, Attribute.NONE), //毎ターン10%HP減少
	MGS("マジシャンズソウル(F)(未実装)", SkillType.FIRST, 0, 0, 0, Attribute.NONE),
	HRS("ヒーローズソウル(F)(未実装)", SkillType.FIRST, 0, 0, 0, Attribute.NONE), //HP0になったら全回復+バフで復活
	HTT("不退転の覚悟(F)(未実装)", SkillType.FIRST, 0, 0, 0, Attribute.NONE),
	GYB("運命の女神に愛されたもの(F)(未実装)", SkillType.FIRST, 0, 0, 0, Attribute.NONE), //技全部コイントス、表なら2倍裏ならファンブル
	;
	//案　仲間を呼ぶ　敵味方全員に付与する効果、ダメージ　自傷して強力なやつ

	private final String name;
	private final SkillType type;
	private final int power;
	private final int dulation;
	private final int useMP;
	private final Attribute atrbt;

	private FirstSkill(String name, SkillType type, int power, int dulation, int useMP, Attribute atrbt) {
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
