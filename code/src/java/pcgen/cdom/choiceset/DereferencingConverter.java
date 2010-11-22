/*
 * Copyright 2010 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.cdom.choiceset;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Converter;
import pcgen.cdom.base.PrimitiveFilter;
import pcgen.core.PlayerCharacter;

public class DereferencingConverter<T> implements Converter<T, T>
{

	private final PlayerCharacter character;

	public DereferencingConverter(PlayerCharacter pc)
	{
		character = pc;
	}

	public Collection<T> convert(CDOMReference<T> orig)
	{
		return orig.getContainedObjects();
	}

	public Collection<T> convert(CDOMReference<T> orig, PrimitiveFilter<T> lim)
	{
		Set<T> returnSet = new HashSet<T>();
		for (T o : orig.getContainedObjects())
		{
			if (lim.allow(character, o))
			{
				returnSet.add(o);
			}
		}
		return returnSet;
	}
}