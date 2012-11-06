/*
 * PreRaceParser.java
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 18-Dec-2003
 *
 * Current Ver: $Revision$
 *
 * Last Editor: $Author$
 *
 * Last Edited: $Date$
 *
 */
package plugin.pretokens.parser;

import pcgen.core.prereq.Prerequisite;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.AbstractPrerequisiteListParser;
import pcgen.persistence.lst.prereq.PrerequisiteParserInterface;

/**
 * A prerequisite parser class that handles the parsing of pre reach tokens.
 *
 */
public class PreRaceParser extends AbstractPrerequisiteListParser implements
		PrerequisiteParserInterface
{
	/**
	 * Get the type of prerequisite handled by this token.
	 * @return the type of prerequisite handled by this token.
	 */
    @Override
	public String[] kindsHandled()
	{
		return new String[]{"RACE"};
	}

	/**
	 * Parse the pre req list
	 *
	 * @param kind The kind of the prerequisite (less the "PRE" prefix)
	 * @param formula The body of the prerequisite.
	 * @param invertResult Whether the prerequisite should invert the result.
	 * @param overrideQualify
	 *           if set true, this prerequisite will be enforced in spite
	 *           of any "QUALIFY" tag that may be present.
	 * @return PreReq
	 * @throws PersistenceLayerException
	 */
	@Override
	public Prerequisite parse(String kind,
	                          String formula,
	                          boolean invertResult,
	                          boolean overrideQualify) throws PersistenceLayerException
	{
		final Prerequisite prereq = super.parse(kind, formula, invertResult, overrideQualify);

		//
		// Negate the race names wrapped in []'s. Then need to bump up the required number of matches
		//
		if (formula.indexOf('[') >= 0)
		{
			NegateRaceChoice(prereq);
		}

		return prereq;
	}

	private void NegateRaceChoice(Prerequisite prereq)
	{
		int modified = 0;
		for (Prerequisite p : prereq.getPrerequisites())
		{
			if (p.getKind() == null) // PREMULT
			{
				NegateRaceChoice(p);
			}
			else
			{
				String preKey = p.getKey();
				if (preKey.startsWith("[") && preKey.endsWith("]"))
				{
					preKey = preKey.substring(1, preKey.length() - 1);
					p.setKey(preKey);
					p.setOperator(p.getOperator().invert());
					++modified;
				}
			}
		}
		if (modified > 0)
		{
			String oper = prereq.getOperand();
			try
			{
				oper = Integer.toString(Integer.parseInt(oper) + modified);
			}
			catch (NumberFormatException nfe)
			{
				oper = "(" + oper + ")+" + Integer.toString(modified);
			}
			prereq.setOperand(oper);
		}
	}
}
