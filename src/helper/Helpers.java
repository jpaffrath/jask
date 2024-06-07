package helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Provides small helper functions
 *
 * @author Julius Paffrath
 *
 */
public final class Helpers {

	/**
	 * Private constructor to mark this class as an utility class
	 */
	private Helpers() { }
	
	/**
	 * Checks if a given path points to a file
	 *
	 * @param name path to file
	 * @return true if file exists, otherwise false
	 */
	public static boolean checkFilename(String name) {
		File f = new File(name);
		return f.exists() && !f.isDirectory();
	}

	/**
	 * Checks if a given file exists
	 *
	 * @param file file to check
	 * @return true if file exists, otherwise false
	 */
	public static boolean checkFile(File file) {
		return file.exists() && !file.isDirectory();
	}

	/**
	 * Splits a given parameter string into a list of parameters
	 *
	 * @param param parameter string
	 * @return list of strings containing parameters
	 */
	public static List<String> splitParams(String param) {
		List<String> params = new ArrayList<>();
		String tempString = "";
		boolean stringParsing = false;
		boolean insideFunction = false;

		for (char c : param.toCharArray()) {
			if (c == '"' && !stringParsing) {
				stringParsing = true;
			}
			else if (c == '"' && stringParsing) {
				stringParsing = false;
			}

			if (c == '(' && !stringParsing && !insideFunction) {
				insideFunction = true;
			}
			else if (c == ')' && !stringParsing && insideFunction) {
				insideFunction = false;
			}

			if (c == ':' && !stringParsing && !insideFunction) {
				params.add(tempString);
				tempString = "";
				continue;
			}

			tempString += c;
		}

		// adds the temp string to the parameter list
		// if only one parameter is present
		if (!tempString.contentEquals("")) {
			params.add(tempString);
		}

		return params;
	}
	
	/**
	 * Checks if a given string is a jask function
	 *
	 * @param input string to check
	 * @return true if the given string is a jask function
	 */
	public static boolean isFunction(String input) {
		if (input.contains("(") == false) return false;
		if (input.contains(")") == false) return false;

		Stack<Character> stack = new Stack<>();
        boolean functionNameFound = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (Character.isLetter(currentChar)) {
                functionNameFound = true;
            } else if (currentChar == '(') {
                if (!functionNameFound) {
                    return false;
                }
                stack.push(currentChar);
            } else if (currentChar == ')') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return false;
                }
                stack.pop();
            } else if (currentChar == ',' || currentChar == ':' || currentChar == '"') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return false;
                }
            }
        }

        return functionNameFound && stack.isEmpty();
    }
}