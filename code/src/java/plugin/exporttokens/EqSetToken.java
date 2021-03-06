/*
 * EQSetToken.java
 * Copyright 2003 (C) Devon Jones <soulcatcher@evilsoft.org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on December 15, 2003, 12:21 PM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.exporttokens;

import pcgen.core.PlayerCharacter;
import pcgen.core.display.CharacterDisplay;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;

/**
 * Deal with EQSET Token
 * 
 * EQSET.START
 * EQSET.END
 * EQSET.NUMBER
 * EQSET.NAME
 */
public class EqSetToken extends Token
{
	/** Token Name */
	public static final String TOKENNAME = "EQSET";

	/**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
	@Override
	public String getTokenName()
	{
		return TOKENNAME;
	}

	/**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
	@Override
	public String getToken(String tokenSource, PlayerCharacter pc,
		ExportHandler eh)
	{
		String retString = "";

		if ("EQSET.START".equals(tokenSource))
		{
			//TODO: Does Nothing here, only on EQSheet exports, should that be fixed?
		}
		else if ("EQSET.END".equals(tokenSource))
		{
			//TODO: Does Nothing here, only on EQSheet exports, should that be fixed?
		}
		else if ("EQSET.NUMBER".equals(tokenSource))
		{
			retString = Integer.toString(getNumberToken(pc));
		}
		else if ("EQSET.NAME".equals(tokenSource))
		{
			retString = getNameToken(pc.getDisplay());
		}

		return retString;
	}

	/**
	 * Get Name Token
	 * @param pc
	 * @return Name Token
	 */
	public static String getNameToken(CharacterDisplay display)
	{
		return display.getCurrentEquipSetName();
	}

	/**
	 * Get Number Token
	 * @param pc
	 * @return Number Token
	 */
	public static int getNumberToken(PlayerCharacter pc)
	{
		return pc.getEquipSetNumber();
	}
}
