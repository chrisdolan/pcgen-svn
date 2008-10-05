/**
 * pcgen.base.term.PCCountAbilitiesTypeBaseEvaluator.java
 * Copyright � 2008 Andrew Wilson <nuance@users.sourceforge.net>.
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
 * Created 09-Aug-2008 19:37:53
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.base.term;

import java.util.List;

import pcgen.core.PlayerCharacter;
import pcgen.core.Ability;

public abstract class BasePCCountAbilitiesTypeTermEvaluator extends BasePCCountAbilitiesTermEvaluator
{
	protected String[] types;

	public Float resolve(PlayerCharacter pc)
	{
		Float count = 0f;

		List<Ability> abilityList = getAbilities(pc);

		for ( Ability anAbility : abilityList )
		{
			// for each feat, look to see if it has any of the required types.
			for ( String type : types)
			{
				if (anAbility.isType(type))
				{
					count += countVisibleAbility(
							pc, anAbility, visible, hidden, false);

					break;
				}
			}
		}

		return count;
	}
}
