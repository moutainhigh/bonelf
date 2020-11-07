package com.bonelf.cicada.util;

/**
 *
 */
public class StringUtil {

	/**
	 * 符号连接方式转驼峰
	 * @param name
	 * @param symbol
	 * @return
	 */
	public static String symbol2CamelCase(CharSequence name, Character symbol) {
		if (null == name) {
			return null;
		} else {
			String name2 = name.toString();
			if (name2.contains(symbol.toString())) {
				StringBuilder sb = new StringBuilder(name2.length());
				boolean upperCase = false;
				for (int i = 0; i < name2.length(); ++i) {
					char c = name2.charAt(i);
					if (c == symbol) {
						upperCase = true;
					} else if (upperCase) {
						sb.append(Character.toUpperCase(c));
						upperCase = false;
					} else {
						sb.append(Character.toLowerCase(c));
					}
				}
				return sb.toString();
			} else {
				return name2;
			}
		}
	}

	/**
	 * 横杠连接方式转驼峰
	 * @param name
	 * @return
	 */
	public static String crossbarCase2CamelCase(CharSequence name) {
		return symbol2CamelCase(name, '-');
	}
}
