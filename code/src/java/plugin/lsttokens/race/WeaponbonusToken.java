/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.race;

import java.util.Collection;
import java.util.StringTokenizer;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.reference.ReferenceUtilities;
import pcgen.core.Race;
import pcgen.core.WeaponProf;
import pcgen.rules.context.AssociatedChanges;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * Class deals with WEAPONBONUS Token
 */
public class WeaponbonusToken extends AbstractTokenWithSeparator<Race>
		implements CDOMPrimaryToken<Race>
{

	private static final Class<WeaponProf> WEAPONPROF_CLASS = WeaponProf.class;

	@Override
	public String getTokenName()
	{
		return "WEAPONBONUS"; //$NON-NLS-1$
	}

	@Override
	protected char separator()
	{
		return '|';
	}

	@Override
	protected ParseResult parseTokenWithSeparator(LoadContext context,
		Race race, String value)
	{
		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);
		boolean foundAny = false;
		boolean foundOther = false;

		while (tok.hasMoreTokens())
		{
			String tokText = tok.nextToken();
			if (Constants.LST_ALL.equals(tokText))
			{
				foundAny = true;
				CDOMReference<WeaponProf> ref = context.ref
						.getCDOMAllReference(WEAPONPROF_CLASS);
				context.getListContext().addToList(getTokenName(), race,
						WeaponProf.STARTING_LIST, ref);
			}
			else
			{
				foundOther = true;
				CDOMReference<WeaponProf> ref = TokenUtilities
						.getTypeOrPrimitive(context, WEAPONPROF_CLASS, tokText);
				if (ref == null)
				{
					return new ParseResult.Fail("  Error was encountered while parsing "
							+ getTokenName(), context);
				}
				context.getListContext().addToList(getTokenName(), race,
						WeaponProf.STARTING_LIST, ref);
			}
		}
		if (foundAny && foundOther)
		{
			return new ParseResult.Fail("Non-sensical " + getTokenName()
					+ ": Contains ANY and a specific reference: " + value, context);
		}
		return ParseResult.SUCCESS;
	}

	@Override
	public String[] unparse(LoadContext context, Race race)
	{
		AssociatedChanges<CDOMReference<WeaponProf>> changes = context
				.getListContext().getChangesInList(getTokenName(), race,
						WeaponProf.STARTING_LIST);
		Collection<CDOMReference<WeaponProf>> added = changes.getAdded();
		if (added == null || added.isEmpty())
		{
			// Zero indicates no add
			return null;
		}
		return new String[] { ReferenceUtilities.joinLstFormat(added,
				Constants.PIPE) };
	}

	@Override
	public Class<Race> getTokenClass()
	{
		return Race.class;
	}
}
