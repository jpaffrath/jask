package helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Keyword class
 *
 * @author Julius Paffrath
 *
 */
public class Keywords {
	private static List<String> calculations = new ArrayList<String>(Arrays.asList("plus", "minus", "times", "divide"));
	private static List<String> operators  = new ArrayList<String>(Arrays.asList("store", "assign", "mod", "equals", "unequals", "greater", "smaller",
			                                                                     "greaterequals", "smallerequals", "increment", "decrement", "call"));
	private static List<String> keywords  = new ArrayList<String>(Arrays.asList("function", "end", "if", "else", "endif", "return", "convert", "to",
			                                                                    "number", "string", "in", "run", "with", "while", "for", "endrun", "struct",
			                                                                    "endstruct"));

	/**
	 * Checks if a given string is a jask calculation
	 * 
	 * @param t string to check
	 * @return true if the given string is a jask calculation
	 */
	public static boolean isCalculation(String t) {
		return calculations.contains(t);
	}
	
	/**
	 * Checks if a given string is a jask operator
	 *
	 * @param t string to check
	 * @return true if the given string is a jask operator
	 */
	public static boolean isOperator(String t) {
		return operators.contains(t) || Keywords.isCalculation(t);
	}

	/**
	 * Checks if a given string is a jask keyword
	 *
	 * @param t string to check
	 * @return true if the given string is a jask keyword
	 */
	public static boolean isKeyword(String t) {
		return keywords.contains(t) || Keywords.isOperator(t);
	}
}
