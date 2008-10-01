/*
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on April 21, 2001, 2:15 PM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package pcgen.core;

import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import pcgen.base.lang.StringUtil;
import pcgen.base.util.DoubleKeyMap;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.TransitionChoice;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.analysis.LanguageSupport;
import pcgen.core.analysis.OutputNameFormatting;
import pcgen.core.analysis.WeaponProfType;
import pcgen.core.bonus.Bonus;
import pcgen.core.bonus.BonusObj;
import pcgen.core.bonus.BonusUtilities;
import pcgen.core.chooser.ChooserUtilities;
import pcgen.core.levelability.LevelAbility;
import pcgen.core.pclevelinfo.PCLevelInfo;
import pcgen.core.prereq.PrereqHandler;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.utils.KeyedListContainer;
import pcgen.core.utils.MapKey;
import pcgen.core.utils.MapKeyMapToList;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.output.prereq.PrerequisiteWriter;
import pcgen.persistence.lst.prereq.PreParserFactory;
import pcgen.util.Logging;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.chooser.ChooserInterface;

/**
 * <code>PObject</code><br>
 * This is the base class for several objects in the PCGen database.
 *
 * @author Bryan McRoberts <merton_monk@users.sourceforge.net>
 * @version $Revision$
 */
/**
 * @author Joe.Frazier
 *
 */
