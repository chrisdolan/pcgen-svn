/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.kit.basekit;

import org.junit.Test;

import pcgen.core.kit.BaseKit;
import pcgen.core.kit.KitGear;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMSubLineLoader;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import plugin.lsttokens.testsupport.AbstractSubTokenTestCase;

public class OptionTokenTest extends AbstractSubTokenTestCase<BaseKit>
{

	static OptionToken token = new OptionToken();
	static CDOMSubLineLoader<BaseKit> loader = new CDOMSubLineLoader<BaseKit>(
			"*KITTOKEN", "SKILL", BaseKit.class);

	@Override
	public Class<KitGear> getCDOMClass()
	{
		return KitGear.class;
	}

	@Override
	public CDOMSubLineLoader<BaseKit> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMSecondaryToken<BaseKit> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidInputTrailing() throws PersistenceLayerException
	{
		assertFalse(parse("Formula,"));
	}

	@Test
	public void testInvalidInputStarting() throws PersistenceLayerException
	{
		assertFalse(parse(",Formula"));
	}

	@Test
	public void testInvalidInputDouble() throws PersistenceLayerException
	{
		assertFalse(parse("Start,,Formula"));
	}

	@Test
	public void testRoundRobinSimple() throws PersistenceLayerException
	{
		runRoundRobin("MinFormula");
	}

	@Test
	public void testRoundRobinTwo() throws PersistenceLayerException
	{
		runRoundRobin("Exact" + getJoinCharacter() + "AnotherExact");
	}

	@Test
	public void testRoundRobinComplex() throws PersistenceLayerException
	{
		runRoundRobin("1,4" + getJoinCharacter() + "56,Formula");
	}

	@Test
	public void testInvalidListEnd() throws PersistenceLayerException
	{
		assertFalse(parse("TestWP1" + getJoinCharacter()));
	}

	private char getJoinCharacter()
	{
		return '|';
	}

	@Test
	public void testInvalidListStart() throws PersistenceLayerException
	{
		assertFalse(parse(getJoinCharacter() + "TestWP1"));
	}

	@Test
	public void testInvalidListDoubleJoin() throws PersistenceLayerException
	{
		assertFalse(parse("TestWP2" + getJoinCharacter() + getJoinCharacter()
				+ "TestWP1"));
	}
}
