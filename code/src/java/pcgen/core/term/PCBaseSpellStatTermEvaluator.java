/**
 * pcgen.core.term.PCBaseSpellStatTermEvaluator.java
 * Copyright (c) 2008 Andrew Wilson <nuance@users.sourceforge.net>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created 03-Aug-2008 22:55:01
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.core.PlayerCharacter;
import pcgen.core.PCClass;

public class PCBaseSpellStatTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator {

	private final String source;

	public PCBaseSpellStatTermEvaluator(
			String originalText, String source)
	{
		this.originalText = originalText;
		this.source       = source;
	}

	public Float resolve(PlayerCharacter pc)
	{
		PCClass pcClass = pc.getClassKeyed(source);

		if (pcClass == null)
		{
			return 0f;
		}

		return (float) pc.getBaseSpellStatBonus(pcClass);
	}

	public boolean isSourceDependant()
	{
		return true;
	}

	public boolean isStatic()
	{
		return false;
	}
}
