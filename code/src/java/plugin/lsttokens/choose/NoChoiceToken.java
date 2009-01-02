/*
 * Copyright 2007 (C) Thomas Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.choose;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.StringKey;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.util.Logging;

public class NoChoiceToken implements CDOMSecondaryToken<CDOMObject>
{

	public String getTokenName()
	{
		return "NOCHOICE";
	}

	public String getParentToken()
	{
		return "CHOOSE";
	}

	public boolean parse(LoadContext context, CDOMObject obj, String value)
			throws PersistenceLayerException
	{
		if (value == null)
		{
			// No args - legal
			context.obj.put(obj, StringKey.CHOICE_STRING, getTokenName());
			return true;
		}
		Logging.deprecationPrint("CHOOSE:" + getTokenName()
				+ " will ignore arguments: " + value);
		return false;
	}

	public String[] unparse(LoadContext context, CDOMObject cdo)
	{
		String chooseString = context.getObjectContext().getString(cdo,
				StringKey.CHOICE_STRING);
		if (chooseString == null)
		{
			return null;
		}
		String returnString;
		if (getTokenName().equals(chooseString))
		{
			returnString = "";
		}
		else
		{
			if (chooseString.indexOf(getTokenName() + '|') == -1)
			{
				return null;
			}
			returnString = chooseString.substring(getTokenName().length() + 1);
		}
		return new String[] { returnString };
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}
}
