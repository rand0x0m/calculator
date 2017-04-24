package com.random1312.calculator;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Calculator {

	private static Pattern splitPattern = Pattern.compile(
			"(?<=([+\\-/*\\^()])|(sin)|(cos)|(tan)|(cot)|(sqrt\\d{1,10}+))|(?=([+\\-/*\\^()])|(sin)|(cos)|(tan)|(cot)|(sqrt\\d{1,10}+))");

	public static String getResult(String expression)
			throws InvalidFormatException, CalculationException, InfinityException {
		String[] sp = transformInput(expression), p;

		if (!Checker.checkInput(sp)) {
			throw new InvalidFormatException("Invalid expression.");
		}

		int currentLevel = 0, maxLevel = getMaxLevel(sp);
		int startOfp = 0, endOfp = 0;

		for (int j = maxLevel; j > 0; j--) {
			while (!finishedLevel(j, sp)) {
				currentLevel = 0;

				for (int i = 0; i < sp.length; i++) {
					if (sp[i].equals("(")) {
						currentLevel++;
					} else if (sp[i].equals(")")) {
						currentLevel--;
					}

					if (currentLevel == j) {
						if (sp[i].equals("(")) {
							startOfp = i;
						}
					} else if (currentLevel == j - 1 && sp[i].equals(")")) {
						endOfp = i;
						p = Arrays.copyOfRange(sp, startOfp, endOfp + 1);
						sp = concat(new String[][] { Arrays.copyOfRange(sp, 0, startOfp), getParenthesisResult(p),
								Arrays.copyOfRange(sp, endOfp + 1, sp.length) });
						break;
					}
				}
			}
		}

		if (Checker.checkResult(sp)) {
			return sp[0];
		} else {
			throw new CalculationException("Unable to calculate result.");
		}
	}

	private static String[] getParenthesisResult(String[] p) throws InfinityException {
		p = Arrays.copyOfRange(p, 1, p.length - 1);

		if (p.length == 1) {
			return p;
		}

		String[] operatorsPatterns = new String[] { "(sin)|(cos)|(tan)|(cot)", "[\\^]", "[/*]", "[+\\-]" };

		for (String operatorPattern : operatorsPatterns) {
			while (!finishedOperatorsCalculation(p, operatorPattern)) {
				for (int i = 0; i < p.length; i++) {
					if (p[i].matches(operatorPattern)) {
						p = getValue(p, i);
					}
				}
			}
		}

		return p;
	}

	private static String[] getValue(String[] p, int i) throws InfinityException {
		double arg1 = 0, arg2 = 0, result = 0;
		
		arg2 = Double.parseDouble(p[i + 1]);
		if (!p[i].matches("(sin)|(cos)|(tan)|(cot)")) {
			arg1 = Double.parseDouble(p[i - 1]);
		}

		switch (p[i]) {
		case "sin":
			result = Math.sin(arg1);
			break;
		case "cos":
			result = Math.cos(arg1);
			break;
		case "tan":
			result = Math.tan(arg1);
			break;
		case "cot":
			result = 1 / Math.tan(arg1);
			break;
		case "^":
			result = Math.pow(arg1, arg2);
			break;
		case "/":
			result = arg1 / arg2;
			break;
		case "*":
			result = arg1 * arg2;
			break;
		case "+":
			result = arg1 + arg2;
			break;
		case "-":
			result = arg1 - arg2;
			break;
		}

		if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
			throw new InfinityException("Infinity calculations not supported.");
		}

		if (!p[i].matches("(sin)|(cos)|(tan)|(cot)")) {
			if (i == 1) {
				if (p.length == 1) {
					p = new String[] { String.valueOf(result) };
				} else {
					p = concat(new String[][] { new String[] { String.valueOf(result) },
							Arrays.copyOfRange(p, i + 2, p.length) });
				}
			} else if (i == p.length - 2) {
				p = concat(new String[][] { Arrays.copyOfRange(p, 0, i - 1), new String[] { String.valueOf(result) } });
			} else {
				p = concat(new String[][] { Arrays.copyOfRange(p, 0, i - 1), new String[] { String.valueOf(result) },
						Arrays.copyOfRange(p, i + 2, p.length) });
			}
		} else {
			if (i == 0) {
				if (p.length == 2) {
					p = new String[] { String.valueOf(result) };
				} else {
					p = concat(new String[][] { new String[] { String.valueOf(result) },
							Arrays.copyOfRange(p, i + 2, p.length) });
				}
			} else if (i == p.length - 1) {
				p = concat(new String[][] { Arrays.copyOfRange(p, 0, i), new String[] { String.valueOf(result) } });
			} else {
				p = concat(new String[][] { Arrays.copyOfRange(p, 0, i), new String[] { String.valueOf(result) },
						Arrays.copyOfRange(p, i + 2, p.length) });
			}
		}

		return p;
	}

	private static boolean finishedLevel(int j, String[] sp) {
		int currentLevel = 0;
		for (String s : sp) {
			if (s.equals("(")) {
				currentLevel++;
				if (currentLevel == j) {
					return false;
				}
			} else if (s.equals(")")) {
				currentLevel--;
			}
		}

		return true;
	}

	private static boolean finishedOperatorsCalculation(String[] s, String symbolsRegex) {
		for (int i = 0; i < s.length; i++) {
			if (s[i].matches(symbolsRegex)) {
				return false;
			}
		}

		return true;
	}

	private static int getMaxLevel(String[] sp) {
		int currentLevel = 0, maxLevel = 0;
		for (String s : sp) {
			if (s.equals("(")) {
				currentLevel++;
				maxLevel += (currentLevel > maxLevel) ? 1 : 0;
			} else if (s.equals(")")) {
				currentLevel--;
			}
		}

		return maxLevel;
	}

	private static String[] transformInput(String input) {
		String[] sp = input.trim().split(splitPattern.toString());
		sp = transformInts(sp);
		sp = transformSqrts(sp);
		sp = transformTrigonometricals(sp);
		sp = transformMultiplications(sp);
		sp = smallTransformations(sp);

		return sp;
	}

	private static String[] smallTransformations(String[] sp) {
		/* Transform -() to 0-() */
		if (sp[0].equals("-")) {
			sp = concat(new String[][] { new String[] { "0", "-" }, sp });
		} else if (sp[0].equals("+")) {
			sp = concat(new String[][] { new String[] { "0", "+" }, sp });
		}

		/* Add parenthesis in the end and in the start if missing */
		if (!(sp[0].equals("(") && sp[sp.length - 1].equals(""))) {
			sp = concat(new String[][] { new String[] { "(" }, sp, new String[] { ")" } });
		}

		return sp;
	}

	private static String[] transformInts(String[] sp) {
		for (int i = 0; i < sp.length; i++) {
			if (sp[i].matches("\\d+")) {
				sp[i] += ".0";
			}
		}

		return sp;
	}

	private static String[] transformSqrts(String[] sp) {
		for (int i = 0; i < sp.length; i++) {
			if (sp[i].matches("sqrt\\d{1,10}+")) {
				String[] args = sp[i].split("(?<=sqrt)");
				sp = concat(new String[][] { Arrays.copyOfRange(sp, 0, i), Arrays.copyOfRange(sp, i + 1, i + 4),
						new String[] { "^" }, new String[] { "(" }, new String[] { "1" }, new String[] { "/" },
						new String[] { args[1] }, new String[] { ")" }, Arrays.copyOfRange(sp, i + 4, sp.length) });
			}
		}

		return sp;
	}

	private static String[] transformTrigonometricals(String[] sp) {
		for (int i = 0; i < sp.length; i++) {
			if (sp[i].matches("(sin)|(cos)|(tan)|(cot)")) {
				if (sp[i + 1].matches("\\d+")) {
					if (i == sp.length - 2) {
						sp = concat(new String[][] { Arrays.copyOfRange(sp, 0, i), new String[] { "(" },
								new String[] { sp[i + 1] }, new String[] { ")" } });
					} else {
						sp = concat(new String[][] { Arrays.copyOfRange(sp, 0, i), new String[] { "(" },
								new String[] { sp[i + 1] }, new String[] { ")" },
								Arrays.copyOfRange(sp, i + 2, sp.length) });
					}
				}
			}
		}

		return sp;
	}

	private static String[] transformMultiplications(String[] sp) {
		for (int i = 0; i < sp.length; i++) {
			try {
				if ((sp[i].equals("(") && sp[i - 1].matches("(\\d+\\.\\d+)|(\\d+)"))
						|| (sp[i].equals(")") && sp[i + 1].matches("(\\d+\\.\\d+)|(\\d+)"))) {
					sp = concat(new String[][] { Arrays.copyOfRange(sp, 0, i), new String[] { "*" },
							Arrays.copyOfRange(sp, i, sp.length) });
				} else if (sp[i].equals(")") && sp[i + 1].equals("(")) {
					sp = concat(new String[][] { Arrays.copyOfRange(sp, 0, i + 1), new String[] { "*" },
							Arrays.copyOfRange(sp, i + 1, sp.length) });
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}

		return sp;
	}

	private static String[] concat(String[][] arrays) {
		int length = 0;

		for (int i = 0; i < arrays.length; i++) {
			length += arrays[i].length;
		}

		String[] concatenated = new String[length];

		int counter = 0;
		for (int i = 0; i < arrays.length; i++) {
			for (int j = 0; j < arrays[i].length; j++) {
				concatenated[counter++] = arrays[i][j];
			}
		}

		return concatenated;
	}
}
