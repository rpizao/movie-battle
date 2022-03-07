package br.com.rpizao.utils;

public final class NumberUtils {

	public static Integer toInteger(String value) {
		return Integer.valueOf(value.replace(",", ""));
	}
	
	public static Double toFloat(String value) {
		return Double.valueOf(value);
	}
	
	public static boolean isNumeric(String value) {
		try {
			final Double parsed = toFloat(value);
			return !parsed.isNaN();
			
		} catch (Exception e) {
			return false;
		}
	}
	
}
