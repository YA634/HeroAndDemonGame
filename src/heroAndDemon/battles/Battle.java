package heroAndDemon.battles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import heroAndDemon.inputs.InputUtil;
import heroAndDemon.models.Attribute;
import heroAndDemon.models.Creature;
import heroAndDemon.models.Creature.Category;
import heroAndDemon.models.Creature.Jinei;
import heroAndDemon.models.Creature.Param;
import heroAndDemon.models.FirstSkill;
import heroAndDemon.models.Skill;
import heroAndDemon.models.SkillType;
import heroAndDemon.models.Target;

public class Battle {
	public int start(Creature demon, String name) {
		Creature hero = skillselect(demon, name);
		int btlResult = battle(hero, demon);
		return btlResult;
	}

	private Creature skillselect(Creature demon, String name) {
		InputUtil input = new InputUtil();
		Creature hero = Creature.createHero(name);
		hero.showParameter();
		System.out.println("1:easy　2:hard");
		int mode = input.readMenuChoice("モードを選択してください", 2);

		if (mode == 0) {
			//全ての選択肢から取得
			System.out.println("== 取得可能なパッシブスキル ==");
			List<FirstSkill> FSkillList = new ArrayList<>();
			int indexNum1 = 0;
			for (FirstSkill fsk : FirstSkill.values()) {
				if (fsk == FirstSkill.MOU) {
					continue;
				}
				indexNum1++;
				FSkillList.add(fsk);
				System.out.println(indexNum1 + ":" + fsk.getName());
			}
			FirstSkill fskSel = input.readSkill("取得するパッシブスキルを選択してください", FSkillList, mode);
			hero.setFirstSkill(fskSel);

			System.out.println("== 取得可能なスキル ==");
			int indexNum = 0;
			for (Skill skill : Skill.values()) {
				if (skill == Skill.NGR || skill == Skill.MMR) {
					continue;
				}
				indexNum++;
				System.out.println((indexNum) + ":" + skill.getName());
			}
			Skill[] skillSet = new Skill[3];
			for (int i = 0; i < hero.getSkillSet().length; i++) {
				skillSet[i] = input.readSkill((i + 1) + "つ目のスキルを選択してください");
				System.out.println(skillSet[i]);
			}
			hero.setSkillSet(skillSet);
		} else if (mode == 1) {
			//		3つの選択肢から取得
			System.out.println("== 取得可能なパッシブスキル ==");
			List<FirstSkill> FSkillList = new ArrayList<>();
			for (FirstSkill fsk : FirstSkill.values()) {
				if (fsk == FirstSkill.MOU) {
					continue;
				}
				FSkillList.add(fsk);
			}
			Collections.shuffle(FSkillList);
			for (int i = 0; i < 3; i++) {
				System.out.println((i + 1) + ":" + FSkillList.get(i).getName());
			}
			FirstSkill fskSel = input.readSkill("取得するパッシブスキルを選択してください", FSkillList, mode);
			hero.setFirstSkill(fskSel);

			System.out.println("== 取得可能なスキル ==");
			Skill[] skillSet = new Skill[3];
			List<Skill> skillList = new ArrayList<>();
			for (Skill skill : Skill.values()) {
				if (skill == Skill.NGR || skill == Skill.MMR) {
					continue;
				}
				skillList.add(skill);
			}
			List<Skill> selected = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				List<Skill> skillList2 = new ArrayList<>(skillList);
				skillList2.removeAll(selected);
				Collections.shuffle(skillList2);
				for (int k = 0; k < 3; k++) {
					System.out.println((k + 1) + ":" + skillList2.get(k).getName());
				}
				Skill sk = input.readSkill((i + 1) + "つ目のスキルを選択してください", skillList2);
				selected.add(sk);
				skillSet[i] = sk;
			}
			hero.setSkillSet(skillSet);
		}
		return hero;
	}

	private int battle(Creature hero, Creature demon) {
		InputUtil input = new InputUtil();
		Random r = new Random();
		int turn = 0;
		List<Creature> servants = new ArrayList<>();
		List<Creature> creatures = new ArrayList<>();
		List<Creature> creSorted = new ArrayList<>();
		List<Creature> creH = new ArrayList<>();
		List<Creature> creD = new ArrayList<>();
		List<Creature> defCreH = new ArrayList<>();
		List<Creature> defCreD = new ArrayList<>();
		creatures.clear();
		creatures.add(hero);
		creatures.add(demon);
		creH.add(hero);
		creD.add(demon);
		defCreH.add(hero);
		defCreD.add(demon);
		while (hero.isLive() && demon.isLive()) {
			demon.showAA("demon");
			sleep(1);
			int[] MenuSelect = menuControl(hero, creD);
			if (MenuSelect[0] == -1) {
				return 4;
			}
			int[] hsMenuSelect;
			Map<Creature, int[]> SMenuSelect = new HashMap<>();
			SMenuSelect.put(hero, MenuSelect);
			if (!servants.isEmpty()) {
				for (Creature servant : servants) {
					if (servant.getCategory() == Category.SERVANTD) {
						int[] de = { 1, 0 };
						SMenuSelect.put(servant, de);
					} else if (servant.getCategory() == Category.SERVANTH) {
						hsMenuSelect = menuControl(servant, creD);
						SMenuSelect.put(servant, hsMenuSelect);
					}
				}
			}
			turn++;
			if (hero.isLive()) {
				//firstSkill
				if (turn == 1) {
					useFSkill(demon);
					useFSkill(hero);
				}
				Creature p1 = null;
				Creature p2 = null;
				creatures.clear();
				creatures.add(hero);
				creatures.add(demon);
				for (Creature cre : servants) {
					creatures.add(cre);
					if (cre.getJinei() == Jinei.DEMON) {
						defCreD.add(cre);
						creD.add(cre);
					} else if (cre.getJinei() == Jinei.HERO) {
						defCreH.add(cre);
						creH.add(cre);
					}
				}
				if (servants.isEmpty()) {
					p1 = spdJudge(hero, demon);
					if (p1 == hero) {
						p2 = demon;
					} else {
						p2 = hero;
					}
				} else {
					creSorted = spdJudge(creatures);
				}
				if (servants.isEmpty()) {
					if (p1.getEfDulation().containsKey(Attribute.SLEEP)) {
						System.out.println(p1.getName() + "はまだ眠っている！");
					} else {
						if (p1.getEfDulation().containsKey(Attribute.CONFUSION)) {
							int con = r.nextInt(2);
							if (con == 0) {
								int cDmg = p1.getBtlParam().get(Param.HP) * 3 / 100;
								System.out.println(p1.getName() + "は混乱して自身に攻撃を放った！");
								System.out.println(cDmg + "ダメージ！!");
								p1.setBtlParam(Param.HP, p1.getBtlParam().get(Param.HP) - cDmg);
							} else {
								useSkill(MenuSelect[0], p1, p2, servants);
							}
						} else {
							useSkill(MenuSelect[0], p1, p2, servants);
						}
						sleep(1);
						isLiveJudge(creatures, servants, creH, creD);
						if (demon.isLive() && hero.isLive()) {
							if (p2.getEfDulation().containsKey(Attribute.SLEEP)) {
								System.out.println(p2.getName() + "はまだ眠っている！");
							} else {
								if (p2.getEfDulation().containsKey(Attribute.CONFUSION)) {
									int con = r.nextInt(2);
									if (con == 0) {
										int cDmg = p2.getBtlParam().get(Param.HP) * 3 / 100;
										System.out.println(p2.getName() + "は混乱して自身に攻撃を放った！");
										System.out.println(cDmg + "ダメージ！!");
										p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - cDmg);
									} else {
										useSkill(MenuSelect[0], p2, p1, servants);
									}
								} else {
									useSkill(MenuSelect[0], p2, p1, servants);
								}
							}
						}
						isLiveJudge(creatures, servants, creH, creD);
					}
				} else {
					for (Creature cre : creSorted) {
						List<Creature> defCreE = new ArrayList<>();
						List<Creature> defCreC = new ArrayList<>();
						if (cre.getJinei() == Jinei.DEMON) {
							defCreE = defCreH;
							defCreC = defCreD;
						} else {
							defCreE = defCreD;
							defCreC = defCreH;
						}
						if (cre.getEfDulation().containsKey(Attribute.SLEEP)) {
							System.out.println(cre.getServantName() + "はまだ眠っている！");
						} else {
							if (cre.getEfDulation().containsKey(Attribute.CONFUSION)) {
								int con = r.nextInt(2);
								if (con == 0) {
									int cDmg = cre.getBtlParam().get(Param.HP) * 3 / 100;
									System.out.println(cre.getServantName() + "は混乱して自身に攻撃を放った！");
									System.out.println(cDmg + "ダメージ！!");
									cre.setBtlParam(Param.HP, cre.getBtlParam().get(Param.HP) - cDmg);
								} else {
									useSkill(SMenuSelect, cre, defCreE, defCreC, servants);
								}
							} else {
								useSkill(SMenuSelect, cre, defCreE, defCreC, servants);
							}
						}
						sleep(1);
						isLiveJudge(creatures, servants, creH, creD);
					}
				}
				sleep(1);
				endFaze(hero, turn);
				endFaze(demon, turn);
				isLiveJudge(creatures, servants, creH, creD);
				hero.showParameter();
				demon.showParameter();
				sleep(1);
				//				defCreD = creD;
				//				defCreH = creH;
			}
		}
		if (!demon.isLive()) { //勇者勝ち
			if (!hero.isLive()) {
				return 5;
			}
			return 1;
		} else if (!hero.isLive()) { //魔王勝ち
			return 2;
		} else { //なし
			return 3;
		}
	}

	private void useSkill(int skillNum, Creature p1, Creature p2, List<Creature> servants) {
		Random r = new Random();
		if (p1.getJinei() == Jinei.DEMON) {
			int skillLength = p1.getSkillSet().length;
			skillNum = r.nextInt(skillLength + 2) + 1;
		}
		if (skillNum == 1) {
			System.out.println(p1.getName() + "は" + p2.getName() + "に殴りかかった！");
			int dmg = dmgJudge(p1, p2, 1, Skill.NGR);
			System.out.println(dmg + "ダメージ！！");
			p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		} else if (skillNum == 2) {
			System.out.println(p1.getName() + "は自身の守りを固めた！");
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 2);
			p1.setEfDulation(Skill.MMR, Skill.MMR.getDulation());
		} else {
			Skill skill = p1.getSkillSet()[skillNum - 3];
			if (p1.getBtlParam().get(Param.MP) < skill.getUseMP()) {
				System.out.println("MPが足りない！");
			} else {
				p1.setBtlParam(Param.MP, p1.getBtlParam().get(Param.MP) - skill.getUseMP());
				System.out.println(p1.getName() + "は" + skill.getName() + "を使った");
				//色々処理
				if (skill.getType() == SkillType.ATTACK) {
					int dmg = dmgJudge(p1, p2, skill.getPower(), skill);
					System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
					p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
				} else if (skill.getType() == SkillType.MAGIC) {
					int dmg = mDmgJudge(p1, p2, skill.getPower(), skill);
					System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
					p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
				} else if (skill.getType() == SkillType.HEAL) {
					healAction(p1, skill);
				} else if (skill.getType() == SkillType.BUFF) {
					bfAction(p1, skill);
				} else if (skill.getType() == SkillType.DEBUFF) {
					dbfAction(p1, p2, skill);
				} else if (skill.getType() == SkillType.CONERROR) {
					conErrorAction(p1, p2, skill);
				} else if (skill.getType() == SkillType.SPECIAL) {
					specialAction(p1, p2, skill, servants);
				}
			}
		}
	}

	private void useSkill(Map<Creature, int[]> skillNumTarget, Creature p1, List<Creature> defCreE,
			List<Creature> defCreC, List<Creature> servants) {
		//対複数　今後実装予定
		Random r = new Random();
		//		if (skillNumTarget.size() <= 1) {
		int skillNum = skillNumTarget.get(p1)[0];
		int targetNum = skillNumTarget.get(p1)[1];
		Creature p2 = defCreE.get(targetNum);
		if (p1.getJinei() == Jinei.DEMON) {
			int skillLength = p1.getSkillSet().length;
			skillNum = r.nextInt(skillLength + 2) + 1;
		}
		if (skillNum == 1) {
			System.out.println(p1.getName() + "は" + p2.getName() + "に殴りかかった！");
			int dmg = dmgJudge(p1, p2, 1, Skill.NGR);
			System.out.println(dmg + "ダメージ！！");
			p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		} else if (skillNum == 2) {
			System.out.println(p1.getName() + "は自身の守りを固めた！");
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 2);
			p1.setEfDulation(Skill.MMR, Skill.MMR.getDulation());
		} else {
			Skill skill = p1.getSkillSet()[skillNum - 3];
			if (p1.getBtlParam().get(Param.MP) < skill.getUseMP()) {
				System.out.println("MPが足りない！");
			} else {
				p1.setBtlParam(Param.MP, p1.getBtlParam().get(Param.MP) - skill.getUseMP());
				System.out.println(p1.getName() + "は" + skill.getName() + "を使った");
				//色々処理
				if (skill.getType() == SkillType.ATTACK) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreE) {
							int dmg = dmgJudge(p1, p, skill.getPower(), skill);
							System.out.println(p.getName() + "に" + dmg + "ダメージ！！");
							p.setBtlParam(Param.HP, p.getBtlParam().get(Param.HP) - dmg);
						}
					} else if (skill.getTarget() == Target.SINGLE) {
						int dmg = dmgJudge(p1, p2, skill.getPower(), skill);
						System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
						p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
					}
				} else if (skill.getType() == SkillType.MAGIC) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreE) {
							int dmg = mDmgJudge(p1, p, skill.getPower(), skill);
							System.out.println(p.getName() + "に" + dmg + "ダメージ！！");
							p.setBtlParam(Param.HP, p.getBtlParam().get(Param.HP) - dmg);
						}
					} else {
						int dmg = mDmgJudge(p1, p2, skill.getPower(), skill);
						System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
						p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
					}
				} else if (skill.getType() == SkillType.HEAL) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreC) {
							healAction(p, skill);
						}
					} else {
						healAction(p1, skill);
					}
				} else if (skill.getType() == SkillType.BUFF) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreC) {
							bfAction(p, skill);
						}
					} else {
						bfAction(p1, skill);
					}
				} else if (skill.getType() == SkillType.DEBUFF) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreE) {
							dbfAction(p1, p, skill);
						}
					} else {
						dbfAction(p1, p2, skill);
					}
				} else if (skill.getType() == SkillType.CONERROR) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreE) {
							dbfAction(p1, p, skill);
						}
					} else {
						conErrorAction(p1, p2, skill);
					}
				} else if (skill.getType() == SkillType.SPECIAL) {
					if (skill.getTarget() == Target.MULTIPLE) {
						for (Creature p : defCreE) {
							specialAction(p1, p, skill, servants);
						}
					} else {
						specialAction(p1, p2, skill, servants);
					}
				}
			}
		}
		//	}else
		//
		//	{
		//		Set<Creature> p1List = skillNumTarget.keySet();
		//		for (Creature pi : p1List) {
		//			int skillNum = skillNumTarget.get(pi)[0];
		//			int targetNum = skillNumTarget.get(pi)[1];
		//			Creature p2 = defCre.get(targetNum);
		//			if (pi.getJinei() == Jinei.DEMON) {
		//				int skillLength = pi.getSkillSet().length;
		//				skillNum = r.nextInt(skillLength + 2) + 1;
		//			}
		//			if (skillNum == 1) {
		//				System.out.println(pi.getName() + "は" + p2.getName() + "に殴りかかった！");
		//				int dmg = dmgJudge(pi, p2, 1, Skill.NGR);
		//				System.out.println(dmg + "ダメージ！！");
		//				p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		//			} else if (skillNum == 2) {
		//				System.out.println(pi.getName() + "は自身の守りを固めた！");
		//				pi.setBtlParam(Param.DEF, pi.getBtlParam().get(Param.DEF) * 2);
		//				pi.setEfDulation(Skill.MMR, Skill.MMR.getDulation());
		//			} else {
		//				Skill skill = pi.getSkillSet()[skillNum - 3];
		//				if (pi.getBtlParam().get(Param.MP) < skill.getUseMP()) {
		//					System.out.println("MPが足りない！");
		//				} else {
		//					pi.setBtlParam(Param.MP, pi.getBtlParam().get(Param.MP) - skill.getUseMP());
		//					System.out.println(pi.getName() + "は" + skill.getName() + "を使った");
		//					//色々処理
		//					if (skill.getType() == SkillType.ATTACK) {
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								int dmg = dmgJudge(pi, p, skill.getPower(), skill);
		//								System.out.println(p.getName() + "に" + dmg + "ダメージ！！");
		//								p.setBtlParam(Param.HP, p.getBtlParam().get(Param.HP) - dmg);
		//							}
		//						} else if (skill.getTarget() == Target.SINGLE) {
		//							int dmg = dmgJudge(pi, p2, skill.getPower(), skill);
		//							System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
		//							p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		//						}
		//					} else if (skill.getType() == SkillType.MAGIC) {
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								int dmg = mDmgJudge(pi, p, skill.getPower(), skill);
		//								System.out.println(p.getName() + "に" + dmg + "ダメージ！！");
		//								p.setBtlParam(Param.HP, p.getBtlParam().get(Param.HP) - dmg);
		//							}
		//						} else {
		//							int dmg = mDmgJudge(pi, p2, skill.getPower(), skill);
		//							System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
		//							p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		//						}
		//					} else if (skill.getType() == SkillType.HEAL) {
		//						//修正必要
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								healAction(p, skill);
		//							}
		//						} else {
		//							healAction(pi, skill);
		//						}
		//					} else if (skill.getType() == SkillType.BUFF) {
		//						//修正必要かも
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								bfAction(p, skill);
		//							}
		//						} else {
		//							bfAction(pi, skill);
		//						}
		//					} else if (skill.getType() == SkillType.DEBUFF) {
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								dbfAction(pi, p, skill);
		//							}
		//						} else {
		//							dbfAction(pi, p2, skill);
		//						}
		//					} else if (skill.getType() == SkillType.CONERROR) {
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								dbfAction(pi, p, skill);
		//							}
		//						} else {
		//							conErrorAction(pi, p2, skill);
		//						}
		//					} else if (skill.getType() == SkillType.SPECIAL) {
		//						if (skill.getTarget() == Target.MULTIPLE) {
		//							for (Creature p : defCre) {
		//								specialAction(pi, p, skill);
		//							}
		//						} else {
		//							specialAction(pi, p2, skill);
		//						}
		//					}
		//				}
		//			}
		//		}

	}

	private void useFSkill(Creature p1) {
		//ファーストスキル、特性
		FirstSkill fsk = p1.getFirstSkill();
		if (fsk == FirstSkill.BNS) {
			System.out.println(p1.getName() + "の魂が燃え上がる！");
			System.out.println("無限の力が湧いてくる！！");
			p1.setBtlParam(Param.ATK, p1.getBtlParam().get(Param.ATK) * 2);
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 2);
			p1.setBtlParam(Param.MAG, p1.getBtlParam().get(Param.MAG) * 2);
			p1.setBtlParam(Param.SPD, p1.getBtlParam().get(Param.SPD) * 2);
		} else if (fsk == FirstSkill.GYB) {
			System.out.println("運命の女神が" + p1.getName() + "に祝福を与える！");
			System.out.println(p1.getName() + "は自身の運命力に恍惚とする！！");
			p1.setBtlParam(Param.LUK, p1.getBtlParam().get(Param.LUK) + 100);
		} else if (fsk == FirstSkill.MOU) {
			System.out.println(p1.getName() + "が圧倒的な威圧を放つ！");
			System.out.println(p1.getName() + "に力が満ちてゆく！");
			p1.setBtlParam(Param.ATK, p1.getBtlParam().get(Param.ATK) * 2);
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 2);
			p1.setBtlParam(Param.MAG, p1.getBtlParam().get(Param.MAG) * 2);
			p1.setBtlParam(Param.SPD, p1.getBtlParam().get(Param.SPD) * 2);
		} else if (fsk == FirstSkill.MGS) {
			System.out.println(p1.getName() + "に過去の偉大な大魔導士たちが力を託す！");
			System.out.println(p1.getName() + "は無限の魔力が湧いてきた！");
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) / 2);
			p1.setBtlParam(Param.MP, p1.getBtlParam().get(Param.MP) + 1000);
			p1.setBtlParam(Param.MAG, p1.getBtlParam().get(Param.MAG) * 4);
		} else if (fsk == FirstSkill.HTT) {
			System.out.println(p1.getName() + "に不退転の覚悟が満ちてゆく！");
			System.out.println("動かざること山の如し。不動の姿、今見せん！！");
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * 4);
			p1.setBtlParam(Param.SPD, p1.getBtlParam().get(Param.SPD) / 2);
			p1.setGuts(1);
		} else if (fsk == FirstSkill.HRS) {
			System.out.println("勇者の覚悟が、勇者たる所以！");
			System.out.println("魔王との因縁を終わらせる意志が" + p1.getName() + "をこの戦いへと導いた！！");
			p1.setGuts(1);
		}
	}

	private void showMenu(int option, Creature hero) {
		Skill[] skillSet = new Skill[3];
		if (option == 0) {
			System.out.println("1:スキル");
			System.out.println("2:ポーズ");
		} else if (option == 1) {
			System.out.println("1:サレンダー");
			System.out.println("2:バトルを終了");
			System.out.println("3:戻る");
		} else if (option == 2) {
			System.out.println(" 1:戻る");
			System.out.println("2:殴る");
			System.out.println("3:身を守る");
			for (int i = 0; i < hero.getSkillSet().length; i++) {
				System.out.println((4 + i) + ":" + hero.getSkillSet()[i].getName());
			}
		} else if (option == 1 || option == 3 || option == 4) {

		}
	}

	private void showMenu(int option, List<Creature> creatures) {
		if (option == 10) {
			System.out.println("対象を選択して下さい");
			for (int i = 0; i < creatures.size(); i++) {
				System.out.println((1 + i) + ":" + creatures.get(i).getName());
			}
		}
	}

	private int[] menuControl(Creature p, List<Creature> creatures) {
		InputUtil input = new InputUtil();
		int MenuSelect = 2;
		int targetSelect = 0;
		int[] re = { -1, 0 };
		System.out.println();
		System.out.println("== メニュー ==");
		while (MenuSelect == 2) {
			showMenu(0, p);
			MenuSelect = input.readMenuChoice("数字を入力→", 2);
			if (MenuSelect == 1) {//ポーズ選択の場合
				showMenu(1, p);
				MenuSelect = input.readMenuChoice("数字を入力→", 3);
				if (MenuSelect == 0) {//サレンダー
					p.setLive(false);
				} else if (MenuSelect == 1) {//ゲームをやめる
					return re;
				}
			} else if (MenuSelect == 0) {//スキル選択の場合
				showMenu(2, p);
				System.out.println(p.getName() + "の行動を選択");
				MenuSelect = input.readMenuChoice("数字を入力→", 3 + p.getSkillSet().length);
				if (MenuSelect == 0) {//戻る選択の場合
					MenuSelect = 2;
				}
				if (creatures.size() > 1) {
					if (p.getSkillSet()[MenuSelect - 3].getTarget() == Target.SINGLE) {
						showMenu(10, creatures);
						targetSelect = input.readMenuChoice("数字を入力→", creatures.size());
					}
				}
			}
		}
		re[0] = MenuSelect;
		re[1] = targetSelect;
		return re;
	}

	private Creature spdJudge(Creature hero, Creature demon) {
		Random r = new Random();
		int hSPD = hero.getBtlParam().get(Param.SPD) + r.nextInt(30);
		int dSPD = demon.getBtlParam().get(Param.SPD) + r.nextInt(30);
		if (hSPD > dSPD) {
			return hero;
		} else {
			return demon;
		}
	}

	private List<Creature> spdJudge(List<Creature> creatures) {
		Random r = new Random();
		List<Creature> creSorted = new ArrayList<>();
		for (Creature cre : creatures) {
			if (creSorted.isEmpty()) {
				creSorted.add(cre);
			} else {
				for (int i = 0; i < creSorted.size(); i++) {
					int oSp = creSorted.get(i).getBtlParam().get(Param.SPD) + r.nextInt(30);
					int sp = cre.getBtlParam().get(Param.SPD) + r.nextInt(30);
					if (oSp > sp && i == creSorted.size() - 1) {
						creSorted.add(i + 1, cre);
						break;
					} else if (oSp < sp) {
						creSorted.add(i, cre);
						break;
					}
				}
			}
		}
		return creSorted;
	}

	private boolean avoidJudge(Creature p1, Creature p2, int option) {
		Random r = new Random();
		int p1Spd = p1.getBtlParam().get(Param.SPD) + r.nextInt(50);
		int p2Spd = p2.getBtlParam().get(Param.SPD) + r.nextInt(50);
		if (p1Spd > p2Spd + option) {
			//回避成功
			return true;
		} else {
			//回避失敗
			return false;
		}
	}

	private int dmgJudge(Creature p1, Creature p2, int option, Skill sk) {
		//ダメージ計算
		Random r = new Random();
		if (avoidJudge(p1, p2, 50)) {
			System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			return 0;
		} else {
			int cri = r.nextInt(100) + 1;
			int oCri = r.nextInt(100) + 1;
			int pCri = p1.getBtlParam().get(Param.LUK);
			int dmg;
			if (cri <= pCri) {
				if (oCri <= pCri - 100) {
					System.out.println("OVER CRITICAL！！！");
					dmg = (p1.getBtlParam().get(Param.ATK) + option + r.nextInt(100)) * 8
							- p2.getBtlParam().get(Param.DEF);
				} else {
					System.out.println("CRITICAL!!");
					dmg = (p1.getBtlParam().get(Param.ATK) + option + r.nextInt(100)) * 4
							- p2.getBtlParam().get(Param.DEF);
				}
			} else {
				dmg = p1.getBtlParam().get(Param.ATK) + option + r.nextInt(100) - p2.getBtlParam().get(Param.DEF);
			}
			if (dmg < 0) {
				return 0;
			}
			return dmg;
		}
	}

	private int mDmgJudge(Creature p1, Creature p2, int option, Skill sk) {
		//魔法ダメージ計算
		Random r = new Random();
		if (avoidJudge(p1, p2, 50)) {
			System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			return 0;
		} else {
			int dmg = p1.getBtlParam().get(Param.MAG) * 3 / 2 + option + r.nextInt(100)
					- p2.getBtlParam().get(Param.DEF);
			if (dmg < 0) {
				return 0;
			}
			return dmg;
		}
	}

	private void isLiveJudge(List<Creature> creatures, List<Creature> servants, List<Creature> creH,
			List<Creature> creD) {
		Iterator<Creature> it = creatures.iterator();
		for (Creature p : creatures) {
			int hp = p.getBtlParam().get(Param.HP);
			if (hp <= 0 && p.getFirstSkill() != FirstSkill.HRS && p.getFirstSkill() != FirstSkill.HTT) {
				p.setBtlParam(Param.HP, 0);
				p.setLive(false);
			} else if (hp <= 0 && p.getFirstSkill() == FirstSkill.HRS && p.getGuts() == 1) {
				System.out.println("勇者の魂が震える！！");
				System.out.println("まだ終われない！！");
				System.out.println("勇者は生き返り、力がみなぎってきた！！");
				p.resetEfDulation();
				p.setBtlParam(Param.HP, p.getDftParam().get(Param.HP));
				p.setBtlParam(Param.ATK, p.getDftParam().get(Param.ATK) * 2);
				p.setBtlParam(Param.DEF, p.getDftParam().get(Param.DEF) / 2);
				p.setBtlParam(Param.MAG, p.getDftParam().get(Param.MAG) * 2);
				p.setBtlParam(Param.SPD, p.getDftParam().get(Param.SPD) * 2);
				p.setBtlParam(Param.LUK, p.getBtlParam().get(Param.LUK) + 10);
				p.setGuts(0);
			} else if (hp <= 0 && p.getFirstSkill() == FirstSkill.HTT && p.getGuts() == 1) {
				System.out.println("不退転の覚悟がその身を現世に止まらせる！！");
				System.out.println(p.getName() + "は致命的な攻撃を耐え切った！！");
				p.setBtlParam(Param.HP, 1);
				p.setBtlParam(Param.DEF, p.getBtlParam().get(Param.DEF) * 10);
				p.setGuts(0);
			} else if (hp <= 0) {
				p.setBtlParam(Param.HP, 0);
				p.setLive(false);
				//				creatures.remove(p);
				it.remove();
				if (p.getJinei() == Jinei.DEMON) {
					creD.remove(p);
				} else if (p.getJinei() == Jinei.HERO) {
					creH.remove(p);
				}
			}
		}
	}

	private void healAction(Creature p, Skill sk) {
		int dfHP = p.getDftParam().get(Param.HP);
		int btHP = p.getBtlParam().get(Param.HP);
		if (sk.getPower() > 5) {
			p.setBtlParam(Param.HP, dfHP);
			System.out.println(p.getName() + "はHPを全回復した！");
		} else {
			if (dfHP > btHP + dfHP * sk.getPower() / 10) {
				p.setBtlParam(Param.HP, btHP + dfHP * sk.getPower() / 10);
			} else {
				p.setBtlParam(Param.HP, dfHP);
			}
		}
	}

	private void bfAction(Creature p, Skill sk) {
		Map<Param, Integer> dfP = p.getDftParam();
		Map<Param, Integer> btP = p.getBtlParam();
		if (sk.getAtrbt() == Attribute.ATKBF) {
			p.setBtlParam(Param.ATK, btP.get(Param.ATK) * sk.getPower());
			p.setBtlParam(Param.MAG, btP.get(Param.MAG) * sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
			System.out.println(p.getName() + "の物理攻撃力・魔法攻撃力は" + sk.getPower() + "倍になった！");
		} else if (sk.getAtrbt() == Attribute.DEFBF) {
			p.setBtlParam(Param.DEF, btP.get(Param.DEF) * sk.getPower());
			p.setEfDulation(sk, sk.getDulation());
		}
	}

	private void dbfAction(Creature p1, Creature p2, Skill sk) {
		Map<Param, Integer> dfP = p2.getDftParam();
		Map<Param, Integer> btP = p2.getBtlParam();
		if (avoidJudge(p1, p2, 50)) {
			System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
		} else {
			if (sk.getAtrbt() == Attribute.ATKDBF) {
				p2.setBtlParam(Param.ATK, btP.get(Param.ATK) / sk.getPower());
				p2.setBtlParam(Param.MAG, btP.get(Param.MAG) / sk.getPower());
				p2.setEfDulation(sk, sk.getDulation());
				System.out.println(p2.getName() + "の物理攻撃力・魔法攻撃力は1/" + sk.getPower() + "倍になった！");
			} else if (sk.getAtrbt() == Attribute.DEFDBF) {
				p2.setBtlParam(Param.DEF, btP.get(Param.DEF) / sk.getPower());
				p2.setEfDulation(sk, sk.getDulation());
				System.out.println(p2.getName() + "の防御力は1/" + sk.getPower() + "倍になった！");
			}
		}
	}

	private void conErrorAction(Creature p1, Creature p2, Skill sk) {
		if (sk.getAtrbt() == Attribute.SLEEP) {
			if (avoidJudge(p1, p2, 20)) {
				System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			} else {
				System.out.println(p2.getName() + "は眠ってしまった！");
				p2.setEfDulation(sk, sk.getDulation());
			}
		} else if (sk.getAtrbt() == Attribute.POISON) {
			if (avoidJudge(p1, p2, 40)) {
				System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			} else {
				System.out.println(p2.getName() + "は毒状態になってしまった！");
				p2.setEfDulation(sk, sk.getDulation());
			}
		} else if (sk.getAtrbt() == Attribute.CONFUSION) {
			if (avoidJudge(p1, p2, 40)) {
				System.out.println(p2.getName() + "は" + sk.getName() + "を避けた！");
			} else {
				System.out.println(p2.getName() + "は混乱状態になってしまった！");
				p2.setEfDulation(sk, sk.getDulation());
			}
		} else if (sk.getAtrbt() == Attribute.DEATH) {
			System.out.println(sk.getName() + "は避けられない...");
			System.out.println(p2.getName() + "は死の宣告を受けた！");
			p2.setEfDulation(sk, sk.getDulation());
		}
	}

	private void conErrorAction(Creature p1, Creature[] p2, Skill sk) {
		//対複数　今後実装予定
		if (sk.getAtrbt() == Attribute.SLEEP) {
			System.out.println("");
		} else if (sk.getAtrbt() == Attribute.POISON) {
			System.out.println();
		} else if (sk.getAtrbt() == Attribute.CONFUSION) {
			System.out.println();
		}
	}

	private void specialAction(Creature p1, Creature p2, Skill skill, List<Creature> servants) {
		if (skill == Skill.PPT) {
			System.out.println("ランダムな効果が発生！");
		} else if (skill == Skill.MDT) {
			int dmg = dmgJudge(p1, p2, p1.getBtlParam().get(Param.MP) * 2, skill);
			System.out.println(p1.getName() + "は全てのMPを使った！");
			System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
			p1.setBtlParam(Param.MP, 0);
			p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		} else if (skill == Skill.MGT) {
			int dmg = dmgJudge(p1, p2, p1.getBtlParam().get(Param.HP) * 3, skill);
			System.out.println(p1.getName() + "は自身の命を捧げた！");
			System.out.println(p2.getName() + "に" + dmg + "ダメージ！！");
			p1.setBtlParam(Param.HP, 0);
			p2.setBtlParam(Param.HP, p2.getBtlParam().get(Param.HP) - dmg);
		} else if (skill == Skill.NKM) {
			Random r = new Random();
			int index = r.nextInt(Creature.ServantName.values().length) + 1;
			Creature servant = Creature.createServant(p1, Creature.ServantName.values()[index], servants);
			servants.add(servant);
		}
	}

	private int standbyFaze(Creature p1) {
		//いらないかも　今後使うかもだから残す
		return 0;
	}

	private boolean removeConErrorJudge(Creature p1, Attribute atr, int du) {
		//trueなら解除、falseなら解除失敗
		Random r = new Random();
		if (du <= 0) {
			int tf = r.nextInt(2);
			if (tf == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private void endFaze(Creature p1, int turn) {
		Map<Skill, Integer> pEfD = p1.getEfDulation();
		Set<Skill> pSkills = pEfD.keySet();
		Iterator<Skill> it = pEfD.keySet().iterator();
		while (it.hasNext()) {
			Skill sk = it.next();
			//		for (Skill sk : pSkills) {
			Attribute atrbt = sk.getAtrbt();
			int du = pEfD.get(sk);
			if (atrbt == Attribute.POISON) {
				int pDmg = p1.getBtlParam().get(Param.HP) * 3 / 100;
				System.out.println(p1.getName() + "は毒によって" + pDmg + "ダメージを受けた！");
				p1.setBtlParam(Param.HP, p1.getBtlParam().get(Param.HP) - pDmg);
				if (du == 0) {
					boolean remC = removeConErrorJudge(p1, atrbt, du);
					if (remC) {
						//解除成功の場合
						//						pEfD.remove(sk);
						it.remove();
						System.out.println(p1.getName() + "は毒の治癒に成功した！");
					} else {
						System.out.println(p1.getName() + "はまだ毒状態だ！");
					}
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "はまだ毒状態だ！");
				}
			} else if (atrbt == Attribute.SLEEP) {
				if (du == 0) {
					boolean remC = removeConErrorJudge(p1, atrbt, du);
					if (remC) {
						//解除成功の場合
						//						pEfD.remove(sk);
						it.remove();
						System.out.println(p1.getName() + "は眠りから醒めた！");
					} else {
						System.out.println(p1.getName() + "はまだ眠っている！");
					}
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "はまだ眠っている！");
				}
			} else if (atrbt == Attribute.CONFUSION) {
				if (du == 0) {
					boolean remC = removeConErrorJudge(p1, atrbt, du);
					if (remC) {
						//解除成功の場合
						//						pEfD.remove(sk);
						it.remove();
						System.out.println(p1.getName() + "は混乱から覚めた！");
					} else {
						System.out.println(p1.getName() + "はまだ混乱している！");
					}
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "はまだ混乱している！");
				}
			} else if (atrbt == Attribute.ATKBF) {
				if (du == 0) {
					it.remove();
					p1.setBtlParam(Param.ATK,
							p1.getBtlParam().get(Param.ATK) - p1.getDftParam().get(Param.ATK) * (sk.getPower() - 1));
					p1.setBtlParam(Param.MAG,
							p1.getBtlParam().get(Param.MAG) - p1.getDftParam().get(Param.MAG) * (sk.getPower() - 1));
					System.out.println(p1.getName() + "は" + sk.getName() + "の効果が消えた！");
				} else {
					pEfD.put(sk, du - 1);
				}
			} else if (atrbt == Attribute.DEFBF) {
				if (du == 0) {
					it.remove();
					p1.setBtlParam(Param.DEF,
							p1.getBtlParam().get(Param.DEF) - p1.getDftParam().get(Param.DEF) * (sk.getPower() - 1));
					System.out.println(p1.getName() + "は" + sk.getName() + "の効果が消えた！");
				} else {
					pEfD.put(sk, du - 1);
				}
			} else if (atrbt == Attribute.ATKDBF) {
				if (du == 0) {
					it.remove();
					p1.setBtlParam(Param.ATK, p1.getBtlParam().get(Param.ATK) * sk.getPower());
					p1.setBtlParam(Param.MAG, p1.getBtlParam().get(Param.MAG) * sk.getPower());
					System.out.println(p1.getName() + "は" + sk.getName() + "の効果が消えた！");
				} else {
					pEfD.put(sk, du - 1);
				}
			} else if (atrbt == Attribute.DEFDBF) {
				if (du == 0) {
					it.remove();
					p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) * sk.getPower());
					System.out.println(p1.getName() + "は" + sk.getName() + "の効果が消えた！");
				} else {
					pEfD.put(sk, du - 1);
				}
			} else if (atrbt == Attribute.DEATH) {
				if (du == 0) {
					it.remove();
					System.out.println("死の宣告が実現する...");
					System.out.println(p1.getName() + "のHPは0になった...");
					p1.setBtlParam(Param.HP, 0);
				} else {
					pEfD.put(sk, du - 1);
					System.out.println(p1.getName() + "の宣告された死まであと " + pEfD.get(sk) + "ターン...");
				}
			}
		}
		if (p1.getFirstSkill() == FirstSkill.BNS) {
			System.out.println("燃え上がる魂がその身を削る！！");
			int dmg = p1.getBtlParam().get(Param.HP) / 10;
			System.out.println(p1.getName() + "に" + dmg + "ダメージ");
			p1.setBtlParam(Param.HP, p1.getBtlParam().get(Param.HP) - dmg);
		} else if (turn == 5 && p1.getFirstSkill() == FirstSkill.MOU) {
			System.out.println("魔王の威圧が弱まった！！");
			p1.setBtlParam(Param.ATK, p1.getBtlParam().get(Param.ATK) / 2);
			p1.setBtlParam(Param.DEF, p1.getBtlParam().get(Param.DEF) / 2);
			p1.setBtlParam(Param.MAG, p1.getBtlParam().get(Param.MAG) / 2);
			p1.setBtlParam(Param.SPD, p1.getBtlParam().get(Param.SPD) / 2);
		}
	}

	private void sleep(int second) {
		//second秒待機するやつ
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
