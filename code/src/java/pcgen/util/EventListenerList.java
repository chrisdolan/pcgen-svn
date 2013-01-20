/*
 * Copyright (c) 2013 Chris Dolan <chris@chrisdolan.net>
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
package pcgen.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * This class exists to remove the dependency on javax.swing.event.EventListenerList.
 */
public class EventListenerList {
	private final List<Object> listeners = new ArrayList<Object>();

	public <T extends EventListener> void add(Class<T> clazz, T listener) {
		listeners.add(listener);
	}

	public <T extends EventListener> T[] getListeners(Class<T> clazz) {
		List<T> out = new ArrayList<T>(listeners.size()/2);
		for (int i=0;i<listeners.size();i+=2)
			if (listeners.get(i) == clazz)
				out.add((T)listeners.get(i+1));
		T[] outArr = (T[])Array.newInstance(clazz, out.size());
		return out.toArray(outArr);
	}

	public <T extends EventListener> void remove(Class<T> clazz, T listener) {
		for (int i=listeners.size(); i > 0; i-=2)
			if (listeners.get(i-2) == clazz && listeners.get(i-2).equals(listener))
				listeners.subList(i-2, i).clear();
	}

	public Object[] getListenerList() {
		return listeners.toArray();
	}
}
