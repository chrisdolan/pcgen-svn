/*
 * Copyright (c) 2012 Tom Parker <thpr@users.sourceforge.net>
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
package tokenmodel;

import org.junit.Test;

import pcgen.core.Language;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.token.CDOMToken;
import pcgen.rules.persistence.token.ParseResult;
import plugin.lsttokens.template.ChooseLangautoToken;
import tokenmodel.testsupport.AbstractTokenModelTest;

public class TemplateChooseLangautoTest extends AbstractTokenModelTest
{

	@Test
	public void testSimple() throws PersistenceLayerException
	{
		PCTemplate source = create(PCTemplate.class, "Source");
		Language granted = create(Language.class, "Granted");
		ParseResult result = token.parseToken(context, source, "Granted");
		if (result != ParseResult.SUCCESS)
		{
			result.printMessages();
			fail("Test Setup Failed");
		}
		finishLoad();
		assertEquals(0, languageFacet.getCount(id));
		templateFacet.add(id, source, this);
		assertTrue(languageFacet.contains(id, granted));
		assertEquals(1, languageFacet.getCount(id));
		templateFacet.remove(id, source, this);
		assertEquals(0, languageFacet.getCount(id));
	}

	ChooseLangautoToken token = new ChooseLangautoToken();

	@Override
	public CDOMToken<?> getToken()
	{
		return token;
	}

}