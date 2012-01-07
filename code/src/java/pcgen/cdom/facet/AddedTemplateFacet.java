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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Globals;
import pcgen.core.PCTemplate;

/**
 * AddedTemplateFacet is a Facet that tracks the Templates that have been added
 * to a Player Character.
 */
public class AddedTemplateFacet extends AbstractSourcedListFacet<PCTemplate>
{

	private PrerequisiteFacet prereqFacet = FacetLibrary
			.getFacet(PrerequisiteFacet.class);

	public Collection<PCTemplate> select(CharID id, CDOMObject po,
			boolean isImporting)
	{
		List<PCTemplate> list = new ArrayList<PCTemplate>();
		// older version of this cleared the
		// templateAdded list, so this may have to do that as well?
		FacetCache.remove(id, getClass());
		if (!isImporting)
		{
			for (CDOMReference<PCTemplate> ref : po
					.getSafeListFor(ListKey.TEMPLATE))
			{
				for (PCTemplate pct : ref.getContainedObjects())
				{
					add(id, pct, po);
					list.add(pct);
				}
			}
			List<PCTemplate> added = new ArrayList<PCTemplate>();
			for (CDOMReference<PCTemplate> ref : po
					.getSafeListFor(ListKey.TEMPLATE_ADDCHOICE))
			{
				added.addAll(ref.getContainedObjects());
			}
			for (CDOMReference<PCTemplate> ref : po
					.getSafeListFor(ListKey.TEMPLATE_CHOOSE))
			{
				List<PCTemplate> chooseList = new ArrayList<PCTemplate>(added);
				chooseList.addAll(ref.getContainedObjects());
				PCTemplate selected = chooseTemplate(po, chooseList, true, id);
				if (selected != null)
				{
					add(id, selected, po);
					list.add(selected);
				}
			}
		}
		return list;
	}

	public Collection<PCTemplate> remove(CharID id, CDOMObject po,
			boolean isImporting)
	{
		List<PCTemplate> list = new ArrayList<PCTemplate>();
		if (!isImporting)
		{
			for (CDOMReference<PCTemplate> ref : po
					.getSafeListFor(ListKey.REMOVE_TEMPLATES))
			{
				for (PCTemplate pct : ref.getContainedObjects())
				{
					list.add(pct);
				}
			}
		}
		return list;
	}

	public PCTemplate chooseTemplate(CDOMObject anOwner, List<PCTemplate> list,
			boolean forceChoice, CharID id)
	{
		final List<PCTemplate> availableList = new ArrayList<PCTemplate>();
		for (PCTemplate pct : list)
		{
			if (prereqFacet.qualifies(id, pct, anOwner))
			{
				availableList.add(pct);
			}
		}
		if (availableList.size() == 1)
		{
			return availableList.get(0);
		}
		// If we are left without a choice, don't show the chooser.
		if (availableList.size() < 1)
		{
			return null;
		}
		final List<PCTemplate> selectedList = new ArrayList<PCTemplate>(1);
		String title = "Template Choice";
		if (anOwner != null)
		{
			title += " (" + anOwner.getDisplayName() + ")";
		}
		Globals.getChoiceFromList(title, availableList, selectedList, 1,
				forceChoice);
		if (selectedList.size() == 1)
		{
			return selectedList.get(0);
		}

		return null;
	}

	public Collection<PCTemplate> getFromSource(CharID id, CDOMObject cdo)
	{
		List<PCTemplate> list = new ArrayList<PCTemplate>();
		Map<PCTemplate, Set<Object>> map = getCachedMap(id);
		if (map != null)
		{
			for (Map.Entry<PCTemplate, Set<Object>> me : map.entrySet())
			{
				Set<Object> sourceSet = me.getValue();
				if (sourceSet.contains(cdo))
				{
					list.add(me.getKey());
				}
			}
		}
		return list;
	}
}
