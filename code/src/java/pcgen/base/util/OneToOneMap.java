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
package pcgen.base.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Creates a Map which is intended to only possess a given Key or Value one
 * time. Each Key references a single Value and each Value may only be
 * referenced by a single Key. In this way, any Value can be determined uniquely
 * for any Key and any Key can be determined uniquely for any Value.
 */
public class OneToOneMap<K, V>
{
	/**
	 * The underlying map used to store references from the Keys to the Values.
	 */
	private final HashMap<K, V> forwardMap = new HashMap<K, V>();

	/**
	 * The underlying map used to store references from the Values back to the
	 * Keys.
	 */
	private final HashMap<V, K> reverseMap = new HashMap<V, K>();

	/**
	 * Clears the OneToOneMap (removes all keys and values)
	 */
	public void clear()
	{
		forwardMap.clear();
		reverseMap.clear();
	}

	/**
	 * Returns true if the OneToOneMap contains the given Key.
	 * 
	 * @param key
	 *            the Key to be tested to determine if it is present in the
	 *            OneToOneMap
	 * @return true if the OneToOneMap contains the given Key; false otherwise
	 */
	public boolean containsKey(Object key)
	{
		return forwardMap.containsKey(key);
	}

	/**
	 * Returns true if the OneToOneMap contains the given Value.
	 * 
	 * @param value
	 *            the Value to be tested to determine if it is present in the
	 *            OneToOneMap
	 * @return true if the OneToOneMap contains the given Value; false otherwise
	 */
	public boolean containsValue(Object value)
	{
		return reverseMap.containsKey(value);
	}

	/**
	 * Returns the Value in the OneToOneMap for the given Key.
	 * 
	 * @param key
	 *            the Key for which the Value should be returned
	 * @return V the Value stored in the OneToOneMap for the given Key; null if
	 *         the given Key is not contained within the OneToOneMap
	 */
	public V get(Object key)
	{
		return forwardMap.get(key);
	}

	/**
	 * Returns the Key in the OneToOneMap for the given Value.
	 * 
	 * @param key
	 *            the Value for which the Key should be returned
	 * @return V the Key in the OneToOneMap for the given Value; null if the
	 *         given Value is not contained within the OneToOneMap
	 */
	public K getKeyFor(Object key)
	{
		return reverseMap.get(key);
	}

	/**
	 * Returns true if the OneToOneMap is empty; false otherwise
	 * 
	 * @return true if the OneToOneMap is empty; false otherwise
	 */
	public boolean isEmpty()
	{
		return forwardMap.isEmpty();
	}

	/**
	 * Returns a Set of the keys for this OneToOneMap
	 * 
	 * Note: This Set is reference-semantic. The ownership of the Set is
	 * transferred to the calling Object; therefore, changes to the returned Set
	 * will NOT impact the OneToOneMap.
	 * 
	 * @return A Set of the keys for this OneToOneMap
	 */
	public Set<K> keySet()
	{
		return new HashSet<K>(forwardMap.keySet());
	}

	/**
	 * Put the given value into this OneToOneMap for the given key.
	 * 
	 * If this OneToOneMap already contained a mapping for the given key, the
	 * previous value is returned. Otherwise, null is returned.
	 * 
	 * If this OneToOneMap already contained a key mapping to the given value,
	 * the previous mapping is destroyed without warning.
	 * 
	 * @param key
	 *            The key for storing the given value
	 * @param value
	 *            The value to be stored for the given key
	 * @return Object The previous value stored for the given key; null if the
	 *         given key did not previously have a mapping
	 */
	public V put(K key, V value)
	{
		K oldKey = reverseMap.put(value, key);
		forwardMap.remove(oldKey);
		return forwardMap.put(key, value);
	}

	/**
	 * Copies the key/value combinations from the given Map into this
	 * OneToOneMap.
	 * 
	 * If this OneToOneMap already contained a mapping for the any of the key
	 * combinations in the given OneToOneMap, the previous value is overwritten.
	 * 
	 * If the given Map contains a Key or Value more than once, then the *last*
	 * instance of that Key or Value in the Map (as determined by the iterator
	 * of the given Map) will be stored in the OneToOneMap.
	 * 
	 * @param dkm
	 *            The Map for which the key/value combinations should be placed
	 *            into this OneToOneMap
	 * @throws NullPointerException
	 *             if the given Map is null
	 */
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for (Map.Entry<? extends K, ? extends V> me : m.entrySet())
		{
			put(me.getKey(), me.getValue());
		}
	}

	/**
	 * Removes the value from OneToOneMap for the given key. Returns the value
	 * that was removed from the OneToOneMap. If this OneToOneMap did not have a
	 * mapping for the given key, null is returned.
	 * 
	 * @param key1
	 *            The primary key for retrieving the given value
	 * @param key2
	 *            The secondary key for retrieving the given value
	 * @return Object The value previously mapped to the given keys
	 */
	public V remove(Object key)
	{
		V value = forwardMap.remove(key);
		reverseMap.remove(value);
		return value;
	}

	/**
	 * Returns the number of entries (key-value pairs) in the OneToOneMap
	 * 
	 * @return the number of entries (key-value pairs) in the OneToOneMap
	 */
	public int size()
	{
		return forwardMap.size();
	}

	/**
	 * Returns a Collection of the values for this OneToOneMap
	 * 
	 * Note: This Collection is reference-semantic. The ownership of the
	 * Collection is transferred to the calling Object; therefore, changes to
	 * the returned Collection will NOT impact the OneToOneMap.
	 * 
	 * @return A Collection of the values for this OneToOneMap
	 */
	public Collection<V> values()
	{
		return new HashSet<V>(reverseMap.keySet());
	}

	/**
	 * Returns a String representation of this OneToOneMap, primarily for
	 * purposes of debugging. It is strongly advised that no dependency on this
	 * method be created, as the return value may be changed without warning.
	 */
	@Override
	public String toString()
	{
		return "OneToOneMap: " + forwardMap.toString();
	}

}
