/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.qualifier.skill;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.ChooseInformation;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SkillCost;
import pcgen.core.PCClass;
import pcgen.core.Skill;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.QualifierToken;
import plugin.lsttokens.ChooseLst;
import plugin.lsttokens.choose.SkillToken;
import plugin.lsttokens.testsupport.AbstractQualifierTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.TokenRegistration;
import plugin.lsttokens.testsupport.TransparentPlayerCharacter;

public class CrossClassQualifierTokenTest extends
		AbstractQualifierTokenTestCase<CDOMObject, Skill>
{

	static ChooseLst token = new ChooseLst();
	static SkillToken subtoken = new SkillToken();
	static CDOMTokenLoader<CDOMObject> loader = new CDOMTokenLoader<CDOMObject>(
			CDOMObject.class);
	private Skill s1, s2, s3, s4, s5;
	private PCClass cl1;

	private static final CrossClassToken CROSSCLASS_TOKEN = new CrossClassToken();

	public CrossClassQualifierTokenTest()
	{
		super("CROSSCLASS", null);
	}

	@Override
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		TokenRegistration.register(CROSSCLASS_TOKEN);
	}

	@Override
	public CDOMSecondaryToken<?> getSubToken()
	{
		return subtoken;
	}

	@Override
	public Class<Skill> getTargetClass()
	{
		return Skill.class;
	}

	@Override
	public Class<Skill> getCDOMClass()
	{
		return Skill.class;
	}

	@Override
	public CDOMLoader<CDOMObject> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<CDOMObject> getToken()
	{
		return token;
	}

	@Override
	protected boolean allowsNotQualifier()
	{
		return true;
	}

		@Test
	public void testGetSet() throws PersistenceLayerException
	{
		setUpPC();
		TransparentPlayerCharacter pc = new TransparentPlayerCharacter();
		initializeObjects();
		assertTrue(parse(getSubTokenName() + "|CROSSCLASS[ALL]"));

		finishLoad();

		ChooseInformation<?> info = primaryProf.get(ObjectKey.CHOOSE_INFO);
		pc.classSet.add(cl1);
		Collection<?> set = info.getSet(pc);
		assertFalse(set.isEmpty());
		assertEquals(4, set.size());
		assertTrue(set.contains(s1));
		assertTrue(set.contains(s2));
		assertTrue(set.contains(s3));
		assertTrue(set.contains(primaryProf));
		pc.skillSet.put(s1, 2);
		pc.skillSet.put(s2, 0);
		set = info.getSet(pc);
		assertEquals(4, set.size());
		assertTrue(set.contains(s1));
		assertTrue(set.contains(s2));
		assertTrue(set.contains(s3));
		assertTrue(set.contains(primaryProf));
		pc.skillCostMap.put(s2, cl1, SkillCost.CLASS);
		pc.skillCostMap.put(s4, cl1, SkillCost.CROSS_CLASS);
		set = info.getSet(pc);
		assertFalse(set.isEmpty());
		assertEquals(4, set.size());
		assertTrue(set.contains(s1));
		assertTrue(set.contains(s3));
		assertTrue(set.contains(s4));
		assertTrue(set.contains(primaryProf));
	}

	@Test
	public void testGetSetFiltered() throws PersistenceLayerException
	{
		setUpPC();
		TransparentPlayerCharacter pc = new TransparentPlayerCharacter();
		initializeObjects();
		assertTrue(parse(getSubTokenName() + "|CROSSCLASS[TYPE=Masterful]"));

		finishLoad();

		ChooseInformation<?> info = primaryProf.get(ObjectKey.CHOOSE_INFO);
		pc.classSet.add(cl1);
		Collection<?> set = info.getSet(pc);
		assertEquals(2, set.size());
		assertTrue(set.contains(s2));
		assertTrue(set.contains(s3));
		pc.skillCostMap.put(s2, cl1, SkillCost.CLASS);
		set = info.getSet(pc);
		assertFalse(set.isEmpty());
		assertEquals(1, set.size());
		assertTrue(set.contains(s3));
		pc.skillCostMap.put(s4, cl1, SkillCost.CROSS_CLASS);
		pc.skillCostMap.put(s5, cl1, SkillCost.CROSS_CLASS);
		set = info.getSet(pc);
		assertFalse(set.isEmpty());
		assertEquals(2, set.size());
		assertTrue(set.contains(s3));
		assertTrue(set.contains(s4));
	}

	private void initializeObjects()
	{
		s1 = new Skill();
		s1.setName("s1");
		primaryContext.ref.importObject(s1);

		s2 = new Skill();
		s2.setName("s2");
		primaryContext.ref.importObject(s2);
		primaryContext.unconditionallyProcess(s2, "TYPE", "Masterful");

		s3 = new Skill();
		s3.setName("s3");
		primaryContext.ref.importObject(s3);
		primaryContext.unconditionallyProcess(s3, "TYPE", "Masterful");

		s4 = new Skill();
		s4.setName("s4");
		s4.put(ObjectKey.EXCLUSIVE, Boolean.TRUE);
		primaryContext.unconditionallyProcess(s4, "TYPE", "Masterful");
		primaryContext.ref.importObject(s4);

		s5 = new Skill();
		s5.setName("s5");
		s5.put(ObjectKey.EXCLUSIVE, Boolean.TRUE);
		primaryContext.ref.importObject(s5);

		cl1 = new PCClass();
		cl1.setName("MyClass");
		primaryContext.ref.importObject(cl1);
	}

	@Override
	protected Class<? extends QualifierToken<?>> getQualifierClass()
	{
		return plugin.qualifier.skill.CrossClassToken.class;
	}
}
