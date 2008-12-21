/*
 * Copyright 2008 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.rules.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pcgen.base.util.DoubleKeyMapToList;
import pcgen.base.util.TripleKeyMapToList;
import pcgen.cdom.base.CDOMObject;
import pcgen.core.prereq.Prerequisite;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.PrerequisiteParserInterface;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenLibrary.PreTokenIterator;
import pcgen.rules.persistence.TokenLibrary.SubTokenIterator;
import pcgen.rules.persistence.TokenLibrary.TokenIterator;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.CDOMSubToken;
import pcgen.rules.persistence.token.CDOMToken;
import pcgen.rules.persistence.util.TokenFamilyIterator;
import pcgen.rules.persistence.util.TokenFamilySubIterator;
import pcgen.util.Logging;

public class TokenSupport
{
	public DoubleKeyMapToList<Class<?>, String, CDOMToken<?>> tokenCache =
		new DoubleKeyMapToList<Class<?>, String, CDOMToken<?>>();

	public TripleKeyMapToList<Class<?>, String, String, CDOMToken<?>> subTokenCache =
		new TripleKeyMapToList<Class<?>, String, String, CDOMToken<?>>();

	public <T extends CDOMObject> boolean processToken(LoadContext context,
		T derivative, String typeStr, String argument)
		throws PersistenceLayerException
	{
		Class<T> cl = (Class<T>) derivative.getClass();
		List<? extends CDOMToken<T>> tokenList = getTokens(cl, typeStr);
		if (tokenList != null)
		{
			for (CDOMToken<T> token : tokenList)
			{
				if (token.parse(context, derivative, argument))
				{
					return true;
				}
				Logging.addParseMessage(Logging.LST_INFO,
					"Failed in parsing typeStr: " + typeStr + " " + argument);
			}
		}
		Logging.addParseMessage(Logging.LST_ERROR, "Illegal Token '" + typeStr
			+ "' '" + argument + "' for " + cl.getName() + " "
			+ derivative.getDisplayName());
		return false;
	}

	public <T extends CDOMObject> List<? extends CDOMToken<T>> getTokens(Class<T> cl, String name)
	{
		List list = tokenCache.getListFor(cl, name);
		if (list == null)
		{
			for (Iterator<? extends CDOMToken<T>> it =
				new TokenIterator<T, CDOMToken<T>>(cl, name); it.hasNext();)
			{
				CDOMToken<T> token = it.next();
				tokenCache.addToListFor(cl, name, token);
			}
			list = tokenCache.getListFor(cl, name);
		}
		return list;
	}

	public <T> List<? extends CDOMToken<T>> getTokens(
		Class<T> cl, String name, String subtoken)
	{
		List list = subTokenCache.getListFor(cl, name, subtoken);
		if (list == null)
		{
			for (Iterator<CDOMSubToken<T>> it =
					new SubTokenIterator<T, CDOMSubToken<T>>(cl, name, subtoken); it
				.hasNext();)
			{
				CDOMToken<T> token = it.next();
				subTokenCache.addToListFor(cl, name, subtoken, token);
			}
			list = subTokenCache.getListFor(cl, name, subtoken);
		}
		return list;
	}

	public <T> boolean processSubToken(LoadContext context, T cdo,
		String tokenName, String key, String value)
		throws PersistenceLayerException
	{
		List<? extends CDOMToken<T>> tokenList = getTokens((Class<T>) cdo.getClass(), tokenName, key);
		if (tokenList != null)
		{
			for (CDOMToken<T> token : tokenList)
			if (token.parse(context, cdo, value))
			{
				return true;
			}
			Logging.addParseMessage(Logging.LST_ERROR,
				"Failed in parsing subtoken: " + key + " of " + value);
		}
		/*
		 * CONSIDER Better option than toString, given that T != CDOMObject
		 */
		Logging.addParseMessage(Logging.LST_ERROR, "Illegal " + tokenName
			+ " subtoken '" + key + "' '" + value + "' for " + cdo.toString());
		return false;
	}

	public <T> String[] unparse(LoadContext context, T cdo, String tokenName)
	{
		char separator = tokenName.charAt(0) == '*' ? ':' : '|';
		Set<String> set = new TreeSet<String>();
		Class<T> cl = (Class<T>) cdo.getClass();
		TokenFamilySubIterator<T> it =
				new TokenFamilySubIterator<T>(cl, tokenName);
		while (it.hasNext())
		{
			CDOMSecondaryToken<? super T> token = it.next();
			String[] s = token.unparse(context, cdo);
			if (s != null)
			{
				for (String aString : s)
				{
					set.add(token.getTokenName() + separator + aString);
				}
			}
		}
		if (set.isEmpty())
		{
			return null;
		}
		return set.toArray(new String[set.size()]);
	}

	public <T> Collection<String> unparse(LoadContext context, T cdo)
	{
		Set<String> set = new TreeSet<String>();
		Class<T> cl = (Class<T>) cdo.getClass();
		TokenFamilyIterator<T> it = new TokenFamilyIterator<T>(cl);
		while (it.hasNext())
		{
			CDOMPrimaryToken<? super T> token = it.next();
			String[] s = token.unparse(context, cdo);
			if (s != null)
			{
				for (String aString : s)
				{
					set.add(token.getTokenName() + ':' + aString);
				}
			}
		}
		if (set.isEmpty())
		{
			return null;
		}
		return set;
	}

	public Prerequisite getPrerequisite(LoadContext context, String key,
		String value) throws PersistenceLayerException
	{
		for (Iterator<PrerequisiteParserInterface> it =
				new PreTokenIterator(key); it.hasNext();)
		{
			PrerequisiteParserInterface token = it.next();
			Prerequisite p = token.parse(key, value, false, false);
			if (p == null)
			{
				Logging.addParseMessage(Logging.LST_ERROR,
					"Failed in parsing Prereq: " + key + " " + value);
			}
			return p;
		}
		Logging.addParseMessage(Logging.LST_ERROR, "Illegal Choice Token '"
			+ key + "' '" + value + "'");
		return null;
	}
}
