/*
 * DamageReductionTest.java
 *
 * Copyright 2006 (C) Aaron Divinsky <boomer70@yahoo.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	   See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Current Ver: $Revision: $
 *
 * Last Editor: $Author: $
 *
 * Last Edited: $Date:  $
 *
 */
package pcgen.cdom.facet;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.content.DamageReduction;

/**
 * This class tests the handling of DRs in PCGen
 */
@SuppressWarnings("nls")
public class DamageReductionFacetTest extends TestCase
{
	/**
	 * Test the retrieval of the DR String
	 */
	public void testGetDRString()
	{
		DamageReductionFacet drFacet = FacetLibrary.getFacet(DamageReductionFacet.class);

		Map<DamageReduction, Set<Object>> drList = new IdentityHashMap<DamageReduction, Set<Object>>();
		String listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"");
		Set<Object> sourceSet = new HashSet<Object>();
		sourceSet.add(new Object());

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"10/magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"10/good and magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult, "10/good and magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"10/good and magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"10/good and magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"10/good and magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(15), "Good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "evil"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic or good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic or good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic or good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(5), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/Good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic or good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good; 10/magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(15), "magic"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic or good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(15), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic or lawful"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(15), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic and good"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 5/evil");

		drList.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "magic and lawful"), sourceSet);
		listResult = drFacet.getDRString(null, drList);
		assertEquals(listResult,"15/good and magic; 10/lawful; 5/evil");

		Map<DamageReduction, Set<Object>> drList1 = new IdentityHashMap<DamageReduction, Set<Object>>();
		drList1.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "epic"), sourceSet);
		drList1.put(new DamageReduction(FormulaFactory.getFormulaFor(10), "lawful or good"), sourceSet);
		listResult = drFacet.getDRString(null, drList1);
		assertEquals(listResult,"10/epic; 10/lawful or good");

		drList1.clear();
		drList1.put(new DamageReduction(FormulaFactory.getFormulaFor(10),
			"epic and good or epic and lawful"), sourceSet);
		listResult = drFacet.getDRString(null, drList1);
		assertEquals(listResult,"10/epic and good or epic and lawful");

		// TODO Better consolidation: Can't handle this case at the moment.
		//		drList1.add(new DamageReduction(FormulaFactory.getFormulaFor(10), "lawful"));
		//		listResult = drFacet.getDRString(null, drList1);
		//		System.out.println("DR List: " + drList1.toString() + " = " + listResult);
		//		assertEquals(listResult,"10/epic and lawful");
	}
}
