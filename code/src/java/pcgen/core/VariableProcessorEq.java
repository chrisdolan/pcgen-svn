/*
 * VariableProcessorEq.java
 * Copyright 2004 (C) Chris Ward <frugal@purplewombat.co.uk>
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
 * Created on 13-Dec-2004
 */
package pcgen.core;

import pcgen.core.spell.Spell;
import pcgen.core.term.TermEvaluator;
import pcgen.core.term.EvaluatorFactory;

/**
 * <code>VariableProcessorEq</code> is a processor for variables
 * associated with a character's equipment. This class converts
 * formulas or variables into values and is used extensively
 * both in defintions of objects and for output to output sheets.
 *
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 * @author Chris Ward <frugal@purplewombat.co.uk>
 * @version $Revision$
 */
public class VariableProcessorEq extends VariableProcessor
{

	private Equipment eq;
	private boolean primaryHead;

	/**
	 * Create a new VariableProcessorEq instance for an equipment item, and
	 * pc. It also allows splitting of the processing of the heads of double
	 * weapons.
	 *
	 * @param eq
	 *           The item of equipment  being processed.
	 * @param pc 
	 *           The player character being processed.
	 * @param primaryHead
	 *           Is this the primary head of a double weapon?
	 */
	public VariableProcessorEq(
			Equipment eq,
			PlayerCharacter pc,
			boolean primaryHead)
	{
		super(pc);
		this.eq = eq;
		this.primaryHead = primaryHead;
	}

	/**
	 * Retrieve a pre-coded variable for a piece of equipment. These are known
	 * properties of all equipment items. If a value is not found for the
	 * equipment item, a search will be made of the character.
	 *
	 * @param aSpell  
	 *              This is specifically to compute bonuses to CASTERLEVEL
	 *              for a specific spell.
	 * @param valString
	 *              The variable to be evaluated
	 * @param src
	 *              The source within which the variable is evaluated
	 * @return The value of the variable
	 */

	Float getInternalVariable(
			final Spell aSpell,
			String valString,
			final String src)
	{
		TermEvaluator t1 = getTermEvaluator(valString, src);

		Float fResult;
		if (t1 != null)
		{
			fResult = t1.resolve(eq, primaryHead, pc);
		}
		else
		{
			fResult = 0f;
		}

		return t1 == null ? null : fResult;
	}
	
	TermEvaluator getTermEvaluator(String valString, String src)
	{
		TermEvaluator t1 = EvaluatorFactory.EQ.getTermEvaluator(valString, src);

		if (t1 == null)
		{
			return EvaluatorFactory.PC.getTermEvaluator(valString, src);
		}
		else
		{
			return t1;
		}
	}
}
