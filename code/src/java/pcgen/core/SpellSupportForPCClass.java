/*
 * SpellSupportForPCClass
 * Copyright 2009 (c) Tom Parker <thpr@users.sourceforge.net>
 * derived from PCClass.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMList;
import pcgen.cdom.base.Constants;
import pcgen.cdom.content.KnownSpellIdentifier;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.AssociationListKey;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.inst.PCClassLevel;
import pcgen.core.analysis.DomainApplication;
import pcgen.core.analysis.SpellCountCalc;
import pcgen.core.analysis.SpellLevel;
import pcgen.core.analysis.StatAnalysis;
import pcgen.core.character.CharacterSpell;
import pcgen.core.spell.Spell;

public class SpellSupportForPCClass
{
	/*
	 * ALLCLASSLEVELS castForLevelMap is part of PCClassLevel - or nothing at
	 * all since this seems to be a form of cache? - DELETEVARIABLE
	 */
	private HashMap<Integer, Integer> castForLevelMap = null;

	private SpellProgressionCache spellCache = null;

	private boolean spellCacheValid = false;

	private final PCClass source;

	public SpellSupportForPCClass(PCClass cis)
	{
		source = cis;
	}

	/**
	 * Get the highest level of spell that this class can cast.
	 * 
	 * @return the highest level of spells that this class can cast, or -1 if
	 *         this class can not cast spells
	 */
	/*
	 * PCCLASSLEVELONLY This calculation is dependent upon the class level and
	 * is therefore appropriate only for PCClassLevel
	 */
	public int getMaxCastLevel()
	{
		int currHighest = -1;
		if (castForLevelMap != null)
		{
			for (int key : castForLevelMap.keySet())
			{
				final Integer value = castForLevelMap.get(key);
				if (value != null)
				{
					if (value > 0 && key > currHighest)
					{
						currHighest = key;
					}
				}
			}
		}
		return currHighest;
	}

	public List<Formula> getCastListForLevel(int aLevel)
	{
		if (!updateSpellCache(false))
		{
			return null;
		}
		return spellCache.getCastForLevel(aLevel);
	}

	public boolean hasCastList()
	{
		return updateSpellCache(false) && spellCache.hasCastProgression();
	}

	public int getHighestLevelSpell()
	{
		if (!updateSpellCache(false))
		{
			return -1;
		}
		return Math.max(spellCache.getHighestCastSpellLevel(), spellCache
				.getHighestKnownSpellLevel());
	}

	public boolean zeroCastSpells()
	{
		if (!updateSpellCache(false) || !spellCache.hasCastProgression())
		{
			return true;
		}
		/*
		 * CONSIDER This is just blatently wrong because it is not considering
		 * formulas, and not considering bonuses... - thpr 11/8/06
		 * 
		 * May not be a big issue other than a poorly named method, but need to
		 * check what is really required here
		 */
		for (List<Formula> l : spellCache.getCastProgression().values())
		{
			for (Formula st : l)
			{
				try
				{
					if (Integer.parseInt(st.toString()) > 0)
					{
						return false;
					}
				}
				catch (NumberFormatException nfe)
				{
					// ignore
				}
			}
		}

		return true;
	}

	public int getKnownForLevel(int spellLevel, String bookName,
			PlayerCharacter aPC)
	{
		int total = 0;
		int stat = 0;
		final String classKeyName = "CLASS." + source.getKeyName();
		final String levelSpellLevel = ";LEVEL." + spellLevel;
		final String allSpellLevel = ";LEVEL.All";

		int pcLevel = source.getLevel(aPC);
		pcLevel += (int) aPC.getTotalBonusTo("PCLEVEL", source.getKeyName());
		pcLevel += (int) aPC.getTotalBonusTo("PCLEVEL", "TYPE."
				+ source.getSpellType());

		/*
		 * CONSIDER Why is known testing getNumFromCastList??? - thpr 11/8/06
		 */
		if (updateSpellCache(false) && spellCache.hasCastProgression()
				&& (getNumFromCastList(pcLevel, spellLevel, aPC) < 0))
		{
			// Don't know any spells of this level
			// however, character might have a bonus spells e.g. from certain
			// feats
			return (int) aPC.getTotalBonusTo("SPELLKNOWN", classKeyName
					+ levelSpellLevel);
		}

		total += (int) aPC.getTotalBonusTo("SPELLKNOWN", classKeyName
				+ levelSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLKNOWN", "TYPE."
				+ source.getSpellType() + levelSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLKNOWN", "CLASS.Any"
				+ levelSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLKNOWN", classKeyName
				+ allSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLKNOWN", "TYPE."
				+ source.getSpellType() + allSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLKNOWN", "CLASS.Any"
				+ allSpellLevel);

		PCStat aStat = source.baseSpellStat();
		String statString = Constants.s_NONE;

		if (aStat != null)
		{
			stat = StatAnalysis.getTotalStatFor(aPC, aStat);
			statString = aStat.getAbb();
		}

		final int bonusStat = (int) aPC.getTotalBonusTo("STAT", "KNOWN."
				+ statString)
				+ (int) aPC.getTotalBonusTo("STAT", "BASESPELLKNOWNSTAT")
				+ (int) aPC.getTotalBonusTo("STAT", "BASESPELLKNOWNSTAT;CLASS."
						+ source.getKeyName());

		if (!source.useSpellSpellStat())
		{
			final int maxSpellLevel = aPC.getVariableValue(
					"MAXLEVELSTAT=" + statString, "").intValue();

			if ((maxSpellLevel + bonusStat) < spellLevel)
			{
				return total;
			}
		}

		stat += bonusStat;

		int mult = (int) aPC.getTotalBonusTo("SPELLKNOWNMULT", classKeyName
				+ levelSpellLevel);
		mult += (int) aPC.getTotalBonusTo("SPELLKNOWNMULT", "TYPE."
				+ source.getSpellType() + levelSpellLevel);

		if (mult < 1)
		{
			mult = 1;
		}

		if (!updateSpellCache(false))
		{
			return total;
		}

		if (spellCache.hasKnownProgression())
		{
			List<Formula> knownList = spellCache.getKnownForLevel(pcLevel);
			if (spellLevel >= 0 && spellLevel < knownList.size())
			{
				total += mult
						* knownList.get(spellLevel).resolve(aPC, "").intValue();

				// add Stat based bonus
				final String bonusSpell = Globals.getBonusSpellMap().get(
						String.valueOf(spellLevel));

				if (Globals.checkRule(RuleConstants.BONUSSPELLKNOWN)
						&& (bonusSpell != null) && !bonusSpell.equals("0|0"))
				{
					final StringTokenizer s = new StringTokenizer(bonusSpell,
							"|");
					final int base = Integer.parseInt(s.nextToken());
					final int range = Integer.parseInt(s.nextToken());

					if (stat >= base)
					{
						total += Math.max(0, (stat - base + range) / range);
					}
				}
			}
		}

		// if we have known spells (0==no known spells recorded)
		// or a psi specialty.
		if (total > 0 && spellLevel > 0)
		{
			// make sure any slots due from specialties
			total += source.getKnownSpellsFromSpecialty();
			// (including domains) are added
			Integer assoc = aPC.getAssoc(source,
					AssociationKey.DOMAIN_SPELL_COUNT);
			if (assoc != null)
			{
				total += assoc;
			}
		}

		// Add in any from SPELLKNOWN
		total += SpellLevel.getNumBonusKnowSpellsForLevel(aPC, spellLevel,
				source.getClassList());

		return total;
	}

	public int getMinLevelForSpellLevel(int spellLevel, boolean allowBonus)
	{
		if (!updateSpellCache(false))
		{
			return -1;
		}
		return spellCache.getMinLevelForSpellLevel(spellLevel, allowBonus);
	}

	public int getMaxSpellLevelForClassLevel(int classLevel)
	{
		if (!updateSpellCache(false))
		{
			return -1;
		}
		return spellCache.getMaxSpellLevelForClassLevel(classLevel);
	}

	public boolean hasKnownList()
	{
		return updateSpellCache(false) && spellCache.hasKnownProgression();
	}

	public int getSpecialtyKnownForLevel(int spellLevel, PlayerCharacter aPC)
	{
		int total;
		total = (int) aPC.getTotalBonusTo("SPECIALTYSPELLKNOWN", "CLASS."
				+ source.getKeyName() + ";LEVEL." + spellLevel);
		total += (int) aPC.getTotalBonusTo("SPECIALTYSPELLKNOWN", "TYPE."
				+ source.getSpellType() + ";LEVEL." + spellLevel);

		int pcLevel = source.getLevel(aPC);
		pcLevel += (int) aPC.getTotalBonusTo("PCLEVEL", source.getKeyName());
		pcLevel += (int) aPC.getTotalBonusTo("PCLEVEL", "TYPE."
				+ source.getSpellType());

		PCStat aStat = source.baseSpellStat();

		if (aStat != null)
		{
			final int maxSpellLevel = aPC.getVariableValue(
					"MAXLEVELSTAT=" + aStat.getAbb(), "").intValue();

			if (spellLevel > maxSpellLevel)
			{
				return total;
			}
		}

		if (updateSpellCache(false))
		{
			List<Formula> specKnown = spellCache
					.getSpecialtyKnownForLevel(pcLevel);
			if (specKnown != null && specKnown.size() > spellLevel)
			{
				total += specKnown.get(spellLevel).resolve(aPC, "").intValue();
			}
		}

		// make sure any slots due from specialties
		total += source.getKnownSpellsFromSpecialty();
		// (including domains) are added
		Integer assoc = aPC.getAssoc(source, AssociationKey.DOMAIN_SPELL_COUNT);
		if (assoc != null)
		{
			total += assoc;
		}

		return total;
	}

	public boolean updateSpellCache(boolean force)
	{
		if (force || !spellCacheValid)
		{
			SpellProgressionCache cache = new SpellProgressionCache();
			for (PCClassLevel cl : source.getClassLevelCollection())
			{
				Integer lvl = cl.get(IntegerKey.LEVEL);
				List<Formula> cast = cl.getListFor(ListKey.CAST);
				if (cast != null)
				{
					cache.setCast(lvl, cast);
				}
				List<Formula> known = cl.getListFor(ListKey.KNOWN);
				if (known != null)
				{
					cache.setKnown(lvl, known);
				}
				List<Formula> spec = cl.getListFor(ListKey.SPECIALTYKNOWN);
				if (spec != null)
				{
					cache.setSpecialtyKnown(lvl, spec);
				}
			}
			if (!cache.isEmpty())
			{
				spellCache = cache;
			}
			spellCacheValid = true;
		}
		return spellCache != null;
	}

	/**
	 * Build a caster level map for this class. The map will be of the form
	 * <String,String> where the key is the spell level and the value is the
	 * number of times per day that spell level can be cast by the character
	 * 
	 * @param aPC
	 * 
	 * TODO: Why is this not a Map<Integer,Integer>
	 */
	/*
	 * PCCLASSLEVELONLY This calculation is dependent upon the class level and
	 * is therefore appropriate only for PCClassLevel
	 */
	private void calcCastPerDayMapForLevel(final PlayerCharacter aPC)
	{
		//
		// TODO: Shouldn't we be using Globals.getLevelInfo().size() instead of
		// 100?
		// Byngl -- November 25, 2002
		//
		if (castForLevelMap == null)
		{
			castForLevelMap = new HashMap<Integer, Integer>(100);
		}
		for (int i = 0; i < 100; i++)
		{
			final int s = getCastForLevel(i, aPC);
			castForLevelMap.put(i, s);
		}
	}

	public boolean isAutoKnownSpell(Spell aSpell, int spellLevel,
			boolean useMap, PlayerCharacter aPC)
	{
		List<KnownSpellIdentifier> knownSpellsList = source.getKnownSpells();
		if (knownSpellsList == null)
		{
			return false;
		}

		if (useMap)
		{
			final Integer val = castForLevelMap.get(spellLevel);

			if ((val == null) || val == 0 || (aSpell == null))
			{
				return false;
			}
		}
		else if ((getCastForLevel(spellLevel, aPC) == 0) || (aSpell == null))
		{
			return false;
		}

		if (SpellCountCalc.isProhibited(aSpell, source, aPC) && !SpellCountCalc.isSpecialtySpell(aPC, source, aSpell))
		{
			return false;
		}

		// iterate through the KNOWNSPELLS: tag
		for (KnownSpellIdentifier filter : knownSpellsList)
		{
			if (filter.matchesFilter(aSpell, spellLevel))
			{
				return true;
			}
		}
		return false;
	}

	public int getNumFromCastList(int iCasterLevel, int iSpellLevel,
			PlayerCharacter aPC)
	{
		if (iCasterLevel == 0)
		{
			// can't cast spells!
			return -1;
		}

		List<Formula> castListForLevel = getCastListForLevel(iCasterLevel);
		if (castListForLevel == null || iSpellLevel >= castListForLevel.size())
		{
			return -1;
		}
		return castListForLevel.get(iSpellLevel).resolve(aPC, "").intValue();
	}

	public int getCastForLevel(int spellLevel, PlayerCharacter aPC)
	{
		return getCastForLevel(spellLevel, Globals.getDefaultSpellBook(), true,
				true, aPC);
	}

	public int getCastForLevel(int spellLevel, String bookName,
			boolean includeAdj, boolean limitByStat, PlayerCharacter aPC)
	{
		int pcLevel = source.getLevel(aPC);
		int total = 0;
		int stat = 0;
		final String classKeyName = "CLASS." + source.getKeyName();
		final String levelSpellLevel = ";LEVEL." + spellLevel;
		final String allSpellLevel = ";LEVEL.All";

		pcLevel += (int) aPC.getTotalBonusTo("PCLEVEL", source.getKeyName());
		pcLevel += (int) aPC.getTotalBonusTo("PCLEVEL", "TYPE."
				+ source.getSpellType());

		if (getNumFromCastList(pcLevel, spellLevel, aPC) < 0)
		{
			// can't cast spells of this level
			// however, character might have a bonus spell slot e.g. from
			// certain feats
			return (int) aPC.getTotalBonusTo("SPELLCAST", classKeyName
					+ levelSpellLevel);
		}

		total += (int) aPC.getTotalBonusTo("SPELLCAST", classKeyName
				+ levelSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLCAST", "TYPE."
				+ source.getSpellType() + levelSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLCAST", "CLASS.Any"
				+ levelSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLCAST", classKeyName
				+ allSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLCAST", "TYPE."
				+ source.getSpellType() + allSpellLevel);
		total += (int) aPC.getTotalBonusTo("SPELLCAST", "CLASS.Any"
				+ allSpellLevel);

		PCStat aStat = source.bonusSpellStat();
		String statString = Constants.s_NONE;

		if (aStat != null)
		{
			stat = StatAnalysis.getTotalStatFor(aPC, aStat);
			statString = aStat.getAbb();
		}

		final int bonusStat = (int) aPC.getTotalBonusTo("STAT", "CAST."
				+ statString)
				+ (int) aPC.getTotalBonusTo("STAT", "BASESPELLSTAT")
				+ (int) aPC.getTotalBonusTo("STAT", "BASESPELLSTAT;CLASS."
						+ source.getKeyName());

		if (limitByStat)
		{
			PCStat ss = source.baseSpellStat();
			if (ss != null)
			{
				final int maxSpellLevel = aPC.getVariableValue(
						"MAXLEVELSTAT=" + ss.getAbb(), "").intValue();

				if ((maxSpellLevel + bonusStat) < spellLevel)
				{
					return total;
				}
			}
		}

		stat += bonusStat;

		// Now we decide whether to adjust the number of slots down
		// the road by adding specialty slots.
		// Reworked to consider the fact that a lower-level
		// specialty spell can go into this level of specialty slot
		//
		int adj = 0;

		if (includeAdj
				&& !bookName.equals(Globals.getDefaultSpellBook())
				&& (aPC.hasAssocs(source, AssociationKey.SPECIALTY) || aPC
						.hasDomains()))
		{
			// We need to do this for EVERY spell level up to the
			// one really under consideration, because if there
			// are any specialty spells available BELOW this level,
			// we might wind up using THIS level's slots for them.
			for (int ix = 0; ix <= spellLevel; ++ix)
			{
				final List<CharacterSpell> aList = aPC.getCharacterSpells(
						source, null, Constants.EMPTY_STRING, ix);
				List<Spell> bList = new ArrayList<Spell>();

				if (!aList.isEmpty())
				{
					// Assume no null check on castInfo requried, because
					// getNumFromCastList above would have returned -1
					if ((ix > 0)
							&& "DIVINE".equalsIgnoreCase(source.getSpellType()))
					{
						for (Domain d : aPC.getDomainSet())
						{
							if (source.getKeyName().equals(
									aPC.getDomainSource(d).getPcclass()
											.getKeyName()))
							{
								bList = Globals
										.getSpellsIn(
												ix,
												Collections
														.singletonList(d
																.get(ObjectKey.DOMAIN_SPELLLIST)));
							}
						}
					}

					for (CharacterSpell cs : aList)
					{
						int x = -1;

						if (!bList.isEmpty())
						{
							if (bList.contains(cs.getSpell()))
							{
								x = 0;
							}
						}
						else
						{
							x = cs.getInfoIndexFor(aPC, Constants.EMPTY_STRING,
									ix, 1);
						}

						if (x > -1)
						{
							PCClass target = source;
							String subClassKey = aPC.getAssoc(source,
									AssociationKey.SUBCLASS_KEY);
							if (subClassKey != null
									&& (subClassKey.length() > 0)
									&& !subClassKey.equals(Constants.s_NONE))
							{
								target = source.getSubClassKeyed(subClassKey);
							}
							adj = aPC.getSpellSupport(target).getSpecialtyKnownForLevel(spellLevel, aPC);

							break;
						}
					}
				}
				// end of what to do if aList is not empty

				if (adj > 0)
				{
					break;
				}
			}
			// end of looping up to this level looking for specialty spells that
			// can be cast
		}
		// end of deciding whether there are specialty slots to distribute

		int mult = (int) aPC.getTotalBonusTo("SPELLCASTMULT", classKeyName
				+ levelSpellLevel);
		mult += (int) aPC.getTotalBonusTo("SPELLCASTMULT", "TYPE."
				+ source.getSpellType() + levelSpellLevel);

		if (mult < 1)
		{
			mult = 1;
		}

		final int t = getNumFromCastList(pcLevel, spellLevel, aPC);

		total += ((t * mult) + adj);

		// TODO - God I hate all these strings. Return an array or list.
		final String bonusSpell = Globals.getBonusSpellMap().get(
				String.valueOf(spellLevel));

		// TODO - Yuck. Figure out how to get rid of hardcoded "0|0"

		if ((bonusSpell != null) && !bonusSpell.equals("0|0")) //$NON-NLS-1$
		{
			final StringTokenizer s = new StringTokenizer(bonusSpell,
					Constants.PIPE);
			final int base = Integer.parseInt(s.nextToken());
			final int range = Integer.parseInt(s.nextToken());

			if (stat >= base)
			{
				total += Math.max(0, (stat - base + range) / range);
			}
		}

		return total;
	}

	public void calculateKnownSpellsForClassLevel(PlayerCharacter aPC)
	{
		// If this class has at least one entry in the "Known spells" tag
		// And we aer set up to automatically assign known spells...
		if (source.hasKnownSpells() && !aPC.isImporting()
				&& aPC.getAutoSpells())
		{
			// Get every spell that can be cast by this class.
			final List<Spell> cspelllist = Globals.getSpellsIn(-1, source
					.getSpellLists(aPC));
			if (cspelllist.isEmpty())
			{
				return;
			}

			// Recalculate the number of spells per day of each level
			// that this chracter can cast in this class.
			calcCastPerDayMapForLevel(aPC);

			// Get the maximum spell level that this character can cast.
			final int _maxLevel = getMaxCastLevel();

			// Get the key for this class (i.e. "CLASS|Cleric")
			List<? extends CDOMList<Spell>> lists = source.getSpellLists(aPC);

			// For every spell that this class can ever cast.
			for (Spell spell : cspelllist)
			{
				// For each spell level that this class can cast this spell at
				final Integer[] spellLevels = SpellLevel.levelForKey(spell,
						lists, aPC);
				for (Integer si = 0; si < spellLevels.length; ++si)
				{
					final int spellLevel = spellLevels[si];
					if (spellLevel == -1)
					{
						continue;
					}
					if (spellLevel <= _maxLevel)
					{
						// If the spell is autoknown at this level
						if (isAutoKnownSpell(spell, spellLevel, true, aPC))
						{
							CharacterSpell cs = aPC.getCharacterSpellForSpell(
									source, spell);
							if (cs == null)
							{
								// Create a new character spell for this level.
								cs = new CharacterSpell(source, spell);
								cs.addInfo(spellLevel, 1, Globals
										.getDefaultSpellBook());
								aPC
										.addAssoc(
												source,
												AssociationListKey.CHARACTER_SPELLS,
												cs);
							}
							else
							{
								if (cs.getSpellInfoFor(aPC, Globals
										.getDefaultSpellBook(), spellLevel, -1) == null)
								{
									cs.addInfo(spellLevel, 1, Globals
											.getDefaultSpellBook());
								}
								else
								{
									// already know this one
								}
							}
						}
					}
				}
			}

			for (Domain d : aPC.getDomainSet())
			{
				if (source.getKeyName().equals(
						aPC.getDomainSource(d).getPcclass().getKeyName()))
				{
					DomainApplication.addSpellsToClassForLevels(aPC, d, source,
							0, _maxLevel);
				}
			}
		}
	}

	public int getHighestLevelSpell(PlayerCharacter pc)
	{
		final String classKeyName = "CLASS." + source.getKeyName();
		int mapHigh = getHighestLevelSpell();
		int high = mapHigh;
		for (int i = mapHigh; i < mapHigh + 30; i++)
		{
			final String levelSpellLevel = ";LEVEL." + i;
			if (pc.getTotalBonusTo("SPELLCAST", classKeyName + levelSpellLevel) > 0)
			{
				high = i;
			}
			else if (pc.getTotalBonusTo("SPELLKNOWN", classKeyName
					+ levelSpellLevel) > 0)
			{
				high = i;
			}
		}
		return high;
	}

	public String getBonusCastForLevelString(int spellLevel, String bookName,
			PlayerCharacter aPC)
	{
		if (getCastForLevel(spellLevel, bookName, true, true, aPC) > 0)
		{
			// if this class has a specialty, return +1
			if (aPC.hasAssocs(source, AssociationKey.SPECIALTY))
			{
				PCClass target = source;
				String subClassKey = aPC.getAssoc(source,
						AssociationKey.SUBCLASS_KEY);
				if (subClassKey != null && (subClassKey.length() > 0)
						&& !subClassKey.equals(Constants.s_NONE))
				{
					target = source.getSubClassKeyed(subClassKey);
				}

				return "+" + aPC.getSpellSupport(target).getSpecialtyKnownForLevel(spellLevel, aPC);
			}

			if (!aPC.hasDomains())
			{
				return "";
			}

			// if the spelllevel is >0 and this class has a characterdomain
			// associated with it, return +1
			if ((spellLevel > 0)
					&& "DIVINE".equalsIgnoreCase(source.getSpellType()))
			{
				for (Domain d : aPC.getDomainSet())
				{
					if (source.getKeyName().equals(
							aPC.getDomainSource(d).getPcclass().getKeyName()))
					{
						return "+1";
					}
				}
			}
		}

		return "";
	}

	public boolean hasKnownSpells(PlayerCharacter aPC)
	{
		for (int i = 0; i <= getHighestLevelSpell(); i++)
		{
			if (getKnownForLevel(i, aPC) > 0)
			{
				return true;
			}
		}

		return false;
	}

	public void removeKnownSpellsForClassLevel(PlayerCharacter aPC)
	{
		if (!source.hasKnownSpells() || aPC.isImporting()
				|| !aPC.getAutoSpells())
		{
			return;
		}

		if (!aPC.hasAssocs(source, AssociationListKey.CHARACTER_SPELLS))
		{
			return;
		}

		List<? extends CDOMList<Spell>> lists = source.getSpellLists(aPC);

		for (Iterator<CharacterSpell> iter = aPC.getSafeAssocList(source,
				AssociationListKey.CHARACTER_SPELLS).iterator(); iter.hasNext();)
		{
			final CharacterSpell charSpell = iter.next();

			final Spell aSpell = charSpell.getSpell();

			// Check that the character can still cast spells of this level.
			final Integer[] spellLevels = SpellLevel.levelForKey(aSpell, lists,
					aPC);
			for (Integer i = 0; i < spellLevels.length; i++)
			{
				final int spellLevel = spellLevels[i];
				if (spellLevel == -1)
				{
					continue;
				}

				final boolean isKnownAtThisLevel = isAutoKnownSpell(aSpell,
						spellLevel, true, aPC);

				if (!isKnownAtThisLevel)
				{
					iter.remove();
				}
			}
		}
	}

	public int getKnownForLevel(final int spellLevel, final PlayerCharacter aPC)
	{
		return getKnownForLevel(spellLevel, "null", aPC);
	}

}
