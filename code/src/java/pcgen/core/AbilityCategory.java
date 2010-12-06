/*
 * AbilityCategory.java
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
 * Copyright 2006 (C) Aaron Divinsky <boomer70@yahoo.com>
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
 *
 * Current Ver: $Revision$
 * Last Editor: $Author: $
 * Last Edited: $Date$
 */
package pcgen.core;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.Category;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.base.Loadable;
import pcgen.cdom.enumeration.DisplayLocation;
import pcgen.cdom.enumeration.Type;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.util.PropertyFactory;
import pcgen.util.enumeration.View;
import pcgen.util.enumeration.Visibility;

/**
 * This class stores and manages information about Ability categories.
 * 
 * <p>This is a higher level abstraction than the category specified by the 
 * ability object itself.  The low-level AbilityCategory defaults to the same
 * as this category key but this can be changed.  For example to specify an
 * <tt>AbilityCategory</tt> &quot;Fighter Bonus Feats&quot; you could specify
 * the AbilityCategory was &quot;FEAT&quot; and set the ability type to
 * &quot;Fighter&quot;. 
 * 
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.11.1
 */
public class AbilityCategory implements Category<Ability>, Loadable
{
	private URI sourceURI;

	private String keyName;
	private String displayName;
	private String pluralName;

	private CDOMSingleRef<AbilityCategory> parentCategory;
	private Set<CDOMSingleRef<Ability>> containedAbilities = null;
	private DisplayLocation displayLocation;
	private boolean isAllAbilityTypes = false;
	private Set<Type> types = null;
	private Formula poolFormula = FormulaFactory.ZERO;

	private Visibility visibility = Visibility.DEFAULT;
	private boolean isEditable = true;
	private boolean isPoolModifiable = true;
	private boolean isPoolFractional = false;
	private boolean isInternal = false;

	/** A constant used to refer to the &quot;Feat&quot; category. */
	public static final AbilityCategory FEAT = new AbilityCategory("FEAT", "in_feat"); //$NON-NLS-1$ //$NON-NLS-2$
	public static final AbilityCategory LANGBONUS = new AbilityCategory("*LANGBONUS"); //$NON-NLS-1$
	public static final AbilityCategory WEAPONBONUS = new AbilityCategory("*WEAPONBONUS"); //$NON-NLS-1$

	static
	{
		FEAT.pluralName = PropertyFactory.getString("in_feats"); //$NON-NLS-1$
		FEAT.displayLocation = DisplayLocation.getConstant(PropertyFactory.getString("in_feats")); //$NON-NLS-1$
		FEAT.setInternal(true);
		LANGBONUS.setPoolFormula(FormulaFactory.getFormulaFor("BONUSLANG"));
		LANGBONUS.setInternal(true);
		WEAPONBONUS.setInternal(true);
	}

	/**
	 * Constructs a new <tt>AbilityCategory</tt> with the specified key.
	 * 
	 * <p>This method sets the display and plural names to the same value as
	 * the key name.
	 * 
	 * @param aKeyName The name to use to reference this category.
	 */
	public AbilityCategory()
	{
		//For fooling other things
		keyName = "";
		//Self until proven otherwise
		parentCategory = CDOMDirectSingleRef.getRef(this);
	}
	
	/**
	 * Constructor takes a key name and display name for the category.
	 * 
	 * @param aKeyName The name to use to reference this category.
	 * @param aDisplayName The resource key to use for the display name
	 */
	public AbilityCategory(final String aKeyName, final String aDisplayName)
	{
		keyName = aKeyName;
		setName(aDisplayName);
		setPluralName(aDisplayName);

		parentCategory = CDOMDirectSingleRef.getRef(this);
		displayLocation = DisplayLocation.getConstant(aDisplayName);
	}

	/**
	 * Constructor takes a name for the category.
	 * 
	 * @param aKeyName The name to use to reference this category.
	 */
	public AbilityCategory(String aKeyName)
	{
		this(aKeyName, aKeyName);
	}

	/**
	 * Sets the parent AbilityCategory this category is part of.
	 * 
	 * @param category A Reference to an AbilityCategory.
	 */
	public void setAbilityCategory(CDOMSingleRef<AbilityCategory> category)
	{
		/*
		 * Note: This makes an assumption that keyName will not change. We
		 * should not enable a KEY token for AbilityCategory
		 */
		if (isInternal)
		{
			if (!category.getLSTformat(false).equals(this.getKeyName()))
			{
				throw new IllegalArgumentException(
						"Cannot set CATEGORY on an internal AbilityCategory");
			}
		}
		else
		{
			parentCategory = category;
		}
	}
	
