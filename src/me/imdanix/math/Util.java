package me.imdanix.math;

import java.util.regex.Pattern;

public class Util {
	private final static Pattern FLOAT = Pattern.compile("-?\\d+(\\.\\d+)?");

	private Util() {
	}

	public static boolean isStringEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static String removeSpaces(String str) {
		StringBuilder bld = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c != ' ') bld.append(c);
		}
		return bld.toString();
	}

	public static double getDouble(String str, double def) {
		if(isStringEmpty(str) || !FLOAT.matcher(str).matches()) return def;
		return Double.parseDouble(str);
	}
}
