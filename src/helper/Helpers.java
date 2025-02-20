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
	    if (param == null || param.isEmpty()) return List.of();

	    List<String> params = new ArrayList<>();
	    StringBuilder tempString = new StringBuilder();
	    boolean stringParsing = false;
	    boolean insideFunction = false;

	    for (int i = 0, len = param.length(); i < len; i++) {
	        char c = param.charAt(i);

	        switch (c) {
	            case '"':
	                stringParsing = !stringParsing;
	                break;
	            case '(':
	                if (!stringParsing) insideFunction = true;
	                break;
	            case ')':
	                if (!stringParsing) insideFunction = false;
	                break;
	            case ':':
	                if (!stringParsing && !insideFunction) {
	                    params.add(tempString.toString());
	                    tempString.setLength(0);
	                    continue;
	                }
	                break;
	        }

	        tempString.append(c);
	    }

	    if (tempString.length() > 0) params.add(tempString.toString());
	    return params;
	}

	
	/**
	 * Checks if a given string is a jask function
	 *
	 * @param input string to check
	 * @return true if the given string is a jask function
	 */
	public static boolean isFunction(String input) {
	    int len = input.length();
	    if (len < 3 || input.indexOf('(') == -1 || input.indexOf(')') == -1) return false;

	    char[] chars = input.toCharArray();
	    int openParen = 0;
	    boolean functionNameFound = false;

	    for (char c : chars) {
	        if (Character.isLetter(c)) {
	            functionNameFound = true;
	        }
	        else if (c == '(') {
	            if (!functionNameFound) return false;
	            openParen++;
	        }
	        else if (c == ')') {
	            if (openParen == 0) return false;
	            openParen--;
	        }
	        else if ((c == ',' || c == ':' || c == '"') && openParen == 0) {
	            return false;
	        }
	    }

	    return functionNameFound && openParen == 0;
	}

}