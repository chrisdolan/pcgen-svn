/*
 * SpringHelper.java
 * Copyright 2009 (C) James Dempsey
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
 * Created on 16/10/2009 2:38:00 PM
 *
 * $Id$
 */

package pcgen.cdom.helper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import pcgen.cdom.facet.base.AbstractStorageFacet;
import pcgen.util.Logging;

/**
 * The Class <code>SpringHelper</code> is a simple helper for 
 * integrating the Spring framework into PCGen.
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
public class SpringHelper
{
	private static final String APPLICATION_CONTEXT_XML = "applicationContext.xml";
	private static Impl impl = null;

	/**
	 * Initialise the Spring resources. May be called multiple times but 
	 * only the first call will have any effect.
	 */
	private static synchronized Impl getImpl()
	{
		if (impl == null)
		{
//			impl = new ValidatingImpl(new NonSpringImpl(), new SpringImpl());
//			impl = new ValidatingImpl(new SpringImpl(), new NonSpringImpl());
			try
			{
				impl = new SpringImpl();
			}
			catch (Throwable t)
			{
				impl = new NonSpringImpl();
			}
		}
		return impl;
	}
	
	/**
	 * Retrieve a Spring bean based on the class that it implements. Where multiple 
	 * beans implement a class, the first will be returned. 
	 * 
	 * @param cl The type of bean to be retrieved.
	 * @return The bean, or null if none exists.
	 */
	public static <T extends Object> T getBean(Class<T> cl)
	{
		//Logging.errorPrint("getBean(" + cl.getName()+")");
		return getImpl().getBean(cl);
	}
	
	public static Collection<AbstractStorageFacet> getStorageBeans()
	{
		return getImpl().getBeans(AbstractStorageFacet.class);
	}

	private interface Impl {
		<T extends Object> T getBean(Class<T> cl);
		<T> Collection<T> getBeans(Class<T> cl);
		//Collection<AbstractStorageFacet> getStorageBeans();
	}

	private static final class SpringImpl implements Impl
	{
		private final XmlBeanFactory beanFactory;

		private SpringImpl() {
			beanFactory = new XmlBeanFactory(new ClassPathResource(APPLICATION_CONTEXT_XML));
		}

		public <T extends Object> T getBean(Class<T> cl)
		{
			String[] beanNamesForType = beanFactory.getBeanNamesForType(cl);
			if (beanNamesForType.length ==0) 
			{
				return null;
			}
			return (T) beanFactory.getBean (beanNamesForType[0], cl);
		}
		
		public <T> Collection<T> getBeans(Class<T> cl)
		{
			return beanFactory.getBeansOfType(cl).values();
		}
		
	}

	private static class ValidatingImpl implements Impl {
		private final Impl i1;
		private final Impl i2;
		public ValidatingImpl(Impl i1, Impl i2) {
			this.i1 = i1;
			this.i2 = i2;
		}
		public <T> T getBean(Class<T> cl) {
			T b1 = i1.getBean(cl);
			T b2 = i2.getBean(cl);
			deepEquals(b1, b2);
//			asert(b1 == null ? b2 == null : b2 != null);
//			asert(b1 == null || b1.equals(b2));
			return b1;
		}
		public <T> Collection<T> getBeans(Class<T> cl) {
			Collection<T> c1 = i1.getBeans(cl);
			Collection<T> c2 = i2.getBeans(cl);
			deepEquals(c1, c2);
			return c1;
		}
		private void asert(boolean b) {
			if (!b)
				throw new AssertionError();
		}
		private void deepEquals(Object o1, Object o2) {
			deepEquals(o1, o2, new IdentityHashMap<Object, String>());
		}
		private void deepEquals(Object o1, Object o2, Map<Object, String> seen) {
			if (o1 == null)
				asert(o2 == null);
			else {
				asert(o2 != null);
				Class<?> c = o1.getClass();
				asert(c == o2.getClass());
				try {
					if (null != c.getDeclaredMethod("equals", Object.class)) {
						asert(o1.equals(o2));
						return;
					}
				} catch (Exception e) {
				}
				if (null != seen.put(o1, ""))
					return;
				//System.out.println("de " + seen.size() + " = " + o1 + ", " + c);
				checkFields(c, o1, o2, seen);
				seen.remove(o1);
			}
		}
		private void checkFields(Class<?> c, Object o1, Object o2, Map<Object, String> seen) {
			for (Field f : c.getDeclaredFields()) {
				if (f.isSynthetic())
					continue;
				int modifiers = f.getModifiers();
				if (Modifier.isStatic(modifiers))
					continue;
				if ("listeners".equals(f.getName())) // hack
					continue;

				f.setAccessible(true);
				Object v1 = null;
				Object v2 = null;
				try {
					v1 = f.get(o1);
					v2 = f.get(o2);
				} catch (Exception e) {
					e.printStackTrace();
					asert(false);
				}
				//System.out.println("de " + seen.size() + " F=" + f + " x " + o1 + ", " + c);
				if (f.getType().isPrimitive())
					asert(v1.equals(v2));
				else
				    deepEquals(v1, v2, seen);
			}
			Class<?> superclass = c.getSuperclass();
			if (superclass != null && superclass != Object.class)
				checkFields(superclass, o1, o2, seen);
			for (Class<?> intf : c.getInterfaces())
				checkFields(intf, o1, o2, seen);
		}
	}

	private static final class NonSpringImpl implements Impl
	{
		private final Map<String, Bean> registry = new LinkedHashMap<String, Bean>(); // insertion order preserved for predictability

		private static class Bean {
			private String id;
			private Class<?> clazz;
			private Method init;
			private List<Property> properties = new ArrayList<Property>();
			private Object instance;
		}
		private static class Property {
			private String name;
			private String ref;
			private Method setter;
		}

		private NonSpringImpl() {
			openContext();
		}

		public <T> T getBean(Class<T> cl)
		{
			try {
				for (Bean bean : registry.values()) {
					if (cl.isAssignableFrom(bean.clazz))
						return (T) instantiateBean(bean, cl);
				}

//				Bean bean = createBean(cl);
//				registry.put(bean.id, bean);
//				return (T) instantiateBean(bean, cl);
				return null;
			} catch (IOException e) {
				throw new RuntimeException("Failed to instantiate a bean for " + cl, e);
			}
		}

		public <T> Collection<T> getBeans(Class<T> cl)
		{
			List<T> out = new ArrayList<T>();
			for (Bean bean : registry.values()) {
				if (cl.isAssignableFrom(bean.clazz))
					try {
						out.add((T) instantiateBean(bean, cl));
					} catch (IOException e) {
						throw new RuntimeException("Failed to instantiate a bean for " + cl, e);
					}
			}
			return out;
		}

		private void openContext() {
			try {
				InputStream is = getClass().getClassLoader().getResourceAsStream(APPLICATION_CONTEXT_XML);
				if (is == null)
					throw new IOException("Failed to find context XML");
				Document doc;
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					try {
						DocumentBuilder db = dbf.newDocumentBuilder();
						doc = db.parse(is);
					} catch (ParserConfigurationException e) {
						throw new IOException("Failed to set up XML parser", e);
					} catch (SAXException e) {
						throw new IOException("Failed to parse XML", e);
					}
				} finally {
					is.close();
				}
				Logger.getLogger(SpringHelper.class.getName()).info("Loading XML bean definitions from class path resource [" + APPLICATION_CONTEXT_XML + "]");
				readDoc(doc);
			} catch (Exception e) {
				Logging.errorPrint("Failed to read Spring " + APPLICATION_CONTEXT_XML, e);
			}
		}

		private void readDoc(Document doc) throws IOException {
			for (Node node = doc.getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeType() == Node.ELEMENT_NODE && "beans".equals(node.getNodeName())) {
					readBeans((Element)node);
				}
			}
		}
		private void readBeans(Element elem) throws IOException {
			int numBeans = 0;
			for (Node node = elem.getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if ("bean".equals(node.getNodeName())) {
						numBeans++;
						Element childElem = (Element)node;
						String id = childElem.getAttribute("id");
						if (id == null)
							throw new IOException("no id attribute on bean element #" + numBeans);
						registry.put(id, parseBean(id, childElem));
					}
				}
			}
		}

		private Bean parseBean(String id, Element elem) throws IOException {
			Bean bean = new Bean();
			bean.id = id;
			String className = elem.getAttribute("class");
			if (null == className)
				throw new IOException("Bean is missing a class name: " + id);
			try {
				bean.clazz = Class.forName(className);
			} catch (Exception e) {
				throw new IOException("Bean class is missing: " + id + ", " + className, e);
			}
			for (Node node = elem.getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeType() == Node.ELEMENT_NODE && "property".equals(node.getNodeName())) {
					Property p = new Property();
					bean.properties.add(p);
					Element prop = (Element) node;
					p.name = prop.getAttribute("name");
					p.ref = prop.getAttribute("ref");
					if (null == p.name)
						throw new IOException("Bean property is missing a name: " + id);
					if (null == p.ref)
						throw new IOException("Bean property is missing a ref: " + id + ", " + p.name);
					String setterName = "set" + p.name.substring(0, 1).toUpperCase(Locale.US) + p.name.substring(1);
					for (Method m : bean.clazz.getMethods()) {
					    if (setterName.equals(m.getName()) && m.getParameterTypes().length == 1) {
					    	p.setter = m;
					    	break;
					    }
					}
					if (p.setter == null)
						throw new IOException("Failed to find a setter called " + setterName + " in class " + className + " for bean " + id);
				}
			}
			try {
				bean.init = bean.clazz.getMethod("init");
			} catch (Exception e) {
				// ignore
			}
			return bean;
		}

		private <T> Bean createBean(Class<T> cl) {
			Bean bean = new Bean();
			bean.id = cl.getName();
			bean.clazz = cl;
			try {
				bean.init = bean.clazz.getMethod("init");
			} catch (Exception e) {
				// ignore
			}
			return bean;
		}

		private <T> T instantiateBean(Bean bean, Class<T> cl) throws IOException {
			Set<String> seen = new HashSet<String>();
			return instantiateBean(bean, cl, seen);
		}
		private <T> T instantiateBean(Bean bean, Class<T> cl, Set<String> seen) throws IOException {
			if (bean.instance != null)
				return (T) bean.instance;
			if (!seen.add(bean.id))
				throw new IOException("Circular reference in bean " + bean.id + ", " + seen);
			T obj;
			try {
				obj = (T) bean.clazz.newInstance();
			} catch (Exception e) {
				throw new IOException("Bean class constructor failed: " + bean.id + ", " + bean.clazz.getName(), e);
			}
			bean.instance = obj;
			for (Property p : bean.properties) {
				Bean propBean = registry.get(p.ref); 
				if (null == propBean)
					throw new IOException("No bean " +p.ref + " for property " + p.name + " in bean " + bean.id);
				Object value = instantiateBean(propBean, propBean.clazz, seen);
				try {
					p.setter.invoke(obj, value);
				} catch (Exception e) {
					throw new IOException("Failed to invoke a setter called " + p.setter.getName() + " in class " + bean.clazz.getName() + " for bean " + bean.id, e);
				}
			}
			if (null != bean.init) {
				try {
					bean.init.invoke(obj);
				} catch (Exception e) {
					throw new IOException("Failed to invoke init method in class " + bean.clazz.getName() + " for bean " + bean.id, e);
				}
			}
			seen.remove(bean.id);
			return obj;
		}