	/**
	 * Gets the parent AbilityCategory this category is part of.
	 * 
	 * @return A reference to the AbilityCategory.
	 */
	public CDOMSingleRef<AbilityCategory> getAbilityCatRef()
	{
		return parentCategory;
	}

	/**
	 * Adds a new type to the list of types included in this category.
	 * 
	 * @param aType A type string.
	 */
	public void addAbilityType(final Type type)
	{
		if (types == null)
		{
			types = new TreeSet<Type>();
		}
		types.add(type);
	}
	
	/**
	 * Gets the <tt>Set</tt> of all the ability types to be included in this
	 * category.
	 * 
	 * @return An unmodifiable <tt>Set</tt> of type strings.
	 */
	public Set<Type> getTypes()
	{
		if (types == null)
		{
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(types);
	}
	
	/**
	 * Should all ability types be included in this category?
	 * @return true if all types should be included, 
	 *         false if only those listed should be.
	 */
	public boolean isAllAbilityTypes()
	{
		return isAllAbilityTypes;
	}

	/**
	 * Configure whether all ability types be included in this category?
	 * @param allAbilityTypes true if all types should be included, 
	 *         false if only those listed should be.
	 */
	public void setAllAbilityTypes(boolean allAbilityTypes)
	{
		this.isAllAbilityTypes = allAbilityTypes;
	}

	/**
	 * @param key the Ability Key to add to the set
	 */
	public void addAbilityKey(CDOMSingleRef<Ability> ref)
	{
		if ( containedAbilities == null )
		{
			containedAbilities = new HashSet<CDOMSingleRef<Ability>>();
		}
		containedAbilities.add(ref);
	}

	/**
	 * Gets the formula to use for calculating the base pool size for this
	 * category of ability.
	 * 
	 * @return A formula
	 */
	public Formula getPoolFormula()
	{
		return poolFormula;
	}
	
	/**
	 * Sets the formula to use to calculate the base pool size for this category
	 * of ability.
	 * 
	 * @param aFormula A valid formula or variable.
	 */
	public void setPoolFormula(Formula formula)
	{
		poolFormula = formula;
	}
	
	/**
	 * Sets the internationalized plural name for this category.
	 * 
	 * @param aName A plural name.
	 */
	public void setPluralName(final String aName)
	{
		pluralName = aName;
	}
	
	/**
	 * Returns an internationalized plural version of the category name.
	 * 
	 * @return The pluralized name
	 */
	public String getPluralName()
	{
		String name = pluralName;
		if (name == null)
		{
			name = displayName;
		}
		if (name.startsWith("in_"))
		{
			return PropertyFactory.getString(name);
		}
		else
		{
			return name;
		}
	}

	public String getRawPluralName()
	{
		return pluralName;
	}

	/**
	 * Returns the location on which the AbilityCategory should be displayed.
	 * 
	 * @return The display location.
	 */
	public DisplayLocation getDisplayLocation()
	{
		if (displayLocation == null)
		{
			displayLocation = DisplayLocation.getConstant(getPluralName());
		}
		return displayLocation;
	}

	/**
	 * Sets the location where the AbilityCategory should be displayed.
	 * 
	 * @param displayLocation
	 *            The new displayLocation
	 */
	public void setDisplayLocation(DisplayLocation location)
	{
		displayLocation = location;
	}

	/**
	 * Sets if abilities of this category should be displayed in the UI.
	 * 
	 * @param yesNo <tt>true</tt> if these abilities should be displayed.
	 */
	public void setVisible(Visibility visible)
	{
		visibility = visible;
	}
	
	/**
	 * Checks if this category of ability should be displayed in the UI.
	 * 
	 * @return <tt>true</tt> if these abilities should be displayed.
	 */
	public boolean isVisible()
	{
		return isVisible(null);
	}
	
	/**
	 * Checks if this category of ability should be displayed in the 
	 * UI for this PC.
	 * 
	 * @param pc The character to be tested.
	 * @return <tt>true</tt> if these abilities should be displayed.
	 */
	public boolean isVisible(PlayerCharacter pc)
	{
		if ((pc != null) && visibility.equals(Visibility.QUALIFY))
		{
			return pc.getTotalAbilityPool(this).floatValue() != 0.0
					|| !pc.getAggregateVisibleAbilityList(this).isEmpty();
		}
		return visibility.isVisibileTo(View.VISIBLE, false);
	}
	
	/**
	 * Sets if abilities in this category should be user-editable
	 * 
	 * @param yesNo <tt>true</tt> if the user should be able to add and remove
	 * abilities of this category.
	 */
	public void setEditable(final boolean yesNo)
	{
		isEditable = yesNo;
	}
	
	/**
	 * Checks if this category of abilities is user-editable.
	 * 
	 * @return <tt>true</tt> if these abilities are editable.
	 */
	public boolean isEditable()
	{
		return isEditable;
	}

	/**
	 * Sets the flag to allow/disallow user editing of the pool.
	 * 
	 * @param yesNo Set to <tt>true</tt> to allow user editing.
	 */
	public void setModPool(final boolean yesNo)
	{
		isPoolModifiable = yesNo;
	}
	
	/**
	 * Checks if this category allows user editing of the pool.
	 * 
	 * @return <tt>true</tt> to allow user editing.
	 */
	public boolean allowPoolMod()
	{
		return isPoolModifiable;
	}
	
	/**
	 * Sets if the pool can use fractional amounts.
	 * 
	 * @param yesNo <tt>true</tt> to allow fractions.
	 */
	public void setAllowFractionalPool(final boolean yesNo)
	{
		isPoolFractional = yesNo;
	}
	
	/**
	 * Checks if the pool should use whole numbers only.
	 * 
	 * @return <tt>true</tt> if fractional pool amounts are valid.
	 */
	public boolean allowFractionalPool()
	{
		return isPoolFractional;
	}
	
	// -------------------------------------------
	// KeyedObject Support
	// -------------------------------------------
	/**
	 * @see pcgen.core.KeyedObject#getDisplayName()
	 */
	public String getDisplayName()
	{
		if (displayName.startsWith("in_"))
		{
			return PropertyFactory.getString(displayName);
		}
		else
		{
			return displayName;
		}
	}

	public String getRawDisplayName()
	{
		return displayName;
	}

	/**
	 * @see pcgen.core.KeyedObject#getKeyName()
	 */
	public String getKeyName()
	{
		return keyName;
	}

	/**
	 * @see pcgen.core.KeyedObject#setKeyName(java.lang.String)
	 */
	public void setKeyName(final String aKey)
	{
		keyName = aKey;
	}

	/**
	 * @see pcgen.core.KeyedObject#setName(java.lang.String)
	 */
	public void setName(final String aName)
	{
		if ("".equals(keyName))
		{
			setKeyName(aName);
		}
		displayName = aName;
	}

	/**
	 * Returns the display name for this category.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getDisplayName();
	}

	/**
	 * Generates a hash code using the key, category and types.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((keyName == null) ? 0 : keyName.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbilityCategory other = (AbilityCategory) obj;
		if (keyName == null)
		{
			if (other.keyName != null)
				return false;
		}
		else if (!keyName.equals(other.keyName))
			return false;
		return true;
	}

	public Category<Ability> getParentCategory()
	{
		return parentCategory.resolvesTo();
	}

	public boolean containsDirectly(Ability ability)
	{
		if ( containedAbilities == null )
		{
			return false;
		}
		for (CDOMSingleRef<Ability> ref : containedAbilities)
		{
			if (ref.contains(ability))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the collection of references for abilities that will be directly
	 * included in the category.
	 * 
	 * @return the collection of references
	 */
	public Collection<CDOMSingleRef<Ability>> getAbilityRefs()
	{
		if (containedAbilities == null)
		{
			return Collections.emptySet();
		}
		return Collections.unmodifiableCollection(containedAbilities);
	}

	public boolean hasDirectReferences()
	{
		return (containedAbilities != null) && !containedAbilities.isEmpty();
	}

	public Visibility getVisibility()
	{
		return visibility;
	}

	public URI getSourceURI()
	{
		return sourceURI;
	}

	public void setSourceURI(URI source)
	{
		sourceURI = source;
	}

	public String getLSTformat()
	{
		return getKeyName();
	}

	public void setInternal(boolean internal)
	{
		isInternal = internal;
	}

	public boolean isInternal()
	{
		return isInternal;
	}

	public boolean isType(String type)
	{
		return false;
	}

	public String getParentCategoryName()
	{
		return parentCategory.getLSTformat(false);
	}
}