public class PObject extends CDOMObject implements Cloneable, Serializable, Comparable<Object>,
	SourcedObject, KeyedListContainer, KeyedObject
{
	/** Standard serialVersionUID for Serializable objects */
	private static final long serialVersionUID = 1;

	/** a boolean for whether something should recurse, default is false */
	private static boolean dontRecurse = false;

	/** A map of Lists for the object */
	protected final MapKeyMapToList mapListChar = new MapKeyMapToList();

	/** List of Level Abilities for the object  */
	private List<LevelAbility> levelAbilityList = null;

	private SourceEntry theSource = new SourceEntry();

	/** The Non-internationalized name to use to refer to this object. */
	protected String keyName = Constants.EMPTY_STRING;
	/** The name to display to the user.  This should be internationalized. */
	protected String displayName = Constants.EMPTY_STRING;

	/** List of Bonuses for the object */
	private List<BonusObj> bonusList = new ArrayList<BonusObj>();

	private SpellSupport spellSupport = new SpellSupport();
	
	private boolean isNewItem = true;

	private String chooseLanguageAutos = Constants.EMPTY_STRING;

	private HashMap<String,List<String>> servesAsList =null;
	
	private URI sourceURI = null;
	
	private Set<String> types = new LinkedHashSet<String>();
	
	private final Class<?> myClass = getClass();
	
	/* ************
	 * Methods
	 * ************/
	/**
	 * Adds entry to the CSkill list
	 * @param entry skill to add
	 */
	public final void addCcSkill(String entry)
	{
		if (entry.startsWith(".CLEAR"))
		{
			if (".CLEAR".equals(entry))
			{
				clearCcSkills();
			}
			else
			{
				if (entry.startsWith(".CLEAR"))
				{
					entry = entry.substring(7);
				}

				if (entry.startsWith("TYPE.") || entry.startsWith("TYPE="))
				{
					final String typeString = entry.substring(5);

					for ( Skill skill : Globals.getContext().ref.getConstructedCDOMObjects(Skill.class) )
					{
						boolean toClear = true;
						final StringTokenizer cTok = new StringTokenizer(typeString, ".");

						while (cTok.hasMoreTokens() && toClear)
						{
							if (!skill.isType(cTok.nextToken()))
							{
								toClear = false;
							}
						}

						if (toClear)
						{
							removeFromListFor(ListKey.CROSS_CLASS_SKILLS, skill.getKeyName());
						}
					}
				}
				else
				{
					removeFromListFor(ListKey.CROSS_CLASS_SKILLS, entry);
				}
			}
		}
		else if (entry.startsWith("TYPE.") || entry.startsWith("TYPE="))
		{
			for (Skill skill : Globals.getContext().ref.getConstructedCDOMObjects(Skill.class))
			{
				if (skill.isType(entry.substring(5)))
				{
					addToListFor(ListKey.CROSS_CLASS_SKILLS, skill.getKeyName());
				}
			}
		}
		else if ("ALL".equals(entry))
		{
			for (Skill skill : Globals.getContext().ref.getConstructedCDOMObjects(Skill.class))
			{
				addToListFor(ListKey.CROSS_CLASS_SKILLS, skill.getKeyName());
			}
		}
		else
		{
			addToListFor(ListKey.CROSS_CLASS_SKILLS, entry);
		}
	}

	/**
	 * Adds all of the entries to the CSkills list
	 * @param entries list of entries
	 */
	public final void addAllCcSkills(final List<String> entries)
	{
		addAllToListFor(ListKey.CROSS_CLASS_SKILLS, entries);
	}

	/**
	 * Clears the class skill list
	 */
	public void clearCcSkills()
	{
		removeListFor(ListKey.CROSS_CLASS_SKILLS);
	}

	/**
	 * Get the list of class skills for this object
	 * @return the list of class skills for this object
	 */
	public final List<String> getCcSkillList()
	{
		return getListFor(ListKey.CROSS_CLASS_SKILLS);
	}

	/**
	 * Set the description of this object
	 * @param a
	 */
	public final void setDescription(final String a)
	{
		put(StringKey.DESCRIPTION, a);
	}

	public final String getDescription()
	{
		String characteristic = get(StringKey.DESCRIPTION);
		return characteristic == null ? Constants.EMPTY_STRING : characteristic;
	}
	
	/**
	 * Get the level ability list for this object
	 * @return the level ability list for this object
	 */
	public final List<LevelAbility> getLevelAbilityList()
	{
		return levelAbilityList;
	}

	/**
	 * Adds entry to the CSkill list
	 * @param entry skill to add
	 */
	public final void addCSkill(String entry)
	{
		if (entry.startsWith(".CLEAR"))
		{
			if (".CLEAR".equals(entry))
			{
				clearCSkills();
			}
			else
			{
				if (entry.startsWith(".CLEAR"))
				{
					entry = entry.substring(7);
				}

				if (entry.startsWith("TYPE.") || entry.startsWith("TYPE="))
				{
					final String typeString = entry.substring(5);

					for (Skill skill : Globals.getContext().ref.getConstructedCDOMObjects(Skill.class))
					{
						boolean toClear = true;
						final StringTokenizer cTok = new StringTokenizer(typeString, ".");

						while (cTok.hasMoreTokens() && toClear)
						{
							if (!skill.isType(cTok.nextToken()))
							{
								toClear = false;
							}
						}

						if (toClear)
						{
							removeFromListFor(ListKey.CLASS_SKILLS, skill.getKeyName());
						}
					}
				}
				else
				{
					removeFromListFor(ListKey.CLASS_SKILLS, entry);
				}
			}
		}
		else if (entry.startsWith("TYPE.") || entry.startsWith("TYPE="))
		{
			for (Skill skill : Globals.getContext().ref.getConstructedCDOMObjects(Skill.class))
			{
				if (skill.isType(entry.substring(5)))
				{
					addToListFor(ListKey.CLASS_SKILLS, skill.getKeyName());
				}
			}
		}
		else if ("ALL".equals(entry))
		{
			for (Skill skill : Globals.getContext().ref.getConstructedCDOMObjects(Skill.class))
			{
				addToListFor(ListKey.CLASS_SKILLS, skill.getKeyName());
			}
		}
		else
		{
			addToListFor(ListKey.CLASS_SKILLS, entry);
		}
	}

	/**
	 * Adds all of the entries to the CSkills list
	 * @param entries list of entries
	 */
	public final void addAllCSkills(final List<String> entries)
	{
		addAllToListFor(ListKey.CLASS_SKILLS, entries);
	}

	/**
	 * Clears the class skill list
	 */
	public void clearCSkills()
	{
		removeListFor(ListKey.CLASS_SKILLS);
	}

	/**
	 * Get the list of class skills for this object
	 * @return the list of class skills for this object
	 */
	public final List<String> getCSkillList()
	{
		return getListFor(ListKey.CLASS_SKILLS);
	}

	/**
	 * Sets the natural weapon equipment items list for this object
	 * @param aList
	 */
	public void setNaturalWeapons(final List<Equipment> aList)
	{
		removeListFor(ListKey.NATURAL_WEAPONS);
		addAllToListFor(ListKey.NATURAL_WEAPONS, aList);
	}

	/**
	 * Get the natural weapons list for this object
	 * @return the natural weapons list for this object
	 */
	public List<Equipment> getNaturalWeapons()
	{
		return getSafeListFor(ListKey.NATURAL_WEAPONS);
	}

	/**
	 * Get the list of temporary bonuses for this list
	 * @return the list of temporary bonuses for this list
	 */
	public List<BonusObj> getTempBonusList()
	{
		return getSafeListFor(ListKey.TEMP_BONUS);
	}

	/**
	 * Get the temporary description of this object
	 * @return the temporary description of this object
	 */
	public final String getTempDescription()
	{
		String characteristic = get(StringKey.TEMP_DESCRIPTION);
		return characteristic == null ? Constants.EMPTY_STRING : characteristic;
	}

	/**
	 * Add to the 'save' for the character list
	 * @param aString
	 */
	public final void addSave(final String aString)
	{
		addToListFor(ListKey.SAVE, aString);
	}

	/**
	 * Add the selected wepaon prof bonus to the character list
	 * @param entry
	 */
	public final void addSelectedWeaponProfBonus(final String entry)
	{
		addToListFor(ListKey.SELECTED_WEAPON_PROF_BONUS, entry);
	}

	/**
	 * Add to the list of temporary bonuses
	 * @param aBonus
	 */
	public void addTempBonus(final BonusObj aBonus)
	{
		addToListFor(ListKey.TEMP_BONUS, aBonus);
	}

	/**
	 * Remove from the list of temporary bonuses
	 * @param aBonus
	 */
	public void removeTempBonus(final BonusObj aBonus)
	{
		removeFromListFor(ListKey.TEMP_BONUS, aBonus);
	}

	/**
	 * Add to the unarmed damage list (or clear the whole list)
	 * @param addString
	 */
	public final void clearUdamList()
	{
		removeListFor(ListKey.UDAM);
	}

	/**
	 * Add to the unarmed damage list (or clear the whole list)
	 * @param addString
	 */
	public final void setUdamItem(String addString, int loc)
	{
		listChar.addToListAt(ListKey.UDAM, addString, loc);
	}

	/**
	 * Add a virtual feat to the character list
	 * @param aFeat
	 */
	public final void addVirtualFeat(final Ability aFeat)
	{
		addAbility(AbilityCategory.FEAT, Ability.Nature.VIRTUAL,
			new QualifiedObject<String>(aFeat.getKeyName()));
	}
	
	/**
	 * Add a list of virtual feats to the character list
	 * @param aFeatList
	 */
	public final void addVirtualFeats(final List<Ability> aFeatList)
	{
		for (Ability aFeat : aFeatList)
		{
			addAbility(AbilityCategory.FEAT, Ability.Nature.VIRTUAL,
				new QualifiedObject<String>(aFeat.getKeyName()));
		}
	}
	
	/**
	 * Add a natural weapon to the character list.
	 *
	 * @param weapon
	 * @param level
	 */
	public void addNaturalWeapon(final Equipment weapon, final int level)
	{
		addToListFor(ListKey.NATURAL_WEAPONS, weapon);
	}

	/**
	 * if a class implements the Cloneable interface then it should have a
	 * public" 'clone ()' method It should be declared to throw
	 * CloneNotSupportedException', but subclasses do not need the "throws"
	 * declaration unless their 'clone ()' method will throw the exception
	 * Thus subclasses can decide to not support 'Cloneable' by implementing
	 * the 'clone ()' method to throw 'CloneNotSupportedException'
	 * If this rule were ignored and the parent did not have the "throws"
	 * declaration then subclasses that should not be cloned would be forced
	 * to implement a trivial 'clone ()' to satisfy inheritance
	 * final" classes implementing 'Cloneable' should not be declared to
	 * throw 'CloneNotSupportedException" because their implementation of
	 * clone ()' should be a fully functional method that will not
	 * throw the exception.
	 * @return cloned object
	 * @throws CloneNotSupportedException
	 */
	@Override
	public PObject clone() throws CloneNotSupportedException
	{
		final PObject retVal = (PObject) super.clone();
		retVal.types = new LinkedHashSet<String>();
		retVal.types.addAll(types);

		retVal.setName(displayName);
		retVal.setKeyName(keyName);
		retVal.spellSupport = spellSupport.clone();

		// added 04 Aug 2003 by sage_sam -- bug#765749
		// need to copy map correctly during a clone
//		if (sourceMap != null)
//		{
//			retVal.sourceMap = new HashMap<String, String>();
//			retVal.sourceMap.putAll(this.sourceMap);
//		}
		retVal.theSource = theSource.clone();

		if (bonusList != null)
		{
			retVal.bonusList = new ArrayList<BonusObj>();
			for (BonusObj orig : bonusList)
			{
				retVal.bonusList.add(orig.clone());

			}
			retVal.ownBonuses();
		}

		if ((levelAbilityList != null) && !levelAbilityList.isEmpty())
		{
			retVal.levelAbilityList = new ArrayList<LevelAbility>();

			for ( LevelAbility ab : levelAbilityList )
			{
				ab = (LevelAbility) ab.clone();
				ab.setOwner(retVal);
				retVal.levelAbilityList.add(ab);
			}
		}
		return retVal;
	}

	/**
	 * Compares the keys of the object.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Object obj)
	{
		if (obj != null)
		{
			//this should throw a ClassCastException for non-PObjects, like the Comparable interface calls for
			return this.getKeyName().compareToIgnoreCase(((PObject) obj).getKeyName());
		}
		return 1;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( final Object obj )
	{
		if ( obj == null )
		{
			return false;
		}
		final String thisKey;
		final String otherKey;
		if ( obj instanceof PObject )
		{
			thisKey = getKeyName();
			otherKey = ((PObject)obj).getKeyName();
		}
		else
		{
			thisKey = toString();
			otherKey = obj.toString();
		}
		return thisKey.equalsIgnoreCase( otherKey );
	}

	/**
	 * Set the CHOICE string
	 * @param aString
	 */
	public void setChoiceString(final String aString)
	{
		put(StringKey.CHOICE_STRING, aString);
	}

	/**
	 * Get the CHOICE string
	 * @return the CHOICE string
	 */
	public final String getChoiceString()
	{
		String characteristic = get(StringKey.CHOICE_STRING);
		return characteristic == null ? Constants.EMPTY_STRING : characteristic;
	}

	/**
	 * Get the choices for this PC
	 * @param aChoice
	 * @param aPC
	 */
	public final void getChoices(
			final String          aChoice,
			final PlayerCharacter aPC)
	{
		final List availableList = new ArrayList();
		final List selectedList  = new ArrayList();
		ChooserUtilities.getChoices(this, aChoice, availableList, selectedList, aPC);
	}

	/**
	 * Set the Key Name
	 * @param aString
	 */
	public void setKeyName(final String aString)
	{
		keyName = aString;
	}

	/**
	 * Get the Key Name
	 * @return Key Name
	 */
	public final String getKeyName()
	{
		return keyName;
	}

	/**
	 * Return the qualified key, ususally used as the source in a 
	 * getVariableValue call. Always returns an empty string, but 
	 * may be overridden by subclasses to return a required value.
	 * 
	 * @return The qualified name of the object
	 */
	public String getQualifiedKey()
	{
		return Constants.EMPTY_STRING;
	}
	
	/**
	 * Set the name (sets keyname also)
	 * @param aString
	 */
	public void setName(final String aString)
	{
		if (!aString.endsWith(".MOD"))
		{
			fireNameChanged(displayName, aString);
			displayName = aString;
			this.setKeyName(aString);
		}
	}

	/**
	 * This method sets only the name not the key.
	 * @param aName Name to use for display
	 */
	public void setDisplayName( final String aName )
	{
		displayName = aName;
	}
	
	/**
	 * Get name
	 * @return name
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Set the item as new flag
	 * @param newItem
	 */
	public final void setNewItem(final boolean newItem)
	{
		this.isNewItem = newItem;
	}

	///////////////////////////////////////////////////////////////////////
	// Accessor(s) and Mutator(s)
	///////////////////////////////////////////////////////////////////////

	/**
	 * Returns true if the item is new
	 * @return true if the item is new
	 */
	public final boolean isNewItem()
	{
		return isNewItem;
	}

	/**
	 * Get the output name of the item
	 * @return the output name of the item
	 */
	public final String getOutputName()
	{
		String outputName = get(StringKey.OUTPUT_NAME);
		// if no OutputName has been defined, just return the regular name
		if (outputName == null)
		{
			return displayName;
		}
		else if (outputName.equalsIgnoreCase("[BASE]") && displayName.indexOf('(') != -1)
		{
			outputName = displayName.substring(0, displayName.indexOf('(')).trim();
		}
		if (outputName.indexOf("[NAME]") >= 0)
		{
			outputName = outputName.replaceAll("\\[NAME\\]",
					getPreFormatedOutputName());
		}
		return outputName;
	}

	/**
	 * Set the source file for this object
	 * @param sourceFile
	 */
	public final void setSourceURI(URI source)
	{
		sourceURI = source;
	}

	/**
	 * Get the source file for this object
	 * @return the source file for this object
	 */
	public final URI getSourceURI()
	{
		return sourceURI;
	}

	/**
	 * Set the source from a map of source values.
	 * 
	 * <p>The map has the form "source type" ==> "source value".  For example,
	 * <code>"SHORT" ==> "RSRD"</code>.
	 * 
	 * @param arg A <tt>Map</tt> containing source values. 
	 * @throws ParseException If the source date cannot be parsed.
	 * 
	 * @see pcgen.core.SourceEntry#setFromMap(Map)
	 */
	public final void setSourceMap(final Map<String, String> arg) 
		throws ParseException
	{
		theSource.setFromMap( arg );
	}

	/**
	 * Returns the source entry for this object.
	 * 
	 * @return a <tt>SourceEntry</tt>
	 * 
	 * @see pcgen.core.SourceEntry
	 */
	public SourceEntry getSourceEntry()
	{
		if ( theSource == null )
		{
			return new SourceEntry();
		}
		return theSource;
	}
	
	/**
	 * Sets the source entry for this object.
	 * 
	 * @param aSource A <tt>SourceEntry</tt> to set.
	 */
	public void setSource( final SourceEntry aSource )
	{
		theSource = aSource;
	}
	
	/**
	 * Gets the Source string for this object using the default source display
	 * mode.
	 * 
	 * @return The Source string.
	 */
	public String getDefaultSourceString()
	{
		return theSource.toString();

	}
	
	/**
	 * Returns a hardcoded "POBJECT|" + name of this object
	 * @return "POBJECT|" + name of this object
	 */
	public String getSpellKey()
	{
		return "POBJECT|" + getKeyName(); //$NON-NLS-1$
	}

	/**
	 * Get the user defined type by index
	 * @param i
	 * @return the user defined type by index
	 */
	public String getMyType(final int i)
	{
		if (i < getMyTypeCount())
		{
			//Yes, this in inefficient... it's done rarely enough it's ok for now
			//Best performance improvement to offset this would be to make Type
			// in campaigns NOT order sensitive...
			return new ArrayList<String>(types).get(i);
		}

		return null;
	}

	/**
	 * Get the number of user defined types
	 * @return the number of user defined types
	 */
	public int getMyTypeCount()
	{
		return types.size();
	}

	/**
	 * This method gets access to the spell list.
	 * @return List
	 */
	public List<PCSpell> getSpellList()
	{
		return spellSupport.getSpellList(-1);
	}

	public void addSAB(SpecialAbility sa, int level)
	{
		mapListChar.addToListFor(MapKey.SAB, level, sa);
	}

	public void clearSABList(int level)
	{
		mapListChar.removeListFor(MapKey.SAB, level);
	}
	
	public void clearAllSABLists()
	{
		mapListChar.removeListsFor(MapKey.SAB);
	}
	
	public void removeSAB(String s, int level)
	{
		List<SpecialAbility> sabs = mapListChar.getListFor(MapKey.SAB, level);
		if (sabs != null)
		{
			for (SpecialAbility sa : sabs)
			{
				if (sa.getDisplayName().equals(s)
					|| sa.getDisplayName().startsWith(s + "|"))
				{
					mapListChar.removeFromListFor(MapKey.SAB, level, sa);
				}
			}
		}
	}

	public void addSABToList(List<SpecialAbility> saList, PlayerCharacter pc)
	{
		for (Integer lvl : mapListChar.getSecondaryKeySet(MapKey.SAB))
		{
			List<SpecialAbility> sabs = mapListChar.getListFor(MapKey.SAB, lvl);
			if (sabs != null)
			{
				for (SpecialAbility sa : sabs)
				{
					if (pc == null || sa.qualifies(pc))
					{
						final String key = sa.getKeyName();
						final int idx = key.indexOf("%CHOICE");
						
						if (idx >= 0)
						{
							StringBuilder sb = new StringBuilder();
							sb.append(key.substring(0, idx));

							if (pc.hasAssociations(this))
							{
								sb.append(StringUtil.joinToStringBuffer(pc.getAssociationList(this), ", "));
							}
							else
							{
								sb.append("<undefined>");
							}

							sb.append(key.substring(idx + 7));
							sa = new SpecialAbility(sb.toString(), sa
									.getSASource(), sa.getSADesc());
							saList.add(sa);
						}
						else
						{
							saList.add(sa);
						}
					}
				}
			}
		}
	}

	/**
	 * Get the type of PObject
	 * 
	 * @return the type of PObject
	 */
	public String getType()
	{
		return StringUtil.join(getTypeList(false), ".");
	}

	public List<String> getTypeList(final boolean visibleOnly)
	{
		final List<String> ret = new ArrayList<String>(types);
		if (visibleOnly )
		{
			for ( String type : types )
			{
				if ( SettingsHandler.getGame().isTypeHidden( myClass, type) )
				{
					ret.remove(type);
				}
			}
		}
		return Collections.unmodifiableList(ret);
	}
	
	/**
	 * If aType begins with an &#34; (Exclamation Mark) the &#34; will be
	 * removed before checking the type.
	 *
	 * @param aType
	 * @return Whether the item is of this type
	 */
	public boolean isType(final String aType)
	{
		final String myType;

		if ((aType.length() > 0) && (aType.charAt(0) == '!'))
		{
			myType = aType.substring(1).toUpperCase();
		}
		else
		{
			myType = aType.toUpperCase();
		}

		return types.contains(myType);
	}

	/**
	 * Deal with the type, whether to ADD, REMOVE it etc.
	 * @param aString
	 */
	public void setTypeInfo(final String aString)
	{
		boolean bRemove = false;
		final StringTokenizer aTok = new StringTokenizer(aString.toUpperCase().trim(), ".");

		while (aTok.hasMoreTokens())
		{
			final String aType = aTok.nextToken();

			if (bRemove)
			{
				removeMyType(aType);
				bRemove = false;
			}
			else if ("ADD".equals(aType))
			{
				bRemove = false;
			}
			else if ("REMOVE".equals(aType))
			{
				bRemove = true;
			}
			else if ("CLEAR".equals(aType))
			{
				clearMyType();
			}
			else if (!types.contains(aType))
			{
				doGlobalTypeUpdate(aType);
				addMyType(aType);
			}
		}
	}

	/**
	 * Remove the save
	 * @param bonusString
	 */
	public final void removeSave(final String bonusString)
	{
		boolean b = removeFromListFor(ListKey.SAVE, bonusString);
		if (!b) {
			Logging.errorPrint("removeSave: Could not find bonus: " + bonusString + " in saveList.");
		}
	}

	/**
	 * Remove user defined types
	 * @param aString
	 */
	public final void removeType(final String aString)
	{
		final String typeString = aString.toUpperCase().trim();
		final StringTokenizer aTok = new StringTokenizer(typeString, ".");

		while (aTok.hasMoreTokens())
		{
			final String aType = aTok.nextToken();
			removeMyType(aType);
		}
	}

	/**
	 * Reset (Clear) the temporary bonus list
	 */
	public void resetTempBonusList()
	{
		removeListFor(ListKey.TEMP_BONUS);
	}

	/**
	 * Set the region string
	 * @param arg
	 */
	public final void setRegionString(final String arg)
	{
		put(StringKey.REGION, arg);
	}

	/**
	 * Get the region string
	 * @return the region string
	 */
	public final String getRegionString()
	{
		return get(StringKey.REGION);
	}

	/**
	 * Set the remove list for the character list
	 * @param arg
	 */
	public final void setRemoveString(final String arg)
	{
		if (arg.equals(".CLEAR"))
		{
			removeListFor(ListKey.REMOVE_STRING_LIST);
		}
		else
		{
			addToListFor(ListKey.REMOVE_STRING_LIST, arg);
		}
	}

	/**
	 * Check the removals list
	 * @param aPC
	 */
	public void checkRemovals(PlayerCharacter aPC)
	{
		// only feat removal is supported atm
		if (!containsListFor(ListKey.REMOVE_STRING_LIST))
			return;
		for (Iterator<String> ri = getListFor(ListKey.REMOVE_STRING_LIST).iterator(); ri.hasNext();)
		{
			checkRemoval(aPC, ri.next());
		}
	}

	/**
	 * Check the removal of x from a PC, only supports Feats at the moment
	 * @param aPC
	 * @param removeString
	 */
	private void checkRemoval(PlayerCharacter aPC, String removeString)
	{
		String remString = removeString.substring(removeString.indexOf("|")+1);
		if (this instanceof PCClass)
		{
			int lev = Integer.parseInt(removeString.substring(0, removeString.indexOf("|")));
			PCClass aClass = (PCClass)this;
			if (aClass.getLevel() != lev)
				return;
		}
		if (!remString.startsWith("FEAT("))
			return;
		int i = remString.indexOf("(");
		int k = remString.lastIndexOf(")");
		final StringTokenizer aTok = new StringTokenizer(remString.substring(i+1,k),"(),", false);
		if (aTok.countTokens() == 0)
			return; // nothing to do?
		List<Ability> theFeatList = new ArrayList<Ability>(); // don't remove virtual or mfeats
		while (aTok.hasMoreTokens())
		{
			final String arg = aTok.nextToken();
			// could be a TYPE of feat
			if (arg.startsWith("TYPE."))
			{
				final String theType = arg.substring(5);
				for (Ability aFeat : aPC.getRealAbilitiesList(AbilityCategory.FEAT))
				{
					if (aFeat.isType(theType) && !theFeatList.contains(aFeat))
						theFeatList.add(aFeat);
				}
			}
			else if (arg.startsWith("CLASS."))
			{
				PCClass aClass = aPC.getClassKeyed(arg.substring(6));
				if (aClass != null)
				{
					for (PCLevelInfo element : aPC.getLevelInfo())
					{
						if (element.getClassKeyName().equalsIgnoreCase(aClass.getKeyName()))
						{
							for (Ability aFeat : (List<Ability>)element.getObjects())
							{
								if (!theFeatList.contains(aFeat))
									theFeatList.add(aFeat);
							}
						}
					}

				}
			}
			else if (arg.equals("CHOICE"))
			{
				for (Ability aFeat : aPC.getRealAbilitiesList(AbilityCategory.FEAT))
				{
					theFeatList.add(aFeat);
				}
			}
			// or it's a specifically named feat
			else
			{
				Ability aFeat = aPC.getFeatNamed(arg);
				if (aFeat != null && !theFeatList.contains(aFeat))
					theFeatList.add(aFeat);
			}
		}
		int remCount = theFeatList.size();
		if (remString.length() > k + 1)
		{
			final String rString = remString.substring(k+1);
			if (!rString.equalsIgnoreCase("ALL"))
				remCount = Integer.parseInt(rString);
		}
		if (remCount != theFeatList.size() && theFeatList.size()>0)
		{
			final ChooserInterface chooser = ChooserFactory.getChooserInstance();
			chooser.setPoolFlag(true); // user is not required to make any changes
			chooser.setAllowsDups(false); // only stackable feats can be duped
			chooser.setVisible(false);
			chooser.setTotalChoicesAvail(remCount);

			String title = "Select for removal";
			chooser.setTitle(title);
			ArrayList selectedList = new ArrayList();
			Globals.sortChooserLists(theFeatList, selectedList);

			for (; ;)
			{
				chooser.setAvailableList(theFeatList);
				chooser.setSelectedList(selectedList);
				chooser.setVisible(true);

				final int selectedSize = chooser.getSelectedList().size();

				if (remCount > 0)
				{
					if (selectedSize != remCount)
					{
						ShowMessageDelegate.showMessageDialog("You must make " + (remCount - selectedSize) + " more selection(s).",
							Constants.s_APPNAME, MessageType.INFORMATION);

						continue;
					}
				}

				break;
			}
			for (int ci = 0; ci < chooser.getSelectedList().size(); ci++)
			{
				final String chosenItem = (String) chooser.getSelectedList().get(ci);
				final Ability anAbility = Globals.getAbilityKeyed("FEAT", chosenItem);
//				if (anAbility == null)
//				{
//					aPC.adjustFeats(-1);
//					Logging.debugPrint("There is no feat '" + chosenItem + "'. Adjusting feat count by -1");
//				}
//				else
//				{
//					aPC.adjustFeats(-anAbility.getCost());
//				}

				AbilityUtilities.modFeat(aPC, null, chosenItem, false, false);
			}
		}
		else if (remCount == theFeatList.size())
		{
			for (int ix = theFeatList.size() - 1; ix >= 0; ix--)
			{
				Ability aFeat = theFeatList.get(ix);
				aPC.adjustFeats(-aFeat.getSafe(ObjectKey.SELECTION_COST).doubleValue());
				AbilityUtilities.modFeat(aPC, null, aFeat.getKeyName(), false, false);
				if (ix > theFeatList.size())
					ix = theFeatList.size();
			}
		}
	}

	/**
	 * Add auto array
	 * @param arg
	 */
	public final void addAutoArray(String arrayName, String item)
	{
		mapListChar.addToListFor(MapKey.AUTO_ARRAY, arrayName, item);
	}

	/**
	 * Add the select armor proficiencies to the list
	 * @param aList
	 */
	public final void addSelectedArmorProfs(final List<String> aList)
	{
		//This can't do a direct addAll on listChar because this does duplication removal
		for (String aString : aList)
		{
			if (!containsInList(ListKey.SELECTED_ARMOR_PROF, aString))
			{
				addToListFor(ListKey.SELECTED_ARMOR_PROF, aString);
			}
		}
	}

	/**
	 * Clear the auto list
	 */
	public final void clearAutoMap()
	{
		mapListChar.removeListsFor(MapKey.AUTO_ARRAY);
	}

	/**
	 * This does a partial clear of the auto list, removing any entries
	 * carrying the supplied tag
	 * @param tag The type to be removed e.g. WEAPONPROF
	 */
	public final void clearAutoTag(String tag)
	{
		mapListChar.removeListFor(MapKey.AUTO_ARRAY, tag);
	}

	public final Set<String> getAutoMapKeys()
	{
		return mapListChar.getSecondaryKeySet(MapKey.AUTO_ARRAY);
	}
	
	public final List<String> getAuto(String tag)
	{
		return mapListChar.getListFor(MapKey.AUTO_ARRAY, tag);
	}
	
	/**
	 * Set the campaign source
	 * @param arg
	 */
	public void setSourceCampaign(final Campaign arg)
	{
		theSource.getSourceBook().setCampaign( arg );
	}

	/**
	 * This method returns a reference to the Campaign that this object
	 * originated from
	 *
	 * @return Campaign instance referencing the file containing the
	 *         source for this object
	 */
	public Campaign getSourceCampaign()
	{
		return theSource.getSourceBook().getCampaign();
	}

	/**
	 * gets the bonuses to a stat based on the stat Index
	 * @param statIdx
	 * @param aPC
	 * @return stat mod
	 */
	public int getStatMod(final int statIdx, final PlayerCharacter aPC)
	{
		final List<PCStat> statList = SettingsHandler.getGame().getUnmodifiableStatList();

		if ((statIdx < 0) || (statIdx >= statList.size()))
		{
			return 0;
		}

		final String aStat = statList.get(statIdx).getAbb();

		return (int) bonusTo("STAT", aStat, aPC, aPC);
	}

	/**
	 * Add the level and ability to the level ability list
	 * @param aLevel
	 * @param aString
	 * @return the LevelAbility
	 */
	public LevelAbility addAddList(final int aLevel, final String aString)
	{
		if (levelAbilityList == null)
		{
			levelAbilityList = new ArrayList<LevelAbility>();
		}

		if (aString.startsWith(".CLEAR"))
		{
			if (".CLEAR".equals(aString))
			{
				if (aLevel > 0)
				{
					Logging.errorPrint("Warning: You performed a Dangerous .CLEAR in a ADD: Token");
					Logging.errorPrint("  A non-level limited .CLEAR was used in a Class Level line");
					Logging.errorPrint("  Today, this performs a .CLEAR on the entire PCClass");
					Logging.errorPrint("  However, you are using undocumented behavior that is subject to change");
					Logging.errorPrint("  Hint: It will change after PCGen 5.14");
					Logging.errorPrint("  Please level limit the .CLEAR (e.g. .CLEAR.LEVEL2)");
					Logging.errorPrint("  ... or put the ADD:.CLEAR on a non-level Class line");
				}
				levelAbilityList.clear();
			}
			else if (aString.indexOf(".LEVEL") >= 0)
			{
				int level;

				try
				{
					level = Integer.parseInt(aString.substring(12));
				}
				catch (NumberFormatException e)
				{
					Logging.errorPrint("Badly formed addAddList attribute: " + aString.substring(12));
					level = -1;
				}

				if (aLevel > 0 && aLevel != level)
				{
					Logging.errorPrint("Warning: You performed a Dangerous .CLEAR in a ADD: Token");
					Logging.errorPrint("  A level limited .CLEAR was used in a Class Level line");
					Logging.errorPrint("  But was asked to clear a different Class Level than the Class Level Line it appeared on");
					Logging.errorPrint("  However, you are using undocumented behavior");
					Logging.errorPrint("  Please match the level to the limit on the .CLEAR (e.g. 2<tab>ADD:.CLEAR.LEVEL2)");
					Logging.errorPrint("  ... or put the ADD:.CLEAR on a non-level Class line");
				}
				
				if (level >= 0)
				{
					for (int x = levelAbilityList.size() - 1; x >= 0; --x)
					{
						final LevelAbility ability = levelAbilityList.get(x);

						if (ability.level() == level)
						{
							levelAbilityList.remove(x);
						}
					}
				}
			}
		}
		else
		{
			final LevelAbility la = LevelAbility.createAbility(this, aLevel, aString);
			levelAbilityList.add(la);

			return la;
		}

		return null;
	}

	/**
	 * Make choices for the PC (just calls getChoices)
	 * @param aPC
	 */
	public void makeChoices(final PlayerCharacter aPC)
	{
		getChoices(getChoiceString(), aPC);
	}

	/**
	 * Returns true if the PC has a bonus that is currently applied
	 * @param aPC
	 * @param anObj
	 * @return true if the PC has a bonus that is currently applied
	 */
	public boolean passesPreApplied(final PlayerCharacter aPC, final PObject anObj)
	{
		if (!aPC.getUseTempMods())
		{
			return false;
		}

		// If anObj is null, use this objects tempBonusList
		if (anObj == null)
		{
			for (BonusObj aBonus : getTempBonusList())
			{
				final Object abT = aBonus.getTargetObject();

				if (abT instanceof PlayerCharacter)
				{
					final PlayerCharacter bPC = (PlayerCharacter) abT;

					if (aBonus.isApplied() && (bPC == aPC))
					{
						return true;
					}
				}
			}

			return false;
		}

		// else use the anObj's tempBonusList
		for (BonusObj aBonus : anObj.getTempBonusList())
		{
			final Object abT = aBonus.getTargetObject();

			if (abT instanceof Equipment)
			{
				final Equipment aTarget = (Equipment) abT;

				if (aBonus.isApplied() && aTarget.equals(anObj))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Get the Product Identity string
	 * @return the Product Identity string
	 */
	public String piString()
	{
		return OutputNameFormatting.piString(this, true);
	}

	/**
	 * In some cases, we need a PI-formatted string to place within
	 * a pre-existing <html> tag
	 * @return PI String with no header
	 */
	public String piSubString()
	{
		return OutputNameFormatting.piString(this, false);
	}

	/**
	 * Remove all abilities gained via a level
	 * @param aLevel
	 */
	public void removeAllLevelAbilities(final int aLevel)
	{
		if (levelAbilityList != null)
		{
			for (int x = levelAbilityList.size() - 1; x >= 0; --x)
			{
				if (levelAbilityList.get(x).level() == aLevel)
				{
					levelAbilityList.remove(x);
				}
			}
		}
	}

	/**
	 * Remove an ability gained via a level
	 * @param aLevel
	 * @param aString
	 * @return true if successful
	 */
	public boolean removeLevelAbility(final int aLevel, final String aString)
	{
		for (int x = levelAbilityList.size() - 1; x >= 0; --x)
		{
			final LevelAbility ability = levelAbilityList.get(x);

			if ((ability.level() == aLevel) && (ability.getTagData().equals(aString)))
			{
				levelAbilityList.remove(x);

				return true;
			}
		}

		return false;
	}

	@Override
	public String toString()
	{
		return displayName;
	}

	/**
	 * Get the PCC text with the saved name
	 * @return the PCC text with the saved name
	 */
	public String getPCCText()
	{
		final StringBuffer txt = new StringBuffer(200);
		txt.append(getDisplayName());
		txt.append("\t");
		txt.append(StringUtil.joinToStringBuffer(Globals.getContext().unparse(
				this), "\t"));
		txt.append(getPCCText(false));
		return txt.toString();
	}

	/**
	 * Get the PCC text
	 * @param saveName
	 * @return PCC text
	 */
	protected String getPCCText(final boolean saveName)
	{
//		Iterator e;
		String aString;
		final StringBuffer txt = new StringBuffer(200);

		if (saveName)
		{
			txt.append(getDisplayName());
		}

//		if (!getDisplayName().equals(getKeyName()))
//		{
			txt.append("\tKEY:").append(getKeyName());
//		}

		Set<String> aaKeys = mapListChar.getSecondaryKeySet(MapKey.AUTO_ARRAY);
		if (aaKeys != null)
		{
			for (String s : aaKeys)
			{
				List<String> values = mapListChar.getListFor(MapKey.AUTO_ARRAY, s);
				for (String value : values)
				{
					if (value != null && value.trim().length() > 0)
					{
						txt.append("\tAUTO:").append(s).append(Constants.PIPE)
							.append(value);
					}
				}
			}
		}

		if (!(this instanceof PCClass) && (getBonusList().size() != 0))
		{
			for (BonusObj bonusobj : getBonusList())
			{
				txt.append("\tBONUS:").append(bonusobj.getPCCText()); //This formats the bonus items in the proper .lst manner
			}
		}

		List<String> ccSkillList = getCcSkillList();
		if ((ccSkillList != null) && (ccSkillList.size() != 0))
		{
			txt.append("\tCCSKILL:").append(StringUtil.join(ccSkillList, "|"));
		}

		List<String> cSkillList = getCSkillList();
		if ((cSkillList != null) && (cSkillList.size() != 0))
		{
			txt.append("\tCSKILL:").append(StringUtil.join(cSkillList, "|"));
		}

		aString = getChoiceString();

		if ((aString != null) && (aString.length() != 0))
		{
			txt.append("\tCHOOSE:").append(aString);
		}

		if (hasPrerequisites())
		{
			final StringWriter writer = new StringWriter();
			for (Prerequisite prereq : getPrerequisiteList())
			{
				final PrerequisiteWriter prereqWriter = new PrerequisiteWriter();
				try
				{
					writer.write("\t");
					prereqWriter.write(writer, prereq);
				}
				catch (PersistenceLayerException e1)
				{
					e1.printStackTrace();
				}
			}
			txt.append(writer);
		}

		if (!(this instanceof PCClass))
		{
			ArrayList<SpecialAbility> specialAbilityList = new ArrayList<SpecialAbility>();
			addSABToList(specialAbilityList, null);
			for (SpecialAbility sa : specialAbilityList)
			{
				txt.append("\tSAB:").append(sa.toString());
			}
		}

		if (!(this instanceof PCClass))
		{
			for (PCSpell s : getSpellList())
			{
				txt.append("\tSPELLS:").append(s.getPCCText());
			}
		}

		if (getMyTypeCount() != 0)
		{
			txt.append('\t').append(Constants.s_TAG_TYPE).append(getType());
		}

		aString = theSource.getPageNumber();

		if (aString != null && aString.length() != 0)
		{
			txt.append("\tSOURCEPAGE:").append(aString);
		}

		String regionString = get(StringKey.REGION);
		if ((regionString != null) && regionString.startsWith("0|"))
		{
			txt.append("\tREGION:").append(regionString.substring(2));
		}

		// SPELLLEVEL
		txt.append('\t').append(getSpellSupport().getPCCText());
		
		return txt.toString();
	}


	/**
	 * TODO DOCUMENT ME!
	 *
	 * @param  aLevel
	 * @param  aPC
	 * @param  pcLevelInfo
	 */
	protected void addAddsForLevel(
		final int             aLevel,
		final PlayerCharacter aPC,
		final PCLevelInfo     pcLevelInfo)
	{
		if (
			aPC == null ||
			aPC.isImporting() ||
			!aPC.doLevelAbilities())
		{
			return;
		}

		if (levelAbilityList == null || levelAbilityList.isEmpty())
		{
			return;
		}

		for ( LevelAbility levAbility : levelAbilityList )
		{
			levAbility.setOwner(this);

			if ((levAbility.level() == aLevel) && levAbility.canProcess())
			{
				boolean canProcess = true;

				if (
					(levAbility.isFeat()) &&
					!SettingsHandler.getShowFeatDialogAtLevelUp())
				{
					// Check the list of feats for at least one that is hidden or for
					// output only Show the popup if there is one

					Logging.errorPrint("PObject addAddsForLevel");
					canProcess = false;

					final List<String> featList = new ArrayList<String>();
					levAbility.process(featList, aPC, pcLevelInfo);

					for ( String key : featList )
					{
						final Ability anAbility = Globals.getAbilityKeyed(
								"FEAT",
								key);

						if (anAbility != null)
						{
							switch (anAbility.getSafe(ObjectKey.VISIBILITY))
							{
								case HIDDEN:
								case OUTPUT_ONLY:
									canProcess = true;
									break;

								default:
									continue;
							}

							break;
						}
					}
				}

				if (canProcess)
				{
					levAbility.process(aPC, pcLevelInfo);
				}
				else
				{
					aPC.adjustFeats(1); // need to add 1 feat to total available
				}
			}
		}
	}

	public List<SpecialAbility> addSpecialAbilitiesToList(final List<SpecialAbility> aList, final PlayerCharacter aPC)
	{
		for ( SpecialAbility sa : getSafeListFor(ListKey.SPECIAL_ABILITY) )
		{
			if (sa.pcQualifiesFor(aPC))
			{
				final String key = sa.getKeyName();
				final int idx = key.indexOf("%CHOICE");

				if (idx >= 0)
				{
					StringBuilder sb = new StringBuilder();
					sb.append(key.substring(0, idx));

					if (aPC.hasAssociations(this))
					{
						sb.append(StringUtil.joinToStringBuffer(aPC.getAssociationList(this), ", "));
					}
					else
					{
						sb.append("<undefined>");
					}

					sb.append(key.substring(idx + 7));
					sa =
							new SpecialAbility(sb.toString(), sa.getSASource(),
								sa.getSADesc());
				}

				aList.add(sa);
			}
		}

		return aList;
	}

	/**
	 * This method is used to add the type to the appropriate global list if we
	 * are ever interested in knowing what types are available for a particular
	 * object type (for example, all of the different equipment types)
	 * 
	 * @param type
	 *            The name of the type that is to be added to the global list of
	 *            types.
	 */
	protected void doGlobalTypeUpdate(final String type)
	{
		// Override in any class that wants to store type information
	}

	protected void globalChecks(final PlayerCharacter aPC)
	{
		globalChecks(false, aPC);
	}

	protected void globalChecks(final boolean flag, final PlayerCharacter aPC)
	{
		aPC.setArmorProfListStable(false);
		for (TransitionChoice<Kit> kit : getSafeListFor(ListKey.KIT_CHOICE))
		{
			kit.act(kit.driveChoice(aPC), aPC);
		}
		makeRegionSelection(aPC);

		if (flag)
		{
			makeChoices(aPC);
		}

		if (this instanceof PCClass)
		{
			final PCClass aClass = (PCClass) this;
			final PCLevelInfo pcLevelInfo = aPC.getLevelInfoFor(getKeyName(), aClass.level);
			addAddsForLevel(aClass.level, aPC, pcLevelInfo);
			aClass.getClassLevel(aClass.level).addAdds(aPC);
		}
		else
		{
			addAddsForLevel(-9, aPC, null);
			addAddsForLevel(0, aPC, null);
			addAdds(aPC);
		}

		activateBonuses(aPC);
	}

	protected void subAddsForLevel(final int aLevel, final PlayerCharacter aPC)
	{
		if ((aPC == null) || (levelAbilityList == null) || levelAbilityList.isEmpty())
		{
			return;
		}

		for (LevelAbility ability : levelAbilityList)
		{
			if (ability.level() == aLevel)
			{
				ability.subForLevel(aPC);
			}
		}
	}

	/**
	 * <p>Retrieves the unarmed damage information for this PObject.  This
	 * comes from the <code>UDAM</code> tag, and can be a simple die string
	 * as in <code>1d20</code>, or a list of size-modified data like is
	 * utilised for monk unarmed damage.</p>
	 * @param aPC
	 * @return A string representing the unarmed damage dice of the object.
	 */
	String getUdamForSize(int iSize)
	{
		final StringBuffer aString = new StringBuffer(getElementInList(ListKey.UDAM, 0));

		final StringTokenizer aTok = new StringTokenizer(aString.toString(), ",", false);

		while ((iSize > -1) && aTok.hasMoreTokens())
		{
			aString.replace(0, aString.length(), aTok.nextToken());

			if (iSize == 0)
			{
				break;
			}

			iSize -= 1;
		}
		return aString.toString();
	}

	 /**
	  * Add automatic tags to a list
	  * For example, tag = "ARMORPROF", aList is list of armor proficiencies
	  * @param tag
	  * @param aList
	  * @param aPC
	  * @param expandWeaponTypes
	  */
	public final void addAutoTagsToList(final String tag, final Collection aList, final PlayerCharacter aPC, boolean expandWeaponTypes)
	{
		List<String> list = mapListChar.getListFor(MapKey.AUTO_ARRAY, tag);
		
		if (list == null)
		{
			return;
		}
		
		for (String val : list)
		{
			addAutoTagToList(tag, val, aList, aPC, expandWeaponTypes);
		}
	}

	private void addAutoTagToList(String tag, String aString, Collection aList,
		PlayerCharacter aPC, boolean expandWeaponTypes)
	{
		String preReqTag;
		final List<Prerequisite> aPreReqList = new ArrayList<Prerequisite>();
		final int j1 = aString.lastIndexOf('[');
		int j2 = aString.lastIndexOf(']');

		if (j2 < j1)
		{
			j2 = aString.length();
		}

		if (j1 >= 0)
		{
			preReqTag = aString.substring(j1 + 1, j2);
			Prerequisite prereq = null;
			try
			{
				final PreParserFactory factory = PreParserFactory.getInstance();
				prereq = factory.parse(preReqTag);
			}
			catch (PersistenceLayerException ple)
			{
				Logging.errorPrint(ple.getMessage(), ple);
			}

			if (prereq != null)
			{
				aPreReqList.add(prereq);
			}
			if (!PrereqHandler.passesAll(aPreReqList, aPC, null))
			{
				return;
			}

			aString = aString.substring(0, j1);
		}

		final StringTokenizer aTok = new StringTokenizer(aString, "|");

		while (aTok.hasMoreTokens())
		{
			String tok = aTok.nextToken();

			if ((tok.startsWith("TYPE=") || tok.startsWith("TYPE."))
					&& tag.startsWith("WEAPON") && expandWeaponTypes)
				{
					List<String> xList = processWeaponAutoTags(aPC, tok.substring(5));

					aList.addAll(xList);
				}
			else if ((tok.startsWith("WEAPONTYPE=") || tok.startsWith("WEAPONTYPE."))
					&& tag.startsWith("WEAPON") && expandWeaponTypes)
				{
					List<String> xList = processWeaponAutoTags(aPC, tok.substring(11));

					aList.addAll(xList);
				}
			else if ((tok.startsWith("TYPE=") || tok.startsWith("TYPE."))
				&& tag.startsWith("ARMOR"))
			{
				aList.add(tok);
			}
			else if (tag.startsWith("EQUIP"))
			{
				final Equipment aEq =
						EquipmentList.getEquipmentFromName(tok, aPC);

				if (aEq != null)
				{
					final Equipment newEq = aEq.clone();
					newEq.setQty(1);
					newEq.setAutomatic(true);
					int index = aPC.getCachedOutputIndex(newEq.getKeyName());
					newEq.setOutputIndex(index >= 0 ? index : aList.size()+1);
					aList.add(newEq);
				}
			}
			else if ("%LIST".equals(tok))
			{
				for (String assoc : aPC.getAssociationList(this))
				{
					aList.add(assoc);
				}
			}
			else if ("DEITYWEAPONS".equals(tok))
			{
				if (aPC.getDeity() != null)
				{
					List<CDOMReference<WeaponProf>> weapons = aPC.getDeity()
							.getSafeListFor(ListKey.DEITYWEAPON);
					for (CDOMReference<WeaponProf> ref : weapons)
					{
						if (!Constants.ALLREF_LST.equals(ref.getLSTformat()))
						{
							for (WeaponProf wp : ref.getContainedObjects())
							{
								if (!wp.isType("Natural"))
								{
									aList.add(wp.getKeyName());
								}
							}
						}
					}
				}
			}
			else
			{
				// add tok to list
				aList.add(tok);
			}
		}
	}

	/**
	 * @param aPC
	 * @param tok
	 * @return
	 */
	private List<String> processWeaponAutoTags(final PlayerCharacter aPC, String tok)
	{
		final StringTokenizer bTok = new StringTokenizer(tok, ".");
		List<WeaponProf> xList = null;

		while (bTok.hasMoreTokens())
		{
			final String bString = bTok.nextToken();
			final List<WeaponProf> pcWeapProfList = WeaponProfType.getWeaponProfs(bString, aPC);
			final List<Equipment> pcWeaponList = new ArrayList<Equipment>();
			if (pcWeapProfList.size() == 0)
			{
				pcWeaponList.addAll(EquipmentList.getEquipmentOfType("Weapon." + bString, ""));
			}

			if (xList == null)
			{
				xList = new ArrayList<WeaponProf>();

				for (WeaponProf obj : pcWeapProfList)
				{
					if (!xList.contains(obj))
					{
						xList.add(obj);
					}
				}
				
				for (Equipment obj : pcWeaponList)
				{
					CDOMSingleRef<WeaponProf> ref = obj
							.get(ObjectKey.WEAPON_PROF);
					if (ref != null)
					{
						WeaponProf wp = ref.resolvesTo();
						if (!xList.contains(wp))
						{
							xList.add(wp);
						}
					}
				}
			}
			else
			{
				final List<WeaponProf> removeList = new ArrayList<WeaponProf>();

				for (WeaponProf wprof : xList)
				{
					boolean contains = false;

					for (WeaponProf obj : pcWeapProfList)
					{
						if (wprof.equals(obj))
						{
							contains = true;

							break;
						}
					}
					if(!contains) {
						for (Equipment obj : pcWeaponList)
						{
							CDOMSingleRef<WeaponProf> ref = obj.get(ObjectKey.WEAPON_PROF);
							if (ref != null)
							{
								if (wprof.equals(ref.resolvesTo()))
								{
									contains = true;

									break;
								}
							}
						}

						if (!contains)
						{
							removeList.add(wprof);
						}
					}
				}

				for (WeaponProf wprof : removeList)
				{
					xList.remove(wprof);
				}
			}
		}
		List<String> returnList = new ArrayList<String>(xList.size());
		for (WeaponProf wp : xList)
		{
			returnList.add(wp.getKeyName());
		}
		return returnList;
	}

	/**
	 * Add a user defined type
	 * @param myType
	 */
	void addMyType(final String myType)
	{
		types.add(myType);
	}

	/*
	 * REFACTOR Get this OUT of PObject's interface since this is ONLY in PCClass.
	 * Not to mention that the overload code will probably be removed from PCClass.
	 */
	void fireNameChanged(final String oldName, final String newName)
	{
		// This method currently does nothing so it may be overriden in PCClass.
	}

	public final boolean hasCcSkill(PlayerCharacter pc, final String aName)
	{
		List<String> ccSkillList = getCcSkillList();
		if ((ccSkillList == null) || ccSkillList.isEmpty())
		{
			return false;
		}

		if (ccSkillList.contains(aName))
		{
			return true;
		}


		if (ccSkillList.contains("LIST"))
		{
			for (String aString : pc.getAssociationList(this))
			{
				if (aName.startsWith(aString) || aString.startsWith(aName))
				{
					return true;
				}
			}
		}
		
		for (String aString : getCcSkillList())
		{
			if (aString.lastIndexOf('%') >= 0)
			{
				aString = aString.substring(0, aString.length() - 1);

				if (aName.startsWith(aString))
				{
					return true;
				}
			}
		}

		return false;
	}

	public final boolean hasCSkill(PlayerCharacter pc, final String aName)
	{
		List<String> cSkillList = getCSkillList();
		if ((cSkillList == null) || cSkillList.isEmpty())
		{
			return false;
		}

		if (cSkillList.contains(aName))
		{
			return true;
		}

		if (cSkillList.contains("LIST"))
		{
			for (String aString : pc.getAssociationList(this))
			{
				if (aName.startsWith(aString) || aString.startsWith(aName))
				{
					return true;
				}
			}
		}

		for (String aString : cSkillList)
		{
			if (aString.lastIndexOf('%') >= 0)
			{
				aString = aString.substring(0, aString.length() - 1);

				if (aName.startsWith(aString))
				{
					return true;
				}
			}

			if (aName.equalsIgnoreCase(aString))
			{
				return true;
			}
		}

		return false;
	}

	final void makeRegionSelection(final PlayerCharacter aPC)
	{
		makeRegionSelection(0, aPC);
	}

	final void makeRegionSelection(final int arg, final PlayerCharacter aPC)
	{
		String regionString = get(StringKey.REGION);
		if (regionString == null)
		{
			return;
		}

		final StringTokenizer aTok = new StringTokenizer(regionString, "|");

		// first element is prelevel - should be 0 for everything but PCClass entries
		String tok = aTok.nextToken();
		int aLevel;

		try
		{
			aLevel = Integer.parseInt(tok);
		}
		catch (NumberFormatException e)
		{
			Logging.errorPrint("Badly formed preLevel attribute in makeRegionSelection: " + tok);
			aLevel = 0;
		}

		if (aLevel > arg)
		{
			return;
		}

		tok = aTok.nextToken();

		int num;

		try
		{
			num = Integer.parseInt(tok); // number of selections
		}
		catch (NumberFormatException e)
		{
			Logging.errorPrint("Badly formed number of selection attribute in makeRegionSelection: " + tok);
			num = -1;
		}

		List<String> aList = new ArrayList<String>();

		while (aTok.hasMoreTokens())
		{
			aList.add(aTok.nextToken());
		}

		if (num != aList.size())
		{
			final ChooserInterface c = ChooserFactory.getChooserInstance();
			c.setTitle("Region Selection");
			c.setTotalChoicesAvail(num);
			c.setPoolFlag(false);
			c.setAvailableList(aList);
			c.setVisible(true);
			aList = c.getSelectedList();
		}

		if (aList.size() > 0)
		{
			for ( String region : aList )
			{
				if (aPC.getRegion().equalsIgnoreCase(region))
				{
					continue;
				}

				aPC.setRegion(region);
			}
		}
	}

	int numberInList(PlayerCharacter pc, final String aType)
	{
		return 0;
	}

	/**
	 * rephrase parenthetical name components, if appropriate
	 * @return pre formatted output name
	 */
	private String getPreFormatedOutputName()
	{
		//if there are no () to pull from, just return the name
		if ((displayName.indexOf('(') < 0) || (displayName.indexOf(')') < 0))
		{
			return displayName;
		}

		//we just take from the first ( to the first ), typically there should only be one of each
		final String subName = displayName.substring(displayName.indexOf('(') + 1, displayName.indexOf(')')); //the stuff inside the ()
		final StringTokenizer tok = new StringTokenizer(subName, "/");
		final StringBuffer newNameBuff = new StringBuffer();

		while (tok.hasMoreTokens())
		{
			//build this new string from right to left
			newNameBuff.insert(0, tok.nextToken());

			if (tok.hasMoreTokens())
			{
				newNameBuff.insert(0, " ");
			}
		}

		return newNameBuff.toString();
	}

	protected void clearMyType()
	{
		types.clear();
	}

	/**
	 * Clear the selected weapon proficiency bonuses
	 *
	 */
	public void clearSelectedWeaponProfBonus()
	{
		removeListFor(ListKey.SELECTED_WEAPON_PROF_BONUS);
	}

	protected void removeMyType(final String myType)
	{
		types.remove(myType);
	}

	/**
	 * Get the Spell Support for this object
	 * @return SpellSupport
	 */
	public SpellSupport getSpellSupport()
	{
		return spellSupport;
	}

	/**
	 * Set a list of languages that the character this Template is applied to
	 * automatically knows.
	 *
	 * @param  argChooseLanguageAutos  a comma separated list of languages to add
	 */
	public void setChooseLanguageAutos(final String argChooseLanguageAutos)
	{
		chooseLanguageAutos = argChooseLanguageAutos;
	}

	/**
	 * Get a list of languages that the character this Template is applied to
	 * automatically knows.
	 *
	 * @return  a comma separated list of languages automatically known
	 */
	public String getChooseLanguageAutos()
	{
		return chooseLanguageAutos;
	}

	/**
	 * Adds one chosen language.
	 *
	 * @param  flag
	 * @param  aPC
	 */
	void chooseLanguageAutos(final boolean flag, final PlayerCharacter aPC)
	{
		if (!flag && !"".equals(chooseLanguageAutos))
		{
			final List<Language> selectedList; // selected list of choices

			final ChooserInterface c = ChooserFactory.getChooserInstance();
			c.setTotalChoicesAvail(1);
			c.setPoolFlag(false);
			c.setTitle("Pick a Language: ");

			Set<Language> list = LanguageSupport.getLanguagesFromString(chooseLanguageAutos);
			c.setAvailableList(new ArrayList<Language>(list));
			c.setVisible(true);
			selectedList = c.getSelectedList();

			if ((selectedList != null) && (selectedList.size() != 0))
			{
				aPC.addFreeLanguage(selectedList.get(0));
			}
		}
	}

	private DoubleKeyMap<AbilityCategory, Ability.Nature, List<QualifiedObject<String>>> theAbilities = new DoubleKeyMap<AbilityCategory, Ability.Nature, List<QualifiedObject<String>>>();
	
	public void addAbility(final AbilityCategory aCategory, final Ability.Nature aNature, final QualifiedObject<String> anAbility)
	{
		List<QualifiedObject<String>> abilities = theAbilities.get(aCategory, aNature);
		if ( abilities == null )
		{
			abilities = new ArrayList<QualifiedObject<String>>();
			theAbilities.put(aCategory, aNature, abilities);
		}
		abilities.add(anAbility);
	}
	
	/**
	 * @param aCategory
	 * @param aNature
	 * @return List<QualifiedObject<String>>
	 * 
	 * Returns a list of abilities for specific a specific category and nature
	 */
	public List<QualifiedObject<String>> getRawAbilityObjects(
		final AbilityCategory aCategory, final Ability.Nature aNature)
	{
		List<QualifiedObject<String>> abilities = theAbilities.get(aCategory, aNature);
		if ( abilities == null )
		{
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(theAbilities.get(aCategory, aNature));
	}
	
	public boolean removeAbility(final AbilityCategory aCategory,
		final Ability.Nature aNature, QualifiedObject<String> qo)
	{
		List<QualifiedObject<String>> abilities = theAbilities.get(aCategory, aNature);
		return abilities != null && abilities.remove(qo);
	}
	
	public List<String> getAbilityKeys(final PlayerCharacter aPC,
		final AbilityCategory aCategory, final Ability.Nature aNature)
	{
		final List<QualifiedObject<String>> abilities = theAbilities.get(aCategory, aNature);
		if ( abilities == null )
		{
			return Collections.emptyList();
		}
		final List<String> ret = new ArrayList<String>(abilities.size());
		for ( final QualifiedObject<String> str : abilities )
		{
			if ( str.qualifies(aPC) )
			{
				ret.add(str.getObject(aPC));
			}
		}
		return ret;
	}
	
	/**
	 * @return The ability categories as a List
	 *
	 */
	public List<AbilityCategory> getAbilityCategories()
	{
		
		if ( theAbilities == null )
		{
			return new ArrayList<AbilityCategory>();
		}
		final List<AbilityCategory> abList = new ArrayList<AbilityCategory>();
		final Set<AbilityCategory> keys = theAbilities.getKeySet();
		
		for(AbilityCategory ab: keys)
		{
			abList.add(ab);
		}
		
		return Collections.unmodifiableList(abList);
	}
	
	/**
	 * @param category
	 * @return Return the ability nature (enum) as a list.
	 */
	public List<Ability.Nature> getAbilityNatures(final AbilityCategory category)
	{
		if ( theAbilities == null || theAbilities.isEmpty() )
		{
			return new ArrayList<Ability.Nature>();
		}
		final Set<Ability.Nature> keys = theAbilities.getSecondaryKeySet(category);
		final List<Ability.Nature>  abNature = new ArrayList<Ability.Nature>();	
		
		for(Ability.Nature nature: keys)
		{
			abNature.add(nature);
		}
		return Collections.unmodifiableList(abNature);
	}

	/**
	 * Get the list of bonuses for this object
	 * @param as TODO
	 * @return the list of bonuses for this object
	 */
	public List<BonusObj> getBonusList(AssociationStore as)
	{
		return bonusList;
	}

	/**
	 * Get the list of bonuses for this object
	 * @param as TODO
	 * @return the list of bonuses for this object
	 */
	public List<BonusObj> getBonusList()
	{
		return bonusList;
	}

	public void clearBonusList()
	{
		bonusList.clear();
	}
	
	/**
	 * Get the list of bounuses of a particular type for this object
	 * @param as TODO
	 * @param aType
	 * @param aName
	 * @return the list of bounuses of a particular type for this object
	 */
	public List<BonusObj> getBonusListOfType(AssociationStore as, final String aType, final String aName)
	{
		return BonusUtilities.getBonusFromList(getBonusList(as), aType, aName);
	}

	/**
	 * Apply the bonus to a PC, pass through object's default bonuslist
	 *
	 * @param aType
	 * @param aName
	 * @param obj
	 * @param aPC
	 * @return the bonus
	 */
	public double bonusTo(final String aType, final String aName, final AssociationStore obj, final PlayerCharacter aPC)
	{
		return bonusTo(aType, aName, obj, getBonusList(obj), aPC);
	}

	/**
	 * Apply the bonus to a PC
	 *
	 * @param aType
	 * @param aName
	 * @param obj
	 * @param aBonusList
	 * @param aPC
	 * @return the bonus
	 */
	public double bonusTo(String aType, String aName, final Object obj, final List<BonusObj> aBonusList, final PlayerCharacter aPC)
	{
		if ((aBonusList == null) || (aBonusList.size() == 0))
		{
			return 0;
		}

		double retVal = 0;

		aType = aType.toUpperCase();
		aName = aName.toUpperCase();

		final String aTypePlusName = new StringBuffer(aType).append('.').append(aName).append('.').toString();

		if (!dontRecurse && (this instanceof Ability) && !Globals.checkRule(RuleConstants.FEATPRE))
		{
			// SUCK!  This is horrid, but bonusTo is actually recursive with respect to
			// passesPreReqToGain and there is no other way to do this without decomposing the
			// dependencies.  I am loathe to break working code.
			// This addresses bug #709677 -- Feats give bonuses even if you no longer qualify
			dontRecurse = true;

			boolean returnZero = false;

			if (!PrereqHandler.passesAll(getPrerequisiteList(), aPC, this))
			{
				returnZero = true;
			}

			dontRecurse = false;

			if (returnZero)
			{
				return 0;
			}
		}

		int iTimes = 1;

		if ("VAR".equals(aType))
		{
			iTimes = Math.max(1, aPC.getDetailedAssociationCount(this));

			//
			// SALIST will stick BONUS:VAR|...
			// into bonus list so don't multiply
			//
			String choiceString = getChoiceString();
			if (choiceString.startsWith("SALIST|") && (choiceString.indexOf("|VAR|") >= 0))
			{
				iTimes = 1;
			}
		}

		for ( BonusObj bonus : aBonusList )
		{
			String bString = bonus.toString().toUpperCase();

			if (aPC.hasAssociations(this))
			{
				int span = 4;
				int idx = bString.indexOf("%VAR");

				if (idx == -1)
				{
					idx = bString.indexOf("%LIST|");
					span = 5;
				}

				if (idx >= 0)
				{
					final String firstPart = bString.substring(0, idx);
					final String secondPart = bString.substring(idx + span);

					for (String assoc : aPC.getAssociationList(this))
					{
						final String xString = new StringBuffer().append(firstPart).append(assoc).append(secondPart)
							.toString().toUpperCase();
						retVal += calcBonus(xString, aType, aName, aTypePlusName, obj, iTimes, bonus, aPC);
					}
				}
			}
			else
			{
				retVal += calcBonus(bString, aType, aName, aTypePlusName, obj, iTimes, bonus, aPC);
			}
		}

		return retVal;
	}

	/**
	 * Add a new bonus to the list of bonuses
	 * @param aString The unparsed bonus to be added
	 * @return true if new bonus is not null
	 */
	public final boolean addBonusList(final String aString)
	{
		return addBonusList(aString, false);
	}
	
	/**
	 * Add a new bonus to the list of bonuses. The bonus can optionally 
	 * only be added once no matter how many associated choices this 
	 * object has. This is normally used where a bonus is added for 
	 * each associated choice.
	 *  
	 * @param aString The unparsed bonus to be added
	 * @param addOnceOnly Should the bonus only be added once irrespective of number of choices 
	 * @return true if new bonus is not null
	 */
	private final boolean addBonusList(final String aString, final boolean addOnceOnly)
	{
		if (bonusList == null)
		{
			bonusList = new ArrayList<BonusObj>();
		}

		final BonusObj aBonus = Bonus.newBonus(aString);

		if (aBonus != null)
		{
			aBonus.setCreatorObject(this);
			aBonus.setAddOnceOnly(addOnceOnly);
			addBonusList(aBonus);
		}

		return (aBonus != null);
	}

	/**
	 * returns all BonusObj's that are "active"
	 * @param aPC A PlayerCharacter object.
	 * @return active bonuses
	 */
	public List<BonusObj> getActiveBonuses(final PlayerCharacter aPC)
	{
		final List<BonusObj> aList = new ArrayList<BonusObj>();

		for ( BonusObj bonus : getBonusList() )
		{
			if (bonus.isApplied())
			{
				aList.add(bonus);
			}
		}

		return aList;
	}

	/**
	 * Get the list of bonuses as a String
	 * @param aString
	 * @return the list of bonuses as a String
	 */
	public boolean getBonusListString(final String aString)
	{
		for ( BonusObj bonus : getBonusList() )
		{
			if (bonus.getBonusInfo().equalsIgnoreCase(aString))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets all the BonusObj's to "active"
	 * @param aPC
	 */
	public void activateBonuses(final PlayerCharacter aPC)
	{
		for (Iterator<BonusObj> ab = getBonusList().iterator(); ab.hasNext();)
		{
			final BonusObj aBonus = ab.next();
			aBonus.setApplied(false);

			if (aBonus.qualifies(aPC)
				&& aBonus.getPCLevel() <= aPC.getTotalLevels())
			{
				aBonus.setApplied(true);
			}
		}
	}

	/**
	 * This function will be required during the continued re-write
	 * of the BonusObj code -- JSC 8/18/03
	 *
	 * @param aBonus
	 */
	public final void addBonusList(final BonusObj aBonus)
	{
		bonusList.add(aBonus);
	}

	/**
	 * Deactivate all of the bonuses
	 */
	public void deactivateBonuses()
	{
		for ( BonusObj bonus : getBonusList() )
		{
			bonus.setApplied(false);
		}
	}

	/**
	 * Set's all the BonusObj's to this creator
	 */
	public void ownBonuses()
	{
		for ( BonusObj bonus : getBonusList() )
		{
			bonus.setCreatorObject(this);
		}
	}

	/**
	 * Remove the bonus object from the bonus list
	 * @param aBonus
	 */
	public void removeBonusList(final BonusObj aBonus)
	{
		getBonusList().remove(aBonus);
	}

	/**
	 * Remove all bonuses gained via a level
	 * @param aLevel
	 */
	public void removeAllBonuses(final int aLevel)
	{
		if (bonusList != null)
		{
			for (int x = bonusList.size() - 1; x >= 0; --x)
			{
				if (bonusList.get(x).getPCLevel() == aLevel)
				{
					bonusList.remove(x);
				}
			}
		}
	}

	/**
	 * Apply the bonus to a character. The bonus can optionally 
	 * only be added once no matter how many associated choices this 
	 * object has. This is normally used where a bonus is added for 
	 * each associated choice.
	 * 
	 * @param bonusString The unparsed bonus to be added.
	 * @param chooseString The choice to be added.
	 * @param aPC The character to apply thr bonus to.
	 * @param addOnceOnly Should the bonus only be added once irrespective of number of choices 
	 */
	public final void applyBonus(String bonusString, final String chooseString,
		final PlayerCharacter aPC, final boolean addOnceOnly)
	{
		bonusString = makeBonusString(bonusString, chooseString, aPC);
		addBonusList(bonusString, addOnceOnly);
		addSave("BONUS|" + bonusString);
	}

	String makeBonusString(String bonusString, final String chooseString, final PlayerCharacter aPC)
	{
		// assumption is that the chooseString is in the form class/type[space]level
		int i = chooseString.lastIndexOf(' ');
		String classString = "";
		String levelString = "";

		if (bonusString.startsWith("BONUS:"))
		{
			bonusString = bonusString.substring(6);
		}

		final boolean lockIt = bonusString.endsWith(".LOCK");

		if (lockIt)
		{
			bonusString = bonusString.substring(0, bonusString.lastIndexOf(".LOCK"));
		}

		if (i >= 0)
		{
			classString = chooseString.substring(0, i);

			if (i < chooseString.length())
			{
				levelString = chooseString.substring(i + 1);
			}
		}

		while (bonusString.lastIndexOf("TYPE=%") >= 0)
		{
			i = bonusString.lastIndexOf("TYPE=%");
			bonusString = bonusString.substring(0, i + 5) + classString + bonusString.substring(i + 6);
		}

		while (bonusString.lastIndexOf("CLASS=%") >= 0)
		{
			i = bonusString.lastIndexOf("CLASS=%");
			bonusString = bonusString.substring(0, i + 6) + classString + bonusString.substring(i + 7);
		}

		while (bonusString.lastIndexOf("LEVEL=%") >= 0)
		{
			i = bonusString.lastIndexOf("LEVEL=%");
			bonusString = bonusString.substring(0, i + 6) + levelString + bonusString.substring(i + 7);
		}

		if (lockIt)
		{
			i = bonusString.lastIndexOf('|');

			final Float val = aPC.getVariableValue(bonusString.substring(i + 1), "");
			bonusString = bonusString.substring(0, i) + "|" + val;
		}

		return bonusString;
	}

	/**
	 * Remove the bonus from this objects list of bonuses.
	 *
	 * @param bonusString The string representing the bonus
	 * @param chooseString The choice that was made.
	 * @param aPC The player character to remove th bonus from.
	 */
	public final void removeBonus(final String bonusString, final String chooseString, final PlayerCharacter aPC)
	{
		String bonus = makeBonusString(bonusString, chooseString, aPC);

		int index = -1;

		final BonusObj aBonus = Bonus.newBonus(bonus);
		String bonusStrRep = String.valueOf(aBonus);

		if (getBonusList() != null)
		{
			int count = 0;
			for (BonusObj listBonus : getBonusList())
			{
				if (listBonus.getCreatorObject().equals(this)
					&& listBonus.toString().equals(bonusStrRep))
				{
					index = count;
				}
				count++;
			}
		}

		if (index >= 0)
		{
			getBonusList().remove(index);
		}
		else
		{
			Logging.errorPrint("removeBonus: Could not find bonus: " + bonus + " in bonusList " + getBonusList());
		}

		removeSave("BONUS|" + bonus);
	}

	/**
	 * calcBonus adds together all the bonuses for aType of aName
	 *
	 * @param bString       Either the entire BONUS:COMBAT|AC|2 string or part of a %LIST or %VAR bonus section
	 * @param aType         Such as "COMBAT"
	 * @param aName         Such as "AC"
	 * @param aTypePlusName "COMBAT.AC."
	 * @param obj           The object to get the bonus from
	 * @param iTimes        multiply bonus * iTimes
	 * @param aBonusObj
	 * @param aPC
	 * @return bonus
	 */
	private double calcBonus(final String bString, final String aType, final String aName, String aTypePlusName, final Object obj, final int iTimes,
							 final BonusObj aBonusObj, final PlayerCharacter aPC)
	{
		final StringTokenizer aTok = new StringTokenizer(bString, "|");

		if (aTok.countTokens() < 3)
		{
			Logging.errorPrint("Badly formed BONUS:" + bString);

			return 0;
		}

		String aString = aTok.nextToken();

		if ((!aString.equalsIgnoreCase(aType) && !aString.endsWith("%LIST"))
			|| (aString.endsWith("%LIST") && (numberInList(aPC, aType) == 0)) || (aName.equals("ALL")))
		{
			return 0;
		}

		final String aList = aTok.nextToken();

		if (!aList.equals("LIST") && !aList.equals("ALL") && (aList.toUpperCase().indexOf(aName.toUpperCase()) < 0))
		{
			return 0;
		}

		if (aList.equals("ALL")
			&& ((aName.indexOf("STAT=") >= 0) || (aName.indexOf("TYPE=") >= 0) || (aName.indexOf("LIST") >= 0)
			|| (aName.indexOf("VAR") >= 0)))
		{
			return 0;
		}

		if (aTok.hasMoreTokens())
		{
			aString = aTok.nextToken();
		}

		double iBonus = 0;

		if (obj instanceof PlayerCharacter)
		{
			iBonus = ((PlayerCharacter) obj).getVariableValue(aString, "").doubleValue();
		}
		else if (obj instanceof Equipment)
		{
			iBonus = ((Equipment) obj).getVariableValue(aString, "", aPC).doubleValue();
		}
		else
		{
			try
			{
				iBonus = Float.parseFloat(aString);
			}
			catch (NumberFormatException e)
			{
				//Should this be ignored?
				Logging.errorPrint("calcBonus NumberFormatException in BONUS: " + aString, e);
			}
		}

		final String possibleBonusTypeString = aBonusObj.getTypeString();

		// must meet criteria before adding any bonuses
		if (obj instanceof PlayerCharacter)
		{
			if ( !aBonusObj.qualifies((PlayerCharacter)obj) )
			{
				return 0;
			}
		}
		else
		{
			if ( !PrereqHandler.passesAll(aBonusObj.getPrerequisiteList(), ((Equipment)obj), aPC) )
			{
				return 0;
			}
		}

		double bonus = 0;

		if ("LIST".equalsIgnoreCase(aList))
		{
			final int iCount = numberInList(aPC, aName);

			if (iCount != 0)
			{
				bonus += (iBonus * iCount);
			}
		}

		String bonusTypeString = null;

		final StringTokenizer bTok = new StringTokenizer(aList, ",");

		if (aList.equalsIgnoreCase("LIST"))
		{
			bTok.nextToken();
		}
		else if (aList.equalsIgnoreCase("ALL"))
		{
			// aTypePlusName looks like: "SKILL.ALL."
			// so we need to reset it to "SKILL.Hide."
			aTypePlusName = new StringBuffer(aType).append('.').append(aName).append('.').toString();
			bonus = iBonus;
			bonusTypeString = possibleBonusTypeString;
		}

		while (bTok.hasMoreTokens())
		{
			if (bTok.nextToken().equalsIgnoreCase(aName))
			{
				bonus += iBonus;
				bonusTypeString = possibleBonusTypeString;
			}
		}

		if (obj instanceof Equipment)
		{
			((Equipment) obj).setBonusStackFor(bonus * iTimes, aTypePlusName + bonusTypeString);
		}

		// The "ALL" subtag is used to build the stacking bonusMap
		// not to get a bonus value, so just return
		if (aList.equals("ALL"))
		{
			return 0;
		}

		return bonus * iTimes;
	}

	public void clearAdds() {
		levelAbilityList.clear();
	}

	public void putServesAs( final String key, final String category) 
	{
		if (servesAsList == null)
		{
			servesAsList = new HashMap<String,List<String>>();
		}
		List<String> list = servesAsList.get(category);
		if (list == null)
		{
			list = new ArrayList<String>();
			servesAsList.put(category, list);
		}
		list.add(key);
	}

	//TODO This exposes internal structure - be careful.
	public final Map<String,List<String>> getServesAs()
	{
		if (servesAsList == null)
		{
			servesAsList = new  HashMap<String,List<String>>();
		}
		return servesAsList;
	}
	
	public final List<String> getServesAs(String category)
	{
		if (servesAsList == null)
		{
			servesAsList = new  HashMap<String,List<String>>();
		}
		List<String> result = servesAsList.get(category); 
		return result == null ? new ArrayList<String>() : Collections
			.unmodifiableList(result);
	}
}