//		private Object instantiateBean(String id, Set<String> seen) throws IOException {
//			if (!seen.add(id))
//				throw new IOException("Circular reference in bean " + id);
//			Object bean = registry.get(id);
//			if (null == bean)
//				return null;
//			if (bean instanceof Element) {
//				Element elem = (Element) bean;
//				String className = elem.getAttribute("class");
//				if (null == className)
//					throw new IOException("Bean is missing a class name: " + id);
//				Class<?> beanClass;
//				try {
//					beanClass = Class.forName(className);
//					bean = beanClass.newInstance();
//				} catch (ClassNotFoundException e) {
//					throw new IOException("Bean class is missing: " + id + ", " + className, e);
//				} catch (Exception e) {
//					throw new IOException("Bean class constructor failed: " + id + ", " + className, e);
//				}
//				for (Node node = elem.getFirstChild(); node != null; node = node.getNextSibling()) {
//					if (node.getNodeType() == Node.ELEMENT_NODE && "property".equals(node.getNodeName())) {
//						Element prop = (Element) node;
//						String propName = prop.getAttribute("name");
//						String ref = prop.getAttribute("ref");
//						if (null == propName)
//							throw new IOException("Bean property is missing a name: " + id);
//						if (null == ref)
//							throw new IOException("Bean property is missing a ref: " + id + ", " + propName);
//						Object value = instantiateBean(ref, seen);
//						String setterName = "set" + propName.substring(0, 1).toUpperCase(Locale.US) + propName.substring(1);
//						Method setter = null;
//						for (Method m : beanClass.getMethods()) {
//						    if (setterName.equals(m.getName()) && m.getParameterTypes().length == 1) {
//						    	setter = m;
//						    	break;
//						    }
//						}
//						if (setter == null)
//							throw new IOException("Failed to find a setter called " + setterName + " in class " + className + " for bean " + id);
//						try {
//							setter.invoke(bean, value);
//						} catch (Exception e) {
//							throw new IOException("Failed to invoke a setter called " + setterName + " in class " + className + " for bean " + id, e);
//						}
//					}
//				}
//				try {
//					beanClass.getMethod("init").invoke(bean);
//				} catch (NoSuchMethodException e) {
//					// ignore
//				} catch (Exception e) {
//					throw new IOException("Failed to invoke init() in class " + className + " for bean " + id, e);
//				}
//				//registry.put(id, bean);
//			}
//			return bean;
//		}

//		private void processRegistry() throws IOException {
//			Set<String> ids = new HashSet<String>(registry.keySet());
//			Set<String> seen = new HashSet<String>();
//			for (String id : ids) {
//				instantiateBean(id, seen);
//			}
//		}

	}
}
