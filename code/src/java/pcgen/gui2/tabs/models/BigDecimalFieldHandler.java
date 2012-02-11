/*
 * FormattedFieldHandler.java
 * Copyright 2011 Connor Petty <cpmeister@users.sourceforge.net>
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
 * Created on Oct 1, 2011, 4:46:28 PM
 */
package pcgen.gui2.tabs.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JFormattedTextField;
import pcgen.core.facade.ReferenceFacade;
import pcgen.core.facade.event.ReferenceEvent;
import pcgen.core.facade.event.ReferenceListener;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public abstract class BigDecimalFieldHandler implements PropertyChangeListener, ReferenceListener<BigDecimal>
{

	private JFormattedTextField field;
	private ReferenceFacade<BigDecimal> ref;

	public BigDecimalFieldHandler(JFormattedTextField field, ReferenceFacade<BigDecimal> ref)
	{
		this.field = field;
		this.ref = ref;
	}

	public JFormattedTextField getFormattedTextField()
	{
		return field;
	}

	/**
	 * Attach the handler to the screen field. e.g. When the character is
	 * made active.
	 */
	public void install()
	{
		field.setValue(ref.getReference());
		field.addPropertyChangeListener(this);
		ref.addReferenceListener(this);
	}

	/**
	 * Detach the handler from the on screen field. e.g. when the
	 * character is no longer being displayed.
	 */
	public void uninstall()
	{
		field.removePropertyChangeListener(this);
		ref.removeReferenceListener(this);
	}

	public void referenceChanged(ReferenceEvent<BigDecimal> e)
	{
		BigDecimal newVal = e.getNewReference();
		BigDecimal oldVal = new BigDecimal(((Number) field.getValue()).doubleValue());
		if (oldVal.compareTo(newVal) != 0)
		{
			field.setValue(newVal);
		}
	}

	protected abstract void valueChanged(BigDecimal value);

	public void propertyChange(PropertyChangeEvent evt)
	{
		valueChanged(new BigDecimal(((Number) field.getValue()).doubleValue()));
	}

}
