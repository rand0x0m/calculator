package com.random1312.calculator;

import java.util.regex.Pattern;

public class Checker {

	private static Pattern checkPattern = Pattern
			.compile("(\\d+\\.\\d+)|(\\d+)|([+\\-/*\\^()])|(sin)|(cos)|(tan)|(cot)");

	public static boolean checkInput(String[] s) {
		int op = 0, cp = 0;

		for (int i = 0; i < s.length; i++) {
			if (!s[i].matches(checkPattern.toString())) {
				return false;
			}

			if (s[i].matches("[+\\-/*\\^]") && s[i + 1].matches("[+\\-/*\\^]")) {
				return false;
			}

			if (s[i].equals("(")) {
				op++;

				if (s[i + 1].equals(")")) {
					return false;
				}
			} else if (s[i].equals(")")) {
				cp++;
			} else if (s[i].matches("sqrt")) {
				return false;
			} else if (s[i].matches("(sin)|(cos)|(tan)|(cot)")
					&& !s[i + 1].matches("\\(")) {
				return false;
			}

			if (s[i].matches("(\\d+\\.\\d+)|(\\d+)")) {
				try {
					double number = Double.parseDouble(s[i]);
					if (Double.isInfinite(number)) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}

		if (op != cp) {
			return false;
		}

		return true;
	}

	public static boolean checkResult(String[] s) throws CalculationException {

		if (s.length != 1) {
			return false;
		} else {
			return true;
		}
	}
}