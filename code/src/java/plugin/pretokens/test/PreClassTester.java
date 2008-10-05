/*
 * Created on 02-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package plugin.pretokens.test;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Equipment;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.Logging;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PreClassTester extends AbstractPrerequisiteTest implements
		PrerequisiteTest
{

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	@Override
	public int passes(final Prerequisite prereq, final Equipment equipment,
		PlayerCharacter aPC)
	{
		Logging.errorPrint("PreClass on equipment: " + equipment.getName()
			+ "  pre: " + toHtmlString(prereq));
		return 0;
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	@Override
	public int passes(final Prerequisite prereq, final PlayerCharacter character)
	{
		int runningTotal = 0;
		int countedTotal = 0;

		final boolean sumLevels = prereq.isTotalValues();
		final String aString = prereq.getKey().toUpperCase();
		final int preClass = Integer.parseInt(prereq.getOperand());

		if ("SPELLCASTER".equals(aString)) //$NON-NLS-1$
		{
			int spellCaster = character.isSpellCaster(preClass, sumLevels);
			if (spellCaster > 0)
			{
				if (prereq.isCountMultiples())
				{
					countedTotal = spellCaster;
				}
				else
				{
					runningTotal = preClass;
				}
			}
		}
		else if (aString.startsWith("SPELLCASTER.")) //$NON-NLS-1$
		{
			int spellCaster =
					character.isSpellCaster(aString.substring(12), preClass,
						sumLevels);
			if (spellCaster > 0)
			{
				if (prereq.isCountMultiples())
				{
					countedTotal = spellCaster;
				}
				else
				{
					runningTotal = preClass;
				}
			}
		}
		else if (aString.equals("ANY"))
		{
			for (PCClass cl : character.getClassList())
			{
				if (prereq.isCountMultiples())
				{
					if (cl.getLevel() >= preClass)
					{
						countedTotal++;
					}
				}
				else
				{
					runningTotal = Math.max(runningTotal, cl.getLevel());
				}
			}
		}
		else if (aString.startsWith("TYPE=") || aString.startsWith("TYPE."))
		{
			String typeString = aString.substring(5);
			for (PCClass cl : character.getClassList())
			{
				if (cl.isType(typeString))
				{
					if (prereq.isCountMultiples())
					{
						if (cl.getLevel() >= preClass)
						{
							countedTotal++;
						}
					}
					else
					{
						runningTotal = Math.max(runningTotal, cl.getLevel());
					}
				}
				else
				{
					for(CDOMReference<PCClass> ref: cl.getSafeListFor(ListKey.SERVES_AS_CLASS))
					{
						for (PCClass fakeClass : ref.getContainedObjects())
						{
							if (fakeClass.isType(typeString))
							{
								if (prereq.isCountMultiples())
								{
									if (cl.getLevel() >= preClass)
									{
										countedTotal++;
									}
								}
								else
								{
									runningTotal += cl.getLevel();
								}
								break;
							}
						}
					}
				}
			}
		}
		else
		{
			PCClass aClass = character.getClassKeyed(aString);
			if (aClass != null)
			{
				if (prereq.isCountMultiples())
				{
					if (aClass.getLevel() >= preClass)
					{
						countedTotal++;
					}
				}
				else
				{
					runningTotal += aClass.getLevel();
				}
			}
			else
			{
CLASSLIST:		for(PCClass theClass: character.getClassList())
				{
					for (CDOMReference<PCClass> ref : theClass
							.getSafeListFor(ListKey.SERVES_AS_CLASS))
					{
						for (PCClass fakeClass : ref.getContainedObjects())
						{
							if (fakeClass.getKeyName().equalsIgnoreCase(aString))
							{
								if (prereq.isCountMultiples())
								{
									if (theClass.getLevel() >= preClass)
									{
										countedTotal++;
									}
								}
								else
								{
									runningTotal += theClass.getLevel();
								}
								break CLASSLIST;
							}
						}
					}
				}
			}
		}
		runningTotal = prereq.getOperator().compare(runningTotal, preClass);
		return countedTotal(prereq, prereq.isCountMultiples() ? countedTotal : runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled()
	{
		return "CLASS"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#toHtmlString(pcgen.core.prereq.Prerequisite)
	 */
	@Override
	public String toHtmlString(final Prerequisite prereq)
	{
		final String level = prereq.getOperand();
		final String operator = prereq.getOperator().toDisplayString();

		return PropertyFactory.getFormattedString(
			"PreClass.toHtml", prereq.getKey(), operator, level); //$NON-NLS-1$
	}

}
