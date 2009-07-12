/*
 * Created on 23-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package pcgen;

import gmgen.pluginmgr.PluginLoader;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.enumeration.VariableKey;
import pcgen.core.GameMode;
import pcgen.core.Globals;
import pcgen.core.PCAlignment;
import pcgen.core.PCStat;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.rules.context.ReferenceContext;
import pcgen.util.TestHelper;

/**
 * This is an abstract TestClass designed to be able to create a PlayerCharacter
 * Object.
 *
 * @author frugal@purplewombat.co.uk
 */
@SuppressWarnings("nls")
abstract public class AbstractCharacterTestCase extends PCGenTestCase
{
	private PlayerCharacter character = null;
	protected PCStat str;
	protected PCStat cha;
	protected PCStat dex;
	protected PCStat wis;
	protected PCStat intel;
	protected PCAlignment lg;
	protected PCAlignment ln;
	protected PCAlignment le;
	protected PCAlignment ng;
	protected PCAlignment tn;
	protected PCAlignment ne;
	protected PCAlignment cg;
	protected PCAlignment cn;
	protected PCAlignment ce;

	/**
	 * Sets up the absolute minimum amount of data to create a PlayerCharacter
	 * Object.
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		Globals.setUseGUI(false);
		Globals.emptyLists();
		final GameMode gamemode = SettingsHandler.getGame();
		
		str = new PCStat();
		str.setName("Strength");
		str.put(StringKey.ABB, "STR");

		dex = new PCStat();
		dex.setName("Dexterity");
		dex.put(StringKey.ABB, "DEX");

		final PCStat con = new PCStat();
		con.setName("Constitution");
		con.put(StringKey.ABB, "CON");

		intel = new PCStat();
		intel.setName("Intelligence");
		intel.put(StringKey.ABB, "INT");

		wis = new PCStat();
		wis.setName("Wisdom");
		wis.put(StringKey.ABB, "WIS");

		cha = new PCStat();
		cha.setName("Charisma");
		cha.put(StringKey.ABB, "CHA");

		TestHelper.makeSizeAdjustments();

		ReferenceContext ref = Globals.getContext().ref;
		lg = createAlignment("Lawful Good", "LG");
		ref.importObject(lg);
		ln = createAlignment("Lawful Neutral", "LN");
		ref.importObject(ln);
		le = createAlignment("Lawful Evil", "LE");
		ref.importObject(le);
		ng = createAlignment("Neutral Good", "NG");
		ref.importObject(ng);
		tn = createAlignment("True Neutral", "TN");
		ref.importObject(tn);
		ne = createAlignment("Neutral Evil", "NE");
		ref.importObject(ne);
		cg = createAlignment("Chaotic Good", "CG");
		ref.importObject(cg);
		cn = createAlignment("Chaotic Neutral", "CN");
		ref.importObject(cn);
		ce = createAlignment("Chaotic Evil", "CE");
		ref.importObject(ce);
		ref.importObject(createAlignment("None", "NONE"));
		ref.importObject(createAlignment("Deity's", "Deity"));

		gamemode.setBonusFeatLevels("3|3");

		SettingsHandler.setGame("3.5");
		PluginLoader ploader = PluginLoader.inst();
		ploader.startSystemPlugins(Constants.s_SYSTEM_TOKENS);
		
		ref.importObject(str);
		ref.importObject(dex);
		ref.importObject(con);
		ref.importObject(intel);
		ref.importObject(wis);
		ref.importObject(cha);
		for (PCStat stat : ref.getOrderSortedCDOMObjects(PCStat.class))
		{
			ref.registerAbbreviation(stat, stat.getAbb());
		}
		for (PCAlignment al : ref.getOrderSortedCDOMObjects(PCAlignment.class))
		{
			ref.registerAbbreviation(al, al.getKeyName());
		}
		
		character = new PlayerCharacter();
	}

	/**
	 * Constructs a new <code>AbstractCharacterTestCase</code>.
	 *
	 * @see PCGenTestCase#PCGenTestCase()
	 */
	public AbstractCharacterTestCase()
	{
		super();
	}

	/**
	 * Constructs a new <code>AbstractCharacterTestCase</code> with the given
	 * <var>name</var>.
	 *
	 * @param name the test case name
	 *
	 * @see PCGenTestCase#PCGenTestCase(String)
	 */
	public AbstractCharacterTestCase(final String name)
	{
		super(name);
	}

	public static PCAlignment createAlignment(final String longName,
		final String shortName)
	{
		final PCAlignment align = new PCAlignment();
		align.setName(longName);
		align.put(StringKey.KEY_NAME, shortName);
		return align;
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		character = null;
		super.tearDown();
	}

	/**
	 * @return Returns the character.
	 */
	public PlayerCharacter getCharacter()
	{
		return character;
	}

	/**
	 * @param aCharacter The character to set.
	 */
	public void setCharacter(final PlayerCharacter aCharacter)
	{
		this.character = aCharacter;
	}

	/**
	 * Set the value of the stat for the character.
	 *
	 * @param pc The Player Character
	 * @param statName The name of the stat to be set (eg DEX)
	 * @param value The value to be set (eg 18)
	 */
	public void setPCStat(final PlayerCharacter pc, final PCStat stat,
			final int value)
	{
		String statName = stat.getAbb();
		pc.setAssoc(stat, AssociationKey.STAT_SCORE, value);
		stat.put(StringKey.STAT_MOD, "floor(SCORE/2)-5");
		stat.put(VariableKey.getConstant("MAXLEVELSTAT=" + statName),
				FormulaFactory.getFormulaFor(statName + "SCORE-10"));
		if (statName.equals("STR"))
		{
			Globals.getContext().getObjectContext().put(stat,
					VariableKey.getConstant("LOADSCORE"),
					FormulaFactory.getFormulaFor("STRSCORE"));
		}
	}
}
