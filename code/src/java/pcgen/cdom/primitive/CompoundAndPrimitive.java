/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.primitive;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import pcgen.cdom.base.Constants;
import pcgen.cdom.base.Converter;
import pcgen.cdom.base.PrimitiveCollection;
import pcgen.cdom.enumeration.GroupingState;
import pcgen.core.PlayerCharacter;
import pcgen.util.Logging;

public class CompoundAndPrimitive<T> implements PrimitiveCollection<T>
{

	private final Class<? super T> refClass;

	private final Set<PrimitiveCollection<T>> primCollection = new TreeSet<PrimitiveCollection<T>>(
			PrimitiveUtilities.COLLECTION_SORTER);

	public CompoundAndPrimitive(Collection<PrimitiveCollection<T>> pcfCollection)
	{
		if (pcfCollection == null)
		{
			throw new IllegalArgumentException(
					"Collection for CompoundAndPrimitive cannot be null");
		}
		if (pcfCollection.isEmpty())
		{
			throw new IllegalArgumentException(
					"Collection for CompoundAndPrimitive cannot be empty");
		}
		Class<? super T> pcfClass = null;
		primCollection.addAll(pcfCollection);
		if (primCollection.size() != pcfCollection.size())
		{
			Logging.log(Level.WARNING, "Found duplicate item in "
					+ pcfCollection);
			primCollection.add(PrimitiveCollection.INVALID);
		}
		for (PrimitiveCollection<T> pcf : primCollection)
		{
			Class<? super T> refClass = pcf.getReferenceClass();
			if (pcfClass == null)
			{
				pcfClass = refClass;
			}
			else if (!pcfClass.isAssignableFrom(refClass))
			{
				if (refClass.isAssignableFrom(pcfClass))
				{
					pcfClass = refClass;
				}
				else
				{
					throw new IllegalArgumentException(
							"List contains incompatible types: "
									+ pcfClass.getSimpleName() + " and "
									+ refClass.getSimpleName());
				}
			}
		}
		refClass = pcfClass;
	}

	public <R> Collection<R> getCollection(PlayerCharacter pc, Converter<T, R> c)
	{
		Collection<R> returnSet = null;
		for (PrimitiveCollection<T> cs : primCollection)
		{
			if (returnSet == null)
			{
				returnSet = cs.getCollection(pc, c);
			}
			else
			{
				returnSet.retainAll(cs.getCollection(pc, c));
			}
		}
		return returnSet;
	}

	public Class<? super T> getReferenceClass()
	{
		return refClass;
	}

	/**
	 * Returns the GroupingState for this CompoundAndPrimitive. The
	 * GroupingState indicates how this CompoundAndPrimitive can be combined
	 * with other PrimitiveChoiceSets.
	 * 
	 * @return The GroupingState for this CompoundAndPrimitive.
	 */
	public GroupingState getGroupingState()
	{
		GroupingState state = GroupingState.EMPTY;
		for (PrimitiveCollection<T> pcs : primCollection)
		{
			state = pcs.getGroupingState().add(state);
		}
		return state.compound(GroupingState.ALLOWS_INTERSECTION);
	}

	/**
	 * Returns a representation of this CompoundAndPrimitive, suitable for
	 * storing in an LST file.
	 * 
	 * @return A representation of this CompoundAndPrimitive, suitable for
	 *         storing in an LST file.
	 */
	public String getLSTformat(boolean useAny)
	{
		return PrimitiveUtilities.joinLstFormat(primCollection,
				Constants.COMMA, useAny);
	}

	/**
	 * Returns the consistent-with-equals hashCode for this CompoundAndPrimitive
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return primCollection.hashCode();
	}

	/**
	 * Returns true if this CompoundAndPrimitive is equal to the given Object.
	 * Equality is defined as being another CompoundAndPrimitive object with
	 * equal underlying contents.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof CompoundAndPrimitive)
				&& ((CompoundAndPrimitive<?>) obj).primCollection
						.equals(primCollection);
	}
}
