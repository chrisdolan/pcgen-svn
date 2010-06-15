/*
 * Copyright (c) Thomas Parker, 2009.
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
package pcgen.cdom.facet;

import java.util.Map;
import java.util.Set;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.content.SpellResistance;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ObjectKey;

/**
 * ShieldProfFacet is a Facet that tracks the ShieldProfs that have been granted
 * to a Player Character.
 */
public class CharacterSpellResistanceFacet extends AbstractSourcedListFacet<Formula>
		implements DataFacetChangeListener<CDOMObject>
{
	private FormulaResolvingFacet resolveFacet = FacetLibrary
			.getFacet(FormulaResolvingFacet.class);

	/**
	 * Triggered when one of the Facets to which ShieldProfFacet listens fires a
	 * DataFacetChangeEvent to indicate a ShieldProf was added to a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataAdded(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	public void dataAdded(DataFacetChangeEvent<CDOMObject> dfce)
	{
		CDOMObject cdo = dfce.getCDOMObject();
		SpellResistance sr = cdo.get(ObjectKey.SR);
		if (sr != null)
		{
			add(dfce.getCharID(), sr.getReduction(), cdo);
		}
	}

	/**
	 * Triggered when one of the Facets to which ShieldProfFacet listens fires a
	 * DataFacetChangeEvent to indicate a ShieldProf was removed from a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataRemoved(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	public void dataRemoved(DataFacetChangeEvent<CDOMObject> dfce)
	{
		removeAll(dfce.getCharID(), dfce.getCDOMObject());
	}

	public int getSR(CharID id)
	{
		Map<Formula, Set<Object>> componentMap = getCachedMap(id);
		int sr = 0;
		if (componentMap != null)
		{
			for (Map.Entry<Formula, Set<Object>> me : componentMap.entrySet())
			{
				Formula f = me.getKey();
				Set<Object> sourceSet = me.getValue();
				for (Object source : sourceSet)
				{
					String sourceString = (source instanceof CDOMObject) ? ((CDOMObject) source)
							.getQualifiedKey()
							: "";
					sr = Math.max(sr, resolveFacet.resolve(id, f, sourceString)
							.intValue());
				}
			}
		}
		return sr;
	}
}
