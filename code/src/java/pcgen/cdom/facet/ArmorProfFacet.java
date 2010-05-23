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

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.helper.ProfProvider;
import pcgen.core.ArmorProf;

/**
 * ArmorProfFacet is a Facet that tracks the ArmorProfs that have been granted to
 * a Player Character.
 */
public class ArmorProfFacet extends
		AbstractQualifiedListFacet<ProfProvider<ArmorProf>> implements
		DataFacetChangeListener<CDOMObject>
{

	/**
	 * Triggered when one of the Facets to which ArmorProfFacet listens fires a
	 * DataFacetChangeEvent to indicate a ArmorProf was added to a Player
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
		for (ProfProvider<ArmorProf> app : cdo
				.getSafeListFor(ListKey.AUTO_ARMORPROF))
		{
			CDOMObject source = null;
			Object s = dfce.getSource();
			if (s instanceof CDOMObject)
			{
				source = (CDOMObject) s;
			}
			add(dfce.getCharID(), app, source);
		}
	}

	/**
	 * Triggered when one of the Facets to which ArmorProfFacet listens fires a
	 * DataFacetChangeEvent to indicate a ArmorProf was removed from a Player
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
		removeAll(dfce.getCharID(), dfce.getSource());
	}
}
