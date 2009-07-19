/*
 * PlayerCharacterTest.java
 *
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	   See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 09-Jan-2004
 *
 * Current Ver: $Revision$
 *
 * Last Editor: $Author$
 *
 * Last Edited: $Date$
 *
 */
package pcgen.core;

import java.awt.HeadlessException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import pcgen.AbstractCharacterTestCase;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.content.LevelCommandFactory;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.Nature;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.enumeration.Type;
import pcgen.cdom.enumeration.VariableKey;
import pcgen.cdom.helper.StatLock;
import pcgen.cdom.inst.PCClassLevel;
import pcgen.cdom.list.ClassSkillList;
import pcgen.cdom.list.CompanionList;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.cdom.reference.CDOMSimpleSingleRef;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.analysis.SkillRankControl;
import pcgen.core.bonus.Bonus;
import pcgen.core.bonus.BonusObj;
import pcgen.core.character.CharacterSpell;
import pcgen.core.character.SpellBook;
import pcgen.core.spell.Spell;
import pcgen.core.system.LoadInfo;
import pcgen.gui.utils.SwingChooser;
import pcgen.io.exporttoken.StatToken;
import pcgen.rules.context.LoadContext;
import pcgen.util.Logging;
import pcgen.util.TestHelper;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.enumeration.Visibility;

/**
 * The Class <code>PlayerCharacterTest</code> is responsible for testing 
 * that PlayerCharacter is working correctly.
 * 
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author Chris Ward <frugal@purplewombat.co.uk>
 * @version $Revision$
 */
@SuppressWarnings("nls")
public class PlayerCharacterTest extends AbstractCharacterTestCase
{
	Race giantRace = null;
	PCClass giantClass = null;
	PCClass pcClass = null;
	PCClass classWarmind = null;
	PCClass class2LpfM = null;
	PCClass class3LpfM = null;
	PCClass class3LpfBlank = null;
	Race human = null;
	Ability toughness = null;
	AbilityCategory specialFeatCat;
	AbilityCategory specialAbilityCat;
	
	/**
	 * Run the tests.
	 * @param args
	 */
	public static void main(final String[] args)
	{
		TestRunner.run(PlayerCharacterTest.class);
	}

