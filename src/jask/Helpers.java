package jask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides small helper functions
 *
 * @author Julius Paffrath
 *
 */
public class Helpers {

	/**
	 * Reads the content of a given file
	 *
	 * @param name name of the file
	 * @return list of strings containing the files content
	 */
	public static List<String> readFile(String name) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lines;
	}

	/**
	 * Checks if a given path points to a file
	 *
	 * @param name path to file
	 * @return true if file exists, otherwise false
	 */
	public static boolean checkFile(String name) {
		File f = new File(name);
		return (f.exists() && !f.isDirectory());
	}

	/**
	 * Splits a given parameter string into a list of parameters
	 *
	 * @param param parameter string
	 * @return list of strings containing parameters
	 */
	public static List<String> splitParams(String param) {
		List<String> params = new ArrayList<String>();
		String tempString = "";
		boolean stringParsing = false;

		for (char c : param.toCharArray()) {
			if (c == '"' && !stringParsing) stringParsing = true;
			else if (c == '"' && stringParsing) stringParsing = false;

			if (c == ':' && !stringParsing) {
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
}