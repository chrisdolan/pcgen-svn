/*
 * Copyright 2007, 2008 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pcgen.base.formula.Formula;
import pcgen.cdom.enumeration.AssociationListKey;
import pcgen.core.PlayerCharacter;
import pcgen.util.StringPClassUtil;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.chooser.ChooserInterface;

/**
 * This is a transitional class from PCGen 5.15+ to the final CDOM core. It is
 * provided as convenience to hold a set of choices and the number of choices
 * allowed, prior to final implementation of the new choice system
 * 
 * @param <T>
 *            The type of object that will be chosen when this TransitionChoice
 *            is used
 */
public class TransitionChoice<T>
{

	/**
	 * The underlying ChoiceSet used to determine the choices available when
	 * selections are to be made in this TransitionChoice.
	 */
	private final ChoiceSet<? extends T> choices;

	/**
	 * The Formula indicating the number of choices to be made when selections
	 * are made in this TransitionChoice.
	 */
	private final Formula choiceCount;

	/**
	 * The title of this choice - presented to the user as the title of the
	 * dialog box from which selections are made.
	 */
	private String title;

	/**
	 * IDentifies if this TransitionChoice selection is required - if it is
	 * required, then the user cannot dismiss the dialog box without making a
	 * choice (or the dialog box reappears, etc.)
	 */
	private boolean required = true;

	/**
	 * The ChoiceActor (optional) which will act upon any choices made from this
	 * TransitionChoice.
	 */
	private ChoiceActor<T> choiceActor;

	/**
	 * Identifies if this TransitionChoice allows stacking of the same object.
	 * 
	 * This is typically done with Abilities, which has the STACKS: token in
	 * order to identify stackable Abilities. Note that this field only allows
	 * stacking, it does not enable stacking of objects which are not generally
	 * stackable.
	 */
	private boolean allowStack = false;

	/**
	 * Identifies any limit to stacking in this TransitionChoice. This is only
	 * enabled if allowStack is true, and limits the number of times a single
	 * object may be stacked in this selection.
	 */
	private Integer stackLimit = null;

	/**
	 * Constructs a new TransitionChoice with the given ChoiceSet (of possible
	 * choices) and Formula (indicating the number of choices that may be taken)
	 * 
	 * @param cs
	 *            The ChoiceSet indicating the choices available in this
	 *            TransitionChoice.
	 * @param count
	 *            The Formula indicating the number of choices that may be
	 *            selected when selections are made in this TransitionChoice.
	 */
	public TransitionChoice(ChoiceSet<? extends T> cs, Formula count)
	{
		choices = cs;
		choiceCount = count;
	}

	/**
	 * Returns the ChoiceSet for this TransitionChoice.
	 * 
	 * TODO Should determine if this should be exposed. It seems this is
	 * primarily used to get access to getLSTformat and getChoiceClass, so
	 * perhaps the TransitionChoice should delegate those instead in order to
	 * protect the ChoiceSet?
	 * 
	 * @return The ChoiceSet for this TransitionChoice.
	 */
	public ChoiceSet<? extends T> getChoices()
	{
		return choices;
	}

	/**
	 * Returns the Formula indicating the number of selections available when
	 * selections are made in this TransitionChoice.
	 * 
	 * @return The Formula indicating the number of selections available
	 */
	public Formula getCount()
	{
		return choiceCount;
	}

	/**
	 * Returns true if the given Object is a TransitionChoice and has identical
	 * underlying choices and choiceCount
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof TransitionChoice)
		{
			TransitionChoice<?> other = (TransitionChoice<?>) obj;
			return choiceCount.equals(other.choiceCount)
					&& choices.equals(other.choices);
		}
		return false;
	}

	/**
	 * Returns a consistent-with-equals hashCode for this TransitionChoice.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return choiceCount.hashCode() * 29 + choices.hashCode();
	}

	/**
	 * Drives a selection for this TransitionChoice on the given
	 * PlayerCharacter.
	 * 
	 * @param pc
	 *            The PlayerCharacter for which this TransitionChoice should
	 *            drive a choice.
	 * @return A Collection of objects of the type that this TransitionChoice
	 *         selects.
	 */
	public Collection<? extends T> driveChoice(PlayerCharacter pc)
	{
		ChooserInterface c = ChooserFactory.getChooserInstance();
		int intValue = choiceCount.resolve(pc, "").intValue();
		c.setPoolFlag(required);
		if (intValue == Integer.MAX_VALUE)
		{
			c.setPickAll(true);
		}
		else
		{
			c.setTotalChoicesAvail(intValue);
		}
		if (title == null)
		{
			title = "Choose a "
					+ StringPClassUtil.getStringFor(choices.getChoiceClass());
		}
		c.setTitle(title);
		Set<? extends T> set = choices.getSet(pc);
		Set<T> allowed = new HashSet<T>();
		List<Object> assocList = pc.getAssocList(this, AssociationListKey.ADD);
		for (T o : set)
		{
			if (choiceActor == null || choiceActor.allow(o, pc, allowStack))
			{
				if (assocList != null && stackLimit != null && stackLimit > 0)
				{
					int takenCount = 0;
					for (Object choice : assocList)
					{
						if (choice.equals(o))
						{
							takenCount++;
						}
					}
					if (stackLimit <= takenCount)
					{
						continue;
					}
				}
				allowed.add(o);
			}
		}

		if (c.pickAll() || intValue == set.size())
		{
			return allowed;
		}
		else
		{
			c.setAvailableList(new ArrayList<T>(allowed));
			c.setVisible(true);
			return c.getSelectedList();
		}
	}

	/**
	 * Sets the title of this TransitionChoice (displayed to the user as the
	 * title of the dialog box used to make the selections).
	 * 
	 * @param string
	 *            The title of this TransitionChoice.
	 */
	public void setTitle(String string)
	{
		title = string;
	}

	/**
	 * Sets whether a selection from this TransitionChoice is required. If
	 * required, a TransitionChoice will not exit the driveChoice method until
	 * the user has made a selection.
	 * 
	 * @param b
	 *            true if a selection from this TransitionChoice should be
	 *            required.
	 */
	public void setRequired(boolean b)
	{
		required = b;
	}

	/**
	 * Sets the (optional) ChoiceActor for this TransitionChoice. The
	 * ChoiceActor will be called when the act method of TransitionChoice is
	 * called. If the ChoiceActor is not set, then the set method may not be
	 * used without triggering an exception.
	 * 
	 * @param ca
	 *            The ChoiceActor for this TransitionChoice.
	 */
	public void setChoiceActor(ChoiceActor<T> ca)
	{
		choiceActor = ca;
	}

	/**
	 * Acts upon choices made in this TransitionChoice.
	 * 
	 * @param choices
	 *            The choices on which this TransitionChoice should act.
	 * @param owner
	 *            The owning object for this TransitionChoice
	 * @param apc
	 *            The PlayerCharacter to which the choices should be applied.
	 */
	public void act(Collection<? extends T> choicesMade, CDOMObject owner,
			PlayerCharacter apc)
	{
		if (choiceActor == null)
		{
			throw new IllegalStateException(
					"Cannot act without a defined ChoiceActor");
		}
		for (T choice : choicesMade)
		{
			choiceActor.applyChoice(owner, choice, apc);
			apc.addAssoc(this, AssociationListKey.ADD, choice);
		}
	}

	/**
	 * Casts an object to the (Generic) Type of this TransitionChoice.
	 * 
	 * @param o
	 *            The incoming object
	 * @return The incoming object, cast to the (Generic) type of this
	 *         TransitionChoice.
	 */
	public T castChoice(Object o)
	{
		return (T) o;
	}

	/**
	 * Sets whether this TransitionChoice should allow stacking.
	 * 
	 * @param allow
	 *            true if this TransitionChoice should allow stacking; false
	 *            otherwise.
	 */
	public void allowStack(boolean allow)
	{
		allowStack = allow;
	}

	/**
	 * Sets the stacking limit of this TransitionChoice. This is only enabled if
	 * allowStack is set to true. This limits the number of times an individual
	 * item can stack in a given TransitionChoice.
	 * 
	 * @param limit
	 *            The limit (number of times a stackable item may be selected in
	 *            this TransitionChoice)
	 */
	public void setStackLimit(int limit)
	{
		stackLimit = limit;
	}

	/**
	 * Identifies if this TransitionChoice allows stacking.
	 * 
	 * @return true if this TransitionChoice should allow stacking; false
	 *         otherwise.
	 */
	public boolean allowsStacking()
	{
		return allowStack;
	}

	/**
	 * Returns the Stacking Limit of this TransitionChoice. This is only enabled
	 * if allowStack is set to true. This limits the number of times an
	 * individual item can stack in a given TransitionChoice.
	 * 
	 * @return The limit (number of times a stackable item may be selected in
	 *         this TransitionChoice)
	 */
	public int getStackLimit()
	{
		return stackLimit == null ? 0 : stackLimit;
	}

	/**
	 * Returns the ChoiceActor for this TransitionChoice.
	 * 
	 * CONSIDER Should look at another method to get rid of this - do the users
	 * of this method require their own sub-class to TransitionChoice?
	 * 
	 * @return The ChoiceActor for this TransitionChoice.
	 */
	public ChoiceActor<T> getChoiceActor()
	{
		return choiceActor;
	}
}
