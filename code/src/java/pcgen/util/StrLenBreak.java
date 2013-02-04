package pcgen.util;

import java.lang.reflect.Field;

public class StrLenBreak {
	private static final Field f;
	static {
		try {
			f = String.class.getDeclaredField("value");
			f.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static boolean isInterned(String s) {
		try {
			char[] v = (char[]) f.get(s);
			if (v.length != s.length()) {
				System.out.println("non-interned string! " + s.length() + " vs. " + v.length);
				System.out.println("str = " + s);
				if (v.length > 1000)
					System.out.println("URGENT!");
				System.out.println("parent = " + new String(v).substring(0, Math.min(200, v.length)));
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static boolean isInterestingSubstring(String s, int beginIndex, int endIndex) {
		if (0 == beginIndex && 3 == endIndex && s.startsWith("VAR")) {
			StackTraceElement[] stack = new Exception().getStackTrace();
			if (stack[3].getClassName().equals("plugin.pretokens.parser.PreVariableParser") && stack[3].getMethodName().equals("parse"))
				return false;
			return true;
		}
		return false;
//		"VAR".equals(substring(arg0,arg1))
//		&&
//		!new Exception().getStackTrace()[3].getMethodName()
//		.equals("newBonus")
//		&&
//		!new Exception().getStackTrace()[1].getMethodName()
//		.equals("processToken")
//		&&
//		!new Exception().getStackTrace()[2].getMethodName()
//		.equals("buildDependMap")
//		&&
//		!new Exception().getStackTrace()[3].getClassName()
//		.equals("plugin.pretokens.parser.PreVariableParser")
		
	}
}
