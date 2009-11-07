/*
 * Copyright 2008 (C) Thomas Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import pcgen.base.formula.Formula;
import pcgen.base.lang.StringUtil;
import pcgen.base.util.DoubleKeyMap;
import pcgen.base.util.MapToList;
import pcgen.base.util.TripleKeyMap;
import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.spell.Spell;
import pcgen.rules.context.AssociatedChanges;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMPrimaryParserToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * @author djones4
 *
 */
public class SpellsLst extends AbstractTokenWithSeparator<CDOMObject> implements
		CDOMPrimaryParserToken<CDOMObject>
{

	@Override
	public String getTokenName()
	{
		return "SPELLS";
	}

	@Override
	protected char separator()
	{
		return '|';
	}

	/**
	 * SPELLS:<spellbook name>|[<optional parameters, pipe deliminated>] |<spell
	 * name>[,<formula for DC>] |<spell name2>[,<formula2 for DC>] |PRExxx
	 * |PRExxx
	 *
	 * CASTERLEVEL=<formula> Casterlevel of spells TIMES=<formula> Cast Times
	 * per day, -1=At Will
	 *
	 * @param sourceLine
	 *            Line from the LST file without the SPELLS:
	 * @return spells list
	 */
	@Override
	protected ParseResult parseTokenWithSeparator(LoadContext context,
		CDOMObject obj, String sourceLine)
	{
		StringTokenizer tok = new StringTokenizer(sourceLine, Constants.PIPE);
		String spellBook = tok.nextToken();
		// Formula casterLevel = null;
		String casterLevel = null;
		String times = null;
		String timeunit = null;

		if (!tok.hasMoreTokens())
		{
			return new ParseResult.Fail(getTokenName()
					+ ": minimally requires a Spell Name");
		}
		String token = tok.nextToken();

		while (true)
		{
			if (token.startsWith("TIMES="))
			{
				if (times != null)
				{
					return new ParseResult.Fail(
							"Found two TIMES entries in " + getTokenName()
									+ ": invalid: " + sourceLine);
				}
				times = token.substring(6);
				if (times.length() == 0)
				{
					return new ParseResult.Fail(
							"Error in Times in " + getTokenName()
									+ ": argument was empty");
				}
				if (!tok.hasMoreTokens())
				{
					return new ParseResult.Fail(getTokenName()
							+ ": minimally requires "
							+ "a Spell Name (after TIMES=)");
				}
				token = tok.nextToken();
			}
			else if (token.startsWith("TIMEUNIT="))
			{
				if (timeunit != null)
				{
					return new ParseResult.Fail(
							"Found two TIMEUNIT entries in " + getTokenName()
									+ ": invalid: " + sourceLine);
				}
				timeunit = token.substring(9);
				if (timeunit.length() == 0)
				{
					return new ParseResult.Fail(
							"Error in TimeUnit in " + getTokenName()
									+ ": argument was empty");
				}
				if (!tok.hasMoreTokens())
				{
					return new ParseResult.Fail(getTokenName()
							+ ": minimally requires "
							+ "a Spell Name (after TIMEUNIT=)");
				}
				token = tok.nextToken();
			}
			else if (token.startsWith("CASTERLEVEL="))
			{
				if (casterLevel != null)
				{
					return new ParseResult.Fail(
							"Found two CASTERLEVEL entries in "
									+ getTokenName() + ": invalid: "
									+ sourceLine);
				}
				casterLevel = token.substring(12);
				if (casterLevel.length() == 0)
				{
					return new ParseResult.Fail(
							"Error in Caster Level in " + getTokenName()
									+ ": argument was empty");
				}
				if (!tok.hasMoreTokens())
				{
					return new ParseResult.Fail(getTokenName()
							+ ": minimally requires a "
							+ "Spell Name (after CASTERLEVEL=)");
				}
				token = tok.nextToken();
			}
			else
			{
				break;
			}
		}
		if (times == null)
		{
			times = "1";
		}

		if (token.charAt(0) == ',')
		{
			return new ParseResult.Fail(getTokenName()
					+ " Spell arguments may not start with , : " + token);
		}
		if (token.charAt(token.length() - 1) == ',')
		{
			return new ParseResult.Fail(getTokenName()
					+ " Spell arguments may not end with , : " + token);
		}
		if (token.indexOf(",,") != -1)
		{
			return new ParseResult.Fail(getTokenName()
					+ " Spell arguments uses double separator ,, : " + token);
		}

		/*
		 * CONSIDER This is currently order enforcing the reference fetching to
		 * match the integration tests that we perform, and their current
		 * behavior. Not sure if this is really tbe best solution?
		 *
		 * See CDOMObject.
		 */
		DoubleKeyMap<CDOMReference<Spell>, AssociationKey<?>, Object> dkm = new DoubleKeyMap<CDOMReference<Spell>, AssociationKey<?>, Object>(LinkedHashMap.class, HashMap.class);
		while (true)
		{
			int commaLoc = token.indexOf(',');
			String name = commaLoc == -1 ? token : token.substring(0, commaLoc);
			CDOMReference<Spell> spell = context.ref.getCDOMReference(
					Spell.class, name);
			dkm.put(spell, AssociationKey.CASTER_LEVEL, casterLevel);
			dkm.put(spell, AssociationKey.TIMES_PER_UNIT, FormulaFactory
					.getFormulaFor(times));
			if (timeunit != null)
			{
				dkm.put(spell, AssociationKey.TIME_UNIT, timeunit);
			}
			dkm.put(spell, AssociationKey.SPELLBOOK, spellBook);
			if (commaLoc != -1)
			{
				dkm.put(spell, AssociationKey.DC_FORMULA, token
						.substring(commaLoc + 1));
			}
			if (!tok.hasMoreTokens())
			{
				// No prereqs, so we're done
				finish(context, obj, dkm, null);
				return ParseResult.SUCCESS;
			}
			token = tok.nextToken();
			if (token.startsWith("PRE") || token.startsWith("!PRE"))
			{
				break;
			}
		}

		List<Prerequisite> prereqs = new ArrayList<Prerequisite>();

		while (true)
		{
			Prerequisite prereq = getPrerequisite(token);
			if (prereq == null)
			{
				return new ParseResult.Fail(
						"   (Did you put spells after the "
								+ "PRExxx tags in SPELLS:?)");
			}
			prereqs.add(prereq);
			if (!tok.hasMoreTokens())
			{
				break;
			}
			token = tok.nextToken();
		}

		finish(context, obj, dkm, prereqs);
		return ParseResult.SUCCESS;
	}

	public void finish(LoadContext context, CDOMObject obj,
			DoubleKeyMap<CDOMReference<Spell>, AssociationKey<?>, Object> dkm,
			List<Prerequisite> prereqs)
	{
		for (CDOMReference<Spell> spell : dkm.getKeySet())
		{
			AssociatedPrereqObject edge = context.getListContext().addToList(
					getTokenName(), obj, Spell.SPELLS, spell);
			for (AssociationKey ak : dkm.getSecondaryKeySet(spell))
			{
				edge.setAssociation(ak, dkm.get(spell, ak));
			}
			if (prereqs != null)
			{
				for (Prerequisite prereq : prereqs)
				{
					edge.addPrerequisite(prereq);
				}
			}
		}
	}

	public String[] unparse(LoadContext context, CDOMObject obj)
	{
		AssociatedChanges<CDOMReference<Spell>> changes = context
				.getListContext().getChangesInList(getTokenName(), obj,
						Spell.SPELLS);
		MapToList<CDOMReference<Spell>, AssociatedPrereqObject> mtl = changes
				.getAddedAssociations();
		if (mtl == null || mtl.isEmpty())
		{
			// Zero indicates no Token
			return null;
		}

		TripleKeyMap<Set<Prerequisite>, Map<AssociationKey<?>, Object>, CDOMReference<Spell>, String> m = new TripleKeyMap<Set<Prerequisite>, Map<AssociationKey<?>, Object>, CDOMReference<Spell>, String>();
		for (CDOMReference<Spell> lw : mtl.getKeySet())
		{
			for (AssociatedPrereqObject assoc : mtl.getListFor(lw))
			{
				Map<AssociationKey<?>, Object> am = new HashMap<AssociationKey<?>, Object>();
				String dc = null;
				for (AssociationKey<?> ak : assoc.getAssociationKeys())
				{
					// if (AssociationKey.SOURCE_URI.equals(ak)
					// || AssociationKey.FILE_LOCATION.equals(ak))
					// {
					// // Do nothing
					// }
					// else
					if (AssociationKey.DC_FORMULA.equals(ak))
					{
						dc = assoc.getAssociation(AssociationKey.DC_FORMULA);
					}
					else
					{
						am.put(ak, assoc.getAssociation(ak));
					}
				}
				m.put(new HashSet<Prerequisite>(assoc.getPrerequisiteList()),
						am, lw, dc);
			}
		}

		Set<String> set = new TreeSet<String>();
		for (Set<Prerequisite> prereqs : m.getKeySet())
		{
			for (Map<AssociationKey<?>, Object> am : m
					.getSecondaryKeySet(prereqs))
			{
				StringBuilder sb = new StringBuilder();
				sb.append(am.get(AssociationKey.SPELLBOOK));
				Formula times = AssociationKey.TIMES_PER_UNIT.cast(am
						.get(AssociationKey.TIMES_PER_UNIT));
				sb.append(Constants.PIPE).append("TIMES=").append(times);
				String timeunit = AssociationKey.TIME_UNIT.cast(am
						.get(AssociationKey.TIME_UNIT));
				if (timeunit != null)
				{
					sb.append(Constants.PIPE).append("TIMEUNIT=").append(
							timeunit);
				}
				String casterLvl = AssociationKey.CASTER_LEVEL.cast(am
						.get(AssociationKey.CASTER_LEVEL));
				if (casterLvl != null)
				{
					sb.append(Constants.PIPE).append("CASTERLEVEL=").append(
							casterLvl);
				}
				Set<String> spellSet = new TreeSet<String>();
				for (CDOMReference<Spell> spell : m.getTertiaryKeySet(prereqs,
						am))
				{
					String spellString = spell.getLSTformat();
					String dc = m.get(prereqs, am, spell);
					if (dc != null)
					{
						spellString += Constants.COMMA + dc;
					}
					spellSet.add(spellString);
				}
				sb.append(Constants.PIPE);
				sb.append(StringUtil.join(spellSet, Constants.PIPE));
				if (prereqs != null && !prereqs.isEmpty())
				{
					sb.append(Constants.PIPE);
					sb.append(getPrerequisiteString(context, prereqs));
				}
				set.add(sb.toString());
			}
		}
		return set.toArray(new String[set.size()]);
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}
}