	/**
	 * @return Test
	 */
	public static Test suite()
	{
		return new TestSuite(PlayerCharacterTest.class);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	
		// Giant Class
		giantClass = new PCClass();
		giantClass.setName("Giant");
		giantClass.addToListFor(ListKey.TYPE, Type.getConstant("MONSTER"));
		final BonusObj babClassBonus = Bonus.newBonus("1|COMBAT|BAB|CL*3/4");
		giantClass.addToListFor(ListKey.BONUS, babClassBonus);
		LoadContext context = Globals.getContext();
		context.ref.importObject(giantClass);
	
		// Human
		human = new Race();
		final BonusObj humanRaceFeatBonus = Bonus.newBonus("FEAT|POOL|2");
		human.addToListFor(ListKey.BONUS, humanRaceFeatBonus);

		// Giant Race
		giantRace = new Race();
		giantRace.setName("Ogre");
		giantRace.put(ObjectKey.MONSTER_CLASS, new LevelCommandFactory(
				CDOMDirectSingleRef.getRef(giantClass), FormulaFactory
						.getFormulaFor(4)));
		giantRace.addToListFor(ListKey.HITDICE_ADVANCEMENT, 100);

		final BonusObj giantRaceFeatBonus = Bonus.newBonus("FEAT|POOL|1");
	
		giantRace.addToListFor(ListKey.BONUS, giantRaceFeatBonus);
	
		context.ref.importObject(giantRace);
	
		// Create the monster class type
		SettingsHandler.getGame().addClassType(
			"Monster		CRFORMULA:0			ISMONSTER:YES	XPPENALTY:NO");
	
		pcClass = new PCClass();
		pcClass.setName("MyClass");
		pcClass.put(StringKey.SPELLTYPE, "ARCANE");
		context.ref.importObject(pcClass);
	
		classWarmind = new PCClass();
		classWarmind.setName("Warmind");
		context.ref.importObject(classWarmind);
	
		class2LpfM = new PCClass();
		class2LpfM.setName("2LpfM");
		class2LpfM.addToListFor(ListKey.TYPE, Type.getConstant("MONSTER"));
		class2LpfM.put(IntegerKey.LEVELS_PER_FEAT, 2);
		class2LpfM.put(StringKey.LEVEL_TYPE, "MONSTER");
		context.ref.importObject(class2LpfM);
		
		class3LpfM = new PCClass();
		class3LpfM.setName("3LpfM");
		class3LpfM.addToListFor(ListKey.TYPE, Type.getConstant("MONSTER"));
		class3LpfM.put(IntegerKey.LEVELS_PER_FEAT, 3);
		class3LpfM.put(StringKey.LEVEL_TYPE, "MONSTER");
		context.ref.importObject(class3LpfM);
		
		class3LpfBlank = new PCClass();
		class3LpfBlank.setName("3LpfBlank");
		class3LpfBlank.addToListFor(ListKey.TYPE, Type.getConstant("Foo"));
		class3LpfBlank.put(IntegerKey.LEVELS_PER_FEAT, 3);
		context.ref.importObject(class3LpfBlank);

		toughness = new Ability();
		toughness.setName("Toughness");
		toughness.put(ObjectKey.MULTIPLE_ALLOWED, Boolean.TRUE);
		toughness.put(ObjectKey.STACKS, Boolean.TRUE);
		toughness.put(StringKey.CHOICE_STRING, "NOCHOICE");
		toughness.setCDOMCategory(AbilityCategory.FEAT);
		final BonusObj aBonus = Bonus.newBonus("HP|CURRENTMAX|3");
		
		if (aBonus != null)
		{
			toughness.addToListFor(ListKey.BONUS, aBonus);
		}
		Globals.addAbility(toughness);
	
		Ability exoticWpnProf =
				TestHelper.makeAbility("Exotic Weapon Proficiency", AbilityCategory.FEAT,
					"General.Fighter");
		exoticWpnProf.put(ObjectKey.MULTIPLE_ALLOWED, Boolean.TRUE);
		exoticWpnProf.put(StringKey.CHOICE_STRING, "PROFICIENCY|WEAPON|UNIQUE|TYPE.Exotic");
		context.unconditionallyProcess(exoticWpnProf, "AUTO", "WEAPONPROF|%LIST");
	
		WeaponProf wpnProfTestA = new WeaponProf();
		wpnProfTestA.setName("Weapon A");
		wpnProfTestA.put(StringKey.KEY_NAME, "Weapon A");
		wpnProfTestA.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		context.ref.importObject(wpnProfTestA);
	
		WeaponProf wpnProfTestB = new WeaponProf();
		wpnProfTestB.setName("Weapon B");
		wpnProfTestB.put(StringKey.KEY_NAME, "Weapon B");
		wpnProfTestB.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		context.ref.importObject(wpnProfTestB);
	
		WeaponProf wpnProfTestC = new WeaponProf();
		wpnProfTestC.setName("Weapon C");
		wpnProfTestC.put(StringKey.KEY_NAME, "Weapon C");
		wpnProfTestC.addToListFor(ListKey.TYPE, Type.getConstant("Exotic"));
		context.ref.importObject(wpnProfTestC);
	
		SettingsHandler
			.setSingleChoicePreference(Constants.CHOOSER_SINGLECHOICEMETHOD_SELECTEXIT);
		ChooserFactory.setInterfaceClassname(SwingChooser.class.getName());
	
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(1), "ADD",
				"FEAT|KEY_Exotic Weapon Proficiency (Weapon B)");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(2), "ADD",
				"FEAT|KEY_Exotic Weapon Proficiency (Weapon A)");
		context.unconditionallyProcess(pcClass.getOriginalClassLevel(3), "ADD",
				"FEAT|KEY_Exotic Weapon Proficiency (Weapon C)");
		
		specialFeatCat = new AbilityCategory("Special Feat");
		specialFeatCat.setAbilityCategory(AbilityCategory.FEAT.getKeyName());
		SettingsHandler.getGame().addAbilityCategory(specialFeatCat);
		
		
		specialAbilityCat = new AbilityCategory("Special Ability");
		SettingsHandler.getGame().addAbilityCategory(specialAbilityCat);
	}

	@Override
	protected void tearDown()
	{
		Logging.setDebugMode(false);
		human.removeListFor(ListKey.BONUS);
		giantRace.removeListFor(ListKey.BONUS);
	}
	
	/**
	 * @throws Exception
	 */
	public void testGetBonusFeatsForNewLevel1() throws Exception
	{
		Globals.getContext().resolveReferences();
		final PlayerCharacter character = new PlayerCharacter();

		character.setRace(human);
		character.incrementClassLevel(1, pcClass, true);
		assertEquals(2, (int) character.getRawFeats(true));
	}

	/**
	 * @throws Exception
	 */
	public void testGetBonusFeatsForNewLevel3() throws Exception
	{
		Globals.getContext().resolveReferences();
		final PlayerCharacter character = new PlayerCharacter();

		character.setRace(human);
		character.incrementClassLevel(3, pcClass, true);
		assertEquals(3, (int) character.getRawFeats(true));
	}

	/**
	 * Test bonus monster feats where there default monster mode is off.
	 * Note: As PCClass grants feats which do not exist, the feat pool gets 
	 * incremented instead.
	 * @throws Exception
	 */
	public void testGetMonsterBonusFeatsForNewLevel1() throws Exception
	{
		Globals.getContext().resolveReferences();
		final PlayerCharacter character = new PlayerCharacter();

		character.setRace(giantRace);
		character.incrementClassLevel(1, pcClass, true);
		is((int) character.getRawFeats(true), eq(2),
			"One level of PCClass, PC has one feat for levels of monster class and one for a missing feat.");
		character.incrementClassLevel(1, pcClass, true);
		is((int) character.getRawFeats(true), eq(3),
			"Three levels of PCClass (6 total), feats increment");
	}

	/**
	 * Test level per feat bonus to feats. 
	 */
	public void testGetNumFeatsFromLevels()
	{
		final PlayerCharacter pc = new PlayerCharacter();
		pc.setRace(human);
		assertEquals("Should start at 0", 0, pc.getNumFeatsFromLevels(), 0.001);

		pc.incrementClassLevel(1, class3LpfM, true);
		assertEquals("1/3 truncs to 0", 0, pc.getNumFeatsFromLevels(), 0.001);
		pc.incrementClassLevel(1, class3LpfM, true);
		assertEquals("2/3 truncs to 0", 0, pc.getNumFeatsFromLevels(), 0.001);
		pc.incrementClassLevel(1, class3LpfM, true);
		assertEquals("3/3 truncs to 1", 1, pc.getNumFeatsFromLevels(), 0.001);
		pc.incrementClassLevel(1, class3LpfM, true);
		assertEquals("4/3 truncs to 1", 1, pc.getNumFeatsFromLevels(), 0.001);
		pc.incrementClassLevel(1, class2LpfM, true);
		assertEquals("4/3 + 1/2 truncs to 1", 1, pc.getNumFeatsFromLevels(),
			0.001);
		pc.incrementClassLevel(1, class3LpfBlank, true);
		assertEquals("4/3 + 1/2 truncs to 1 + 1/3 truncs to 0", 1, pc
			.getNumFeatsFromLevels(), 0.001);
		pc.incrementClassLevel(1, class2LpfM, true);
		assertEquals("5/3 + 2/2 truncs to 2 + 1/3 truncs to 0", 2, pc
			.getNumFeatsFromLevels(), 0.001);
	}

	/**
	 * Test stacking rules for a mixture of normal progression and 
	 * levelsperfeat progression. Stacking should only occur within like 
	 * leveltypes or within standard progression
	 * @throws Exception
	 */
	public void testGetMonsterBonusFeatsForNewLevel2() throws Exception
	{
		final PlayerCharacter pc = new PlayerCharacter();

		pc.setRace(giantRace);
		is((int) pc.getRawFeats(true), eq(2),
			"Four levels from race (4/3), PC has one racial feat.");
		
		pc.incrementClassLevel(1, class3LpfM, true);
		is((int) pc.getRawFeats(true), eq(2),
			"One level of 3LpfM (1/3), four levels from race(4/3), PC has one racial feat.");
		pc.incrementClassLevel(1, class3LpfM, true);
		is((int) pc.getRawFeats(true), eq(2),
			"Two level of 3LpfM (2/3), four levels from race(4/3), PC has one racial feat.");
		pc.incrementClassLevel(1, class3LpfM, true);
		is((int) pc.getRawFeats(true), eq(3),
			"Three level of 3LpfM (3/3), four levels from race(4/3), PC has one racial feat.");
	}
	
	/**
	 * Tests getVariableValue
	 * @throws Exception
	 */
	public void testGetVariableValue1() throws Exception
	{
		//Logging.setDebugMode(true);
		Logging.debugPrint("\n\n\ntestGetVariableValue1()");
		giantRace.put(VariableKey.getConstant("GiantVar1"), FormulaFactory.ZERO);
		final BonusObj raceBonus = Bonus.newBonus("1|VAR|GiantVar1|7+HD");
		giantClass.addToListFor(ListKey.BONUS, raceBonus);

		giantClass.getOriginalClassLevel(1).put(VariableKey.getConstant("GiantClass1"),
				FormulaFactory.ZERO);
		final BonusObj babClassBonus =
				Bonus.newBonus("1|VAR|GiantClass1|CL=Giant");
		giantClass.addToListFor(ListKey.BONUS, babClassBonus);

		final PlayerCharacter character = new PlayerCharacter();
		// NOTE: This will add 4 levels of giantClass to the character 
		character.setRace(giantRace);
		character.incrementClassLevel(4, giantClass, true);

		assertEquals(new Float(15.0), character.getVariableValue("GiantVar1",
			"CLASS:Giant"));
		assertEquals(new Float(8.0), character.getVariableValue("GiantClass1",
			"CLASS:Giant"));

	}

	/**
	 * Tests getVariableValue for stat modifier
	 * @throws Exception
	 */
	public void testGetVariableValueStatMod() throws Exception
	{
		Globals.getContext().resolveReferences();
		//Logging.setDebugMode(true);
		Logging.debugPrint("\n\n\ntestGetVariableValueStatMod()");
		final PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);
		final PCStat stat = character.getUnmodifiableStatList().get(0);
		character.setAssoc(stat, AssociationKey.STAT_SCORE, 16);
		character.incrementClassLevel(2, pcClass, true);

		final Float result =
				character.getVariableValue("(SCORE/2).TRUNC-5", "STAT:STR");
		assertEquals("Stat modifier not correct", 3.0, result.doubleValue(),
			0.1);
	}

	/**
	 * @throws Exception
	 */
	public void testGetVariableValueStatModNew() throws Exception
	{
		Globals.getContext().resolveReferences();
		//Logging.setDebugMode(true);
		Logging.debugPrint("\n\n\ntestGetVariableValueStatModNew()");
		final PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);
		final PCStat stat = character.getUnmodifiableStatList().get(0);
		character.setAssoc(stat, AssociationKey.STAT_SCORE, 16);
		character.incrementClassLevel(2, pcClass, true);

		final Float result =
				character.getVariableValue("floor(SCORE/2)-5", "STAT:STR");
		assertEquals("Stat modifier not correct", 3.0, result.doubleValue(),
			0.1);
	}

	/**
	 * Test out the caching of variable values.
	 */
	public void testGetVariableCaching()
	{
		Globals.getContext().resolveReferences();
		final PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);
		final PCStat stat = character.getUnmodifiableStatList().get(0);
		character.setAssoc(stat, AssociationKey.STAT_SCORE, 16);
		character.incrementClassLevel(2, pcClass, true);

		int iVal = character.getVariableValue("roll(\"3d6\")+5", "").intValue();
		boolean match = true;
		for (int i = 0; i < 10; i++)
		{
			match =
					(iVal == character.getVariableValue("roll(\"3d6\")+5", "")
						.intValue());
			if (!match)
			{
				break;
			}
		}

		assertFalse("Roll function should not be cached.", match);
	}

	/**
	 * Test the processing of modFeat. Checks that when in select single and
	 * close mode, only one instance of a feat with a sub-choice is added.
	 */
	public void testModFeat()
	{
		Globals.getContext().resolveReferences();
		final PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);
		character.incrementClassLevel(1, pcClass, true);

		SettingsHandler
			.setSingleChoicePreference(Constants.CHOOSER_SINGLECHOICEMETHOD_SELECTEXIT);
		ChooserFactory.setInterfaceClassname(SwingChooser.class.getName());

		is((int) character.getRawFeats(true), eq(2), "Start with 2 feats");
		try
		{
			AbilityUtilities.modFeat(character, null, "Toughness", true, false);
			is((int) character.getRawFeats(true), eq(1), "Only 1 feat used");
		}
		catch (HeadlessException e)
		{
			Logging.debugPrint("Ignoring Headless exception.");
		}
	}

	/**
	 * Test that multiple exotic weapon proficiencies work correctly.
	 */
	public void testExoticWpnProf()
	{
		Globals.getContext().resolveReferences();
		PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);

		assertFalse("Not yet proficient in Weapon A", TestHelper.hasWeaponProfKeyed(character, "Weapon A"));
		assertFalse("Not yet proficient in Weapon B", TestHelper.hasWeaponProfKeyed(character, "Weapon B"));
		assertFalse("Not yet proficient in Weapon C", TestHelper.hasWeaponProfKeyed(character, "Weapon C"));

		character.incrementClassLevel(1, pcClass, true);

		assertFalse("First Proficient in Weapon A", TestHelper.hasWeaponProfKeyed(character, "Weapon A"));
		assertTrue("Not yet proficient in Weapon B", TestHelper.hasWeaponProfKeyed(character, "Weapon B"));
		assertFalse("Not yet proficient in Weapon C", TestHelper.hasWeaponProfKeyed(character, "Weapon C"));

		character.incrementClassLevel(1, pcClass, true);

		assertTrue("Second Proficient in Weapon A", TestHelper.hasWeaponProfKeyed(character, "Weapon A"));
		assertTrue("Proficient in Weapon B", TestHelper.hasWeaponProfKeyed(character, "Weapon B"));
		assertFalse("Not yet proficient in Weapon C", TestHelper.hasWeaponProfKeyed(character, "Weapon C"));

		character.incrementClassLevel(1, pcClass, true);

		assertTrue("Third Proficient in Weapon A", TestHelper.hasWeaponProfKeyed(character, "Weapon A"));
		assertTrue("Proficient in Weapon B", TestHelper.hasWeaponProfKeyed(character, "Weapon B"));
		assertTrue("Proficient in Weapon C", TestHelper.hasWeaponProfKeyed(character, "Weapon C"));
	}

	/**
	 * Tests CL variable
	 * @throws Exception
	 */
	public void testGetClassVar() throws Exception
	{
		//Logging.setDebugMode(true);
		Logging.debugPrint("\n\n\ntestGetClassVar()");
		final PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);
		character.incrementClassLevel(2, classWarmind, true);

		final Float result =
				character.getVariableValue("var(\"CL=Warmind\")", "");
		assertEquals("CL count not correct", 2.0, result.doubleValue(), 0.1);
	}

	/**
	 * Test the processing of the MAX function with respect to character stats.
	 */
	public void testMaxValue()
	{
		PlayerCharacter pc = getCharacter();
		setPCStat(pc, str, 8);
		setPCStat(pc, dex, 14);
		pc.setUseTempMods(true);

		assertEquals("STR", -1.0, pc.getVariableValue("STR", "").floatValue(),
			0.1);
		assertEquals("DEX", 2.0, pc.getVariableValue("DEX", "").floatValue(),
			0.1);
		assertEquals("max(STR,DEX)", 2.0, pc.getVariableValue("max(STR,DEX)",
			"").floatValue(), 0.1);

		StatToken statTok = new StatToken();
		assertEquals("Total stat.", "14", statTok.getToken("STAT.1", pc, null));
		assertEquals("Temp stat.", "14", statTok.getToken("STAT.1.NOEQUIP", pc,
			null));
		assertEquals("Equip stat.", "14", statTok.getToken("STAT.1.NOTEMP", pc,
			null));
		assertEquals("No equip/temp stat.", "14", statTok.getToken(
			"STAT.1.NOEQUIP.NOTEMP", pc, null));
		assertEquals("Base stat.", "14", statTok.getToken(
			"STAT.1.NOEQUIP.NOTEMP", pc, null));

		final BonusObj raceBonus = Bonus.newBonus("1|STAT|DEX|-2");
		giantClass.addToListFor(ListKey.BONUS, raceBonus);
		pc.setRace(giantRace);
		pc.incrementClassLevel(4, giantClass, true);

		assertEquals("Total stat.", "12", statTok.getToken("STAT.1", pc, null));
		assertEquals("Temp stat.", "12", statTok.getToken("STAT.1.NOEQUIP", pc,
			null));
		assertEquals("Base stat.", "12", statTok.getToken(
			"STAT.1.NOEQUIP.NOTEMP", pc, null));
		assertEquals("DEX", 1.0, pc.getVariableValue("DEX", "").floatValue(),
			0.1);
		assertEquals("max(STR,DEX)", 1.0, pc.getVariableValue("max(STR,DEX)",
			"").floatValue(), 0.1);

		Spell spell2 = new Spell();
		spell2.setName("Concrete Boots");
		final BonusObj aBonus = Bonus.newBonus("STAT|DEX|-2");
		
		if (aBonus != null)
		{
			spell2.addToListFor(ListKey.BONUS, aBonus);
		}
		BonusObj penalty = spell2.getRawBonusList(pc).get(0);
		pc.addTempBonus(penalty, spell2);
		penalty.setTargetObject(pc);
		pc.calcActiveBonuses();

		assertEquals("Total stat.", "10", statTok.getToken("STAT.1", pc, null));
		assertEquals("Temp stat.", "10", statTok.getToken("STAT.1.NOEQUIP", pc,
			null));
		assertEquals("Base stat.", "12", statTok.getToken(
			"STAT.1.NOEQUIP.NOTEMP", pc, null));
		assertEquals("DEX", 0.0, pc.getVariableValue("DEX", "").floatValue(),
			0.1);
		assertEquals("max(STR,DEX)-STR", 1.0, pc.getVariableValue(
			"max(STR,DEX)-STR", "").floatValue(), 0.1);
	}

	/**
	 * Test the skills visibility functionality. We want to ensure that
	 * each call retrieves the right set of skills.
	 */
	public void testSkillsVisibility()
	{
		PlayerCharacter pc = getCharacter();

		Skill guiSkill = new Skill();
		Skill outputSkill = new Skill();
		Skill defaultSkill = new Skill();
		ClassSkillList csl = new ClassSkillList();
		csl.put(StringKey.NAME, "MyClass");

		guiSkill.addToListFor(ListKey.CLASSES, CDOMDirectSingleRef.getRef(csl));
		guiSkill.setName("GUI");
		guiSkill.addToListFor(ListKey.TYPE, Type.getConstant("INT"));
		guiSkill.put(ObjectKey.VISIBILITY, Visibility.DISPLAY_ONLY);
		SkillRankControl.modRanks(1.0, pcClass, true, pc, guiSkill);
		pc.addSkill(guiSkill);

		outputSkill.addToListFor(ListKey.CLASSES, CDOMDirectSingleRef.getRef(csl));
		outputSkill.setName("Output");
		outputSkill.addToListFor(ListKey.TYPE, Type.getConstant("INT"));
		outputSkill.put(ObjectKey.VISIBILITY, Visibility.OUTPUT_ONLY);
		SkillRankControl.modRanks(1.0, pcClass, true, pc, outputSkill);
		pc.addSkill(outputSkill);

		defaultSkill.addToListFor(ListKey.CLASSES, CDOMDirectSingleRef.getRef(csl));
		defaultSkill.setName("Default");
		defaultSkill.addToListFor(ListKey.TYPE, Type.getConstant("INT"));
		defaultSkill.put(ObjectKey.VISIBILITY, Visibility.DEFAULT);
		SkillRankControl.modRanks(1.0, pcClass, true, pc, defaultSkill);
		pc.addSkill(defaultSkill);

		// Test retrieved list
		List<Skill> skillList = pc.getSkillList();
		assertEquals("Full skill list should have all 3 skills", 3, skillList
			.size());

		skillList = pc.getPartialSkillList(Visibility.DISPLAY_ONLY);
		assertEquals("GUI skill list should have 2 skills", 2, skillList.size());

		skillList = pc.getPartialSkillList(Visibility.OUTPUT_ONLY);
		assertEquals("Output skill list should have 2 skills", 2, skillList
			.size());

		skillList = pc.getPartialSkillList(Visibility.DEFAULT);
		assertEquals("Full skill list should have 3 skills", 3, skillList
			.size());

	}

	/**
	 * Tests adding a spell.
	 */
	public void testAddSpells()
	{
		Globals.getContext().resolveReferences();
		final PlayerCharacter character = new PlayerCharacter();
		character.setRace(human);
		character.incrementClassLevel(1, pcClass, true);

		final List<Ability> none = Collections.emptyList();
		String response =
				character
					.addSpell(null, none, pcClass.getKeyName(), null, 1, 1);
		assertEquals("Add spell should be rejected due to no spell",
			"Invalid parameter to add spell", response);

		Spell spell = new Spell();
		spell.setName("test spell 1");
		CharacterSpell charSpell = new CharacterSpell(pcClass, spell);
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(), null,
					1, 1);
		assertEquals("Add spell should be rejected due to no book",
			"Invalid spell list/book name.", response);
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(), "",
					1, 1);
		assertEquals("Add spell should be rejected due to no book",
			"Invalid spell list/book name.", response);

		// Add a non existant spell to a non existent spellbook
		String spellBookName = "Test book";
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals("Add spell should be rejected due to book not existing",
			"Could not find spell list/book Test book", response);

		character.addSpellBook(spellBookName);
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals(
			"Add spell should be rejected due to no levels.",
			"You can only prepare 0 spells for level 1\nand there are no higher-level slots available.",
			response);

		response =
				character.addSpell(charSpell, none, "noclass", spellBookName,
					1, 1);
		assertEquals("Add spell should be rejected due to no matching class",
			"No class keyed noclass", response);

		SpellBook book = character.getSpellBookByName(spellBookName);
		book.setType(SpellBook.TYPE_PREPARED_LIST);
		character.addSpellBook(spellBookName);
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals(
			"Add spell should be rejected due to no levels.",
			"You can only prepare 0 spells for level 1\nand there are no higher-level slots available.",
			response);

		book.setType(SpellBook.TYPE_SPELL_BOOK);
		book.setPageFormula(FormulaFactory.getFormulaFor("SPELLLEVEL"));
		book.setNumPages(3);
		character.addSpellBook(spellBookName);
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals("Add spell should not be rejected.", "", response);
		// Add a second time to cover multiples
		response =
				character.addSpell(charSpell, none, pcClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals("Add spell should not be rejected.", "", response);
		response =
				character.addSpell(charSpell, none, giantClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals("Add spell should not be rejected.", "", response);
		response =
				character.addSpell(charSpell, none, giantClass.getKeyName(),
					spellBookName, 1, 1);
		assertEquals(
			"Add spell should be rejected due to the book being full.",
			"There are not enough pages left to add this spell to the spell book.",
			response);

		PCClass c = character.getClassKeyed(pcClass.getKeyName());
		List<CharacterSpell> aList =
				character.getCharacterSpells(c, null, spellBookName, 1);
		CharacterSpell addedSpell = aList.get(0);
		response =
				character.delSpell(addedSpell.getSpellInfoFor(character, spellBookName, 1,
					-1, none), pcClass, spellBookName);
		assertEquals("Delete spell should not be rejected.", "", response);

		aList =
				character.getCharacterSpells(giantClass, null, spellBookName, 1);
		addedSpell = aList.get(0);
		response =
				character.delSpell(addedSpell.getSpellInfoFor(character, spellBookName, 1,
					-1), giantClass, spellBookName);
		assertEquals("Delete spell should not be rejected.", "", response);
	}

	/**
	 * Tests getting a map of sets of abilities
	 */
	public void testGetAbilitiesSet()
	{
		verbose = true;
		Logging.errorPrint("--- Start Get Abilities Set Test ---");
	
		PCClass arClass = null;

		
		// do so setup that is specific to testing this method
		arClass = new PCClass();
		arClass.setName("AbilityRichClass");
		arClass.put(StringKey.SPELLTYPE, "ARCANE");
	
		LoadContext context = Globals.getContext();
		context.ref.importObject(arClass);
	
		TestHelper.makeAbilityFromString(
			"TestARc01\tCATEGORY:FEAT\tMULT:YES\tSTACK:YES\tVISIBLE:YES\tCHOOSE:NOCHOICE");
		TestHelper.makeAbilityFromString(
			"TestARc02\tCATEGORY:FEAT\tMULT:YES\tSTACK:YES\tVISIBLE:YES\tCHOOSE:NOCHOICE");
		TestHelper.makeAbilityFromString(
			"TestARc03\tCATEGORY:FEAT\tMULT:YES\tSTACK:YES\tVISIBLE:YES\tCHOOSE:NOCHOICE");
		TestHelper.makeAbilityFromString(
			"TestARc04\tCATEGORY:FEAT\tMULT:YES\tSTACK:YES\tVISIBLE:YES\tCHOOSE:NOCHOICE");
		TestHelper.makeAbilityFromString(
			"TestARc05\tCATEGORY:FEAT\tMULT:YES\tSTACK:YES\tVISIBLE:YES\tCHOOSE:NOCHOICE");
	
		PCClassLevel lvl1 = arClass.getOriginalClassLevel(1);
		context.unconditionallyProcess(lvl1, "ABILITY", "FEAT|NORMAL|TestARc01");
		context.unconditionallyProcess(lvl1, "ABILITY", "FEAT|AUTOMATIC|TestARc02");
		context.unconditionallyProcess(arClass.getOriginalClassLevel(2), "ABILITY", "FEAT|VIRTUAL|TestARc03");
		PCClassLevel lvl3 = arClass.getOriginalClassLevel(3);
		context.unconditionallyProcess(lvl3, "ABILITY", "FEAT|AUTOMATIC|TestARc04");
		context.unconditionallyProcess(lvl3, "ABILITY", "FEAT|AUTOMATIC|TestARc05");
		context.resolveReferences();
		
		final PlayerCharacter pc = new PlayerCharacter();
	
		HashMap<Nature, Set<Ability>> map;
	
		pc.setRace(human);
	
		pc.incrementClassLevel(1, arClass, true);

		map = pc.getAbilitiesSet();

		assertEquals(map.get(Nature.NORMAL).size(),    1);//"First Level human with class AbilityRichClass has 1 normal feat");
		assertEquals(map.get(Nature.AUTOMATIC).size(), 1);// "First Level human with class AbilityRichClass has 1 automatic feat");
		assertEquals(map.get(Nature.VIRTUAL).size(),   0);// "First Level human with class AbilityRichClass has 0 virtual feats");
	
		pc.incrementClassLevel(1, arClass, true);
		
		map = pc.getAbilitiesSet();
		
		assertEquals(map.get(Nature.NORMAL).size(),    1);//, "Second Level human with class AbilityRichClass has 1 normal feat");
		assertEquals(map.get(Nature.AUTOMATIC).size(), 1);//, "Second Level human with class AbilityRichClass has 1 automatic feat");
		assertEquals(map.get(Nature.VIRTUAL).size(),   1);//, "Second Level human with class AbilityRichClass has 1 virtual feat");
	
		pc.incrementClassLevel(1, arClass, true);
		
		map = pc.getAbilitiesSet();
		
		assertEquals(map.get(Nature.NORMAL).size(),    1);//, "Third Level human with class AbilityRichClass has 1 normal feat");
		assertEquals(map.get(Nature.AUTOMATIC).size(), 3);//, "Third Level human with class AbilityRichClass has 3 automatic feats");
		assertEquals(map.get(Nature.VIRTUAL).size(),   1);//, "Third Level human with class AbilityRichClass has 1 virtual feat");
		
		GameMode gm = SettingsHandler.getGame();
		AbilityCategory ac = gm.getAbilityCategory("FEAT");
		Logging.errorPrint("Real abilities: " + pc.getRealAbilitiesList(ac).size());
	
	}

	public void testIsNonAbility()
	{
		PlayerCharacter pc = getCharacter();

		//Base
		assertEquals("Initially character should not have a locked ability", false, pc.isNonAbility(str));

		// With template lock
		PCTemplate nonAbilityLocker = new PCTemplate();
		nonAbilityLocker.setName("locker");
		nonAbilityLocker.addToListFor(ListKey.STAT_LOCKS, new StatLock(str, FormulaFactory.getFormulaFor(10)));
		pc.addTemplate(nonAbilityLocker);
		assertEquals("STR now locked to non ability", true, pc.isNonAbility(str));
		pc.removeTemplate(nonAbilityLocker);
		assertEquals("STR no longer locked to non ability", false, pc.isNonAbility(str));
		
		// With race lock
		Race nonAbilityLockerRace = new Race();
		nonAbilityLockerRace.setName("locker");
		nonAbilityLockerRace.addToListFor(ListKey.STAT_LOCKS, new StatLock(str, FormulaFactory.getFormulaFor(10)));
		pc.setRace(nonAbilityLockerRace);
		assertEquals("STR now locked to non ability", true, pc.isNonAbility(str));
		
		// With template unlock
		nonAbilityLocker.addToListFor(ListKey.UNLOCKED_STATS, str);
		pc.addTemplate(nonAbilityLocker);
		assertEquals("STR now unlocked from a non ability by template", false, pc.isNonAbility(str));
		pc.removeTemplate(nonAbilityLocker);
		assertEquals("STR no longer locked to non ability", true, pc.isNonAbility(str));
		
		// With race unlock
		nonAbilityLockerRace.addToListFor(ListKey.UNLOCKED_STATS, str);
		pc.setRace(nonAbilityLockerRace);
		assertEquals("STR now unlocked from a non ability by race", false, pc.isNonAbility(str));
	}
	
	/**
	 * Test the stacking of the same ability added via different abiltiy 
	 * categories.
	 */
	public void testStackDifferentAbiltyCat()
	{
		PlayerCharacter pc = getCharacter();
		double base = pc.getTotalBonusTo("HP", "CURRENTMAX");
		
		assertEquals("Check repeatability of bonus", base, pc.getTotalBonusTo(
			"HP", "CURRENTMAX"));
		
		try
		{
			AbilityUtilities.modFeat(pc, null, "Toughness", true, false);
			//pc.calcActiveBonuses();
			assertEquals("Check application of single bonus", base+3, pc.getTotalBonusTo(
				"HP", "CURRENTMAX"));
			AbilityUtilities.modFeat(pc, null, "Toughness", true, false);
			pc.calcActiveBonuses();
			assertEquals("Check application of second bonus", base+6, pc.getTotalBonusTo(
				"HP", "CURRENTMAX"));

			AbilityUtilities.modAbility(pc, null, toughness, "Toughness", true,
				specialFeatCat);
			pc.calcActiveBonuses();
			assertEquals(
				"Check application of third bonus in different catgeory",
				base + 9, pc.getTotalBonusTo("HP", "CURRENTMAX"));
		}
		catch (HeadlessException e)
		{
			Logging.debugPrint("Ignoring Headless exception.");
		}
	}
	
	/**
	 * Verify that bested abilities are processed correctly.
	 */
	public void testNestedAbilities()
	{
		Ability resToAcid =
				TestHelper.makeAbility("Resistance To Acid", specialAbilityCat, "Foo");
		resToAcid.setAbilityNature(Nature.AUTOMATIC);
		Ability resToAcidOutputVirt =
			TestHelper.makeAbility("Resistance To Acid Output Virt",
				specialAbilityCat, "Foo");
		resToAcidOutputVirt.setAbilityNature(Nature.VIRTUAL);
		Ability resToAcidOutputAuto =
			TestHelper.makeAbility("Resistance To Acid Output Auto",
				specialAbilityCat, "Foo");
		resToAcidOutputAuto.setAbilityNature(Nature.AUTOMATIC);
		PlayerCharacter pc = getCharacter();
		pc.setRace(human);
		human = pc.getRace();
		assertEquals("PC should now have a race of human", human, pc.getRace());

		LoadContext context = Globals.getContext();
		context.unconditionallyProcess(human, "ABILITY", specialAbilityCat
				.getKeyName()
				+ "|AUTOMATIC|" + resToAcid.getKeyName());
//		context.resolveReferences();
//		pc.setDirty(true);
//		pc.calcActiveBonuses();
//		assertNotNull("Character should have the first feat", pc.getAbilityMatching(resToAcid));
//		assertNull("Character should not have the second feat", pc.getAbilityMatching(resToAcidOutputVirt));
//		assertNull("Character should not have the third feat", pc.getAbilityMatching(resToAcidOutputAuto));

		context.unconditionallyProcess(resToAcid, "ABILITY", specialAbilityCat
				.getKeyName()
				+ "|VIRTUAL|" + resToAcidOutputVirt.getKeyName());
//		context.resolveReferences();
//		pc.setDirty(true);
//		assertNotNull("Character should have the first feat", pc.getAbilityMatching(resToAcid));
//		assertNotNull("Character should have the second feat", pc.getAbilityMatching(resToAcidOutputVirt));
//		assertNull("Character should not have the third feat", pc.getAbilityMatching(resToAcidOutputAuto));

		context.unconditionallyProcess(resToAcid, "ABILITY", specialAbilityCat
				.getKeyName()
				+ "|AUTOMATIC|" + resToAcidOutputAuto.getKeyName());
		context.resolveReferences();
		pc.setDirty(true);
		assertNotNull("Character should have the first feat", pc.getAbilityMatching(resToAcid));
		assertNotNull("Character should have the second feat", pc.getAbilityMatching(resToAcidOutputVirt));
		assertNotNull("Character should have the third feat", pc.getAbilityMatching(resToAcidOutputAuto));
		
	}
	
	public void testGetPartialStatBonusFor()
	{
		PlayerCharacter pc = getCharacter();
		setPCStat(pc, str, 14);

		Ability strBonusAbility =
				TestHelper.makeAbility("Strength power up", AbilityCategory.FEAT,
					"General.Fighter");
		final BonusObj strBonus = Bonus.newBonus("STAT|STR|2");
		strBonusAbility.addToListFor(ListKey.BONUS, strBonus);

		assertEquals("Before bonus, no temp no equip", 0, pc.getPartialStatBonusFor(str, false, false));
		assertEquals("Before bonus, temp no equip", 0, pc.getPartialStatBonusFor(str, true, false));

		AbilityUtilities.modAbility(pc, null, strBonusAbility, "Strength power up", true, AbilityCategory.FEAT);
		pc.calcActiveBonuses();

		assertEquals("After bonus, no temp no equip", 2, pc.getPartialStatBonusFor(str, false, false));
		assertEquals("After bonus, temp no equip", 2, pc.getPartialStatBonusFor(str, true, false));
		
//		final BonusObj strBonusViaList = Bonus.newBonus("STAT|%LIST|3");
//		strBonusAbility.addBonusList(strBonusViaList);
//		strBonusAbility.addAssociated("STR");
//		strBonusAbility.put(ObjectKey.MULTIPLE_ALLOWED, Boolean.TRUE);
//		pc.calcActiveBonuses();
//
//		assertEquals("After list bonus, no temp no equip", 3, pc.getPartialStatBonusFor("STR", false, false));
//		assertEquals("After list bonus, temp no equip", 3, pc.getPartialStatBonusFor("STR", true, false));
		
	}
	
	/**
	 * Validate the getAvailableFollowers function.
	 */
	public void testGetAvailableFollowers()
	{
		Ability ab = TestHelper.makeAbility("Tester", AbilityCategory.FEAT, "Container");
		PlayerCharacter pc = getCharacter();
		
		pc.addAbility(AbilityCategory.FEAT, ab, null);
		
		List<FollowerOption> fo = pc.getAvailableFollowers("Familiar");
		assertTrue("Initially familiar list should be empty", fo.isEmpty());
		fo = pc.getAvailableFollowers("MOUNT");
		assertTrue("Initially mount list should be empty", fo.isEmpty());
		
		CDOMSingleRef<CompanionList> ref = new CDOMSimpleSingleRef<CompanionList>(
				CompanionList.class, "Mount");
		CDOMReference<Race> race  = new  CDOMDirectSingleRef<Race>(giantRace);
		FollowerOption option = new FollowerOption(race, ref);
		ab.addToListFor(ListKey.COMPANIONLIST, option);
		fo = pc.getAvailableFollowers("Familiar");
		assertTrue("Familiar list should still be empty", fo.isEmpty());
		fo = pc.getAvailableFollowers("MOUNT");
		assertFalse("Mount list should not be empty anymore", fo.isEmpty());
		assertEquals("Mount should be the giant race", giantRace.getKeyName(), fo.get(0).getRace().getKeyName());
		assertEquals("Mount list should only have one entry", 1, fo.size());
		
		ref = new CDOMSimpleSingleRef<CompanionList>(
				CompanionList.class, "Familiar");
		race  = new  CDOMDirectSingleRef<Race>(human);
		option = new FollowerOption(race, ref);
		ab.addToListFor(ListKey.COMPANIONLIST, option);
		fo = pc.getAvailableFollowers("Familiar");
		assertFalse("Familiar list should not be empty anymore", fo.isEmpty());
		assertEquals("Familiar should be the human race", human.getKeyName(), fo.get(0).getRace().getKeyName());
		assertEquals("Familiar list should only have one entry", 1, fo.size());
		fo = pc.getAvailableFollowers("MOUNT");
		assertFalse("Mount list should not be empty anymore", fo.isEmpty());
		assertEquals("Mount should be the giant race", giantRace.getKeyName(), fo.get(0).getRace().getKeyName());
		assertEquals("Mount list should only have one entry", 1, fo.size());
	}
	
	public void testGetAggregateAbilityList()
	{
		Ability resToAcid =
				TestHelper.makeAbility("Swelter",
					AbilityCategory.FEAT.getAbilityCategory(), "Foo");
		PCTemplate template = TestHelper.makeTemplate("TemplateVirt"); 
		PCTemplate templateNorm = TestHelper.makeTemplate("TemplateNorm"); 
		LoadContext context = Globals.getContext();
		context.ref.importObject(resToAcid);
		context.unconditionallyProcess(human, "ABILITY", "FEAT|AUTOMATIC|KEY_Swelter");
		context.unconditionallyProcess(template, "ABILITY", "FEAT|VIRTUAL|KEY_Swelter");
		context.unconditionallyProcess(templateNorm, "ABILITY", "FEAT|NORMAL|KEY_Swelter");
		context.resolveReferences();
		PlayerCharacter pc = getCharacter();
		
		List<Ability> abList = pc.getAggregateAbilityList(AbilityCategory.FEAT);
		assertEquals(0, abList.size());

		pc.setRace(human);
		abList = pc.getAggregateAbilityListNoDuplicates(AbilityCategory.FEAT);
		assertEquals(1, abList.size());
		assertEquals(Nature.AUTOMATIC, abList.get(0).getAbilityNature());
		
		pc.addTemplate(template);
		abList = pc.getAggregateAbilityListNoDuplicates(AbilityCategory.FEAT);
		assertEquals(1, abList.size());
		assertEquals(Nature.VIRTUAL, abList.get(0).getAbilityNature());
		
		pc.addTemplate(templateNorm);
		abList = pc.getAggregateAbilityListNoDuplicates(AbilityCategory.FEAT);
		assertEquals(1, abList.size());
		assertEquals(Nature.NORMAL, abList.get(0).getAbilityNature());
	}

	/**
	 * Test the processing and order of operations of the adjustMoveRates method.
	 */
	public void testAdjustMoveRates()
	{
		Ability quickFlySlowSwim =
				TestHelper.makeAbility("quickFlySlowSwim", AbilityCategory.FEAT
					.getAbilityCategory(), "Foo");
		PCTemplate template = TestHelper.makeTemplate("slowFlyQuickSwim");
		//template.addm
		LoadContext context = Globals.getContext();
		context.ref.importObject(quickFlySlowSwim);
		context.unconditionallyProcess(human, "MOVE", "Walk,30");
		context.unconditionallyProcess(quickFlySlowSwim, "MOVE",
			"Swim,10,Fly,30");
		context.unconditionallyProcess(template, "MOVE", "Swim,30,Fly,10");
		context.resolveReferences();
		LoadInfo li =
				SystemCollections.getLoadInfo(SettingsHandler.getGame()
					.getName());
		li.addLoadScoreValue(0, new Float(100.0));
		li.addLoadScoreValue(10, new Float(100.0));
		li.addLoadMultiplier("LIGHT", new Float(100), "100", 0);

		PlayerCharacter pc = getCharacter();
		setPCStat(pc, str, 10);
		pc.setRace(human);
		pc.calcActiveBonuses();
		pc.adjustMoveRates();
		assertEquals(0.0, pc.movementOfType("Swim"), 0.1);
		assertEquals(0.0, pc.movementOfType("Fly"), 0.1);

		pc.addAbility(AbilityCategory.FEAT, quickFlySlowSwim, null);
		pc.calcActiveBonuses();
		pc.adjustMoveRates();
		assertEquals(10.0, pc.movementOfType("Swim"), 0.1);
		assertEquals(30.0, pc.movementOfType("Fly"), 0.1);

		pc.addTemplate(template);
		pc.adjustMoveRates();
		assertEquals(30.0, pc.movementOfType("Swim"), 0.1);
		assertEquals(30.0, pc.movementOfType("Fly"), 0.1);
	}
}
