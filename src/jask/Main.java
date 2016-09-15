package jask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main java file
 *
 * @author Julius Paffrath
 *
 */
public class Main {
	private static String libraryPath = "";

	/**
	 * Searches for "use" statements and imports the files
	 *
	 * @param content content from main file
	 * @return tokens from main file with imported tokens
	 */
	private static List<String> preload(List<String> content) {
		for (int i = 0; i < content.size(); i++) {
			String c = content.get(i);
			if (c.length() < 3) continue;

			String part = c.substring(0, 3);
			if (part.contentEquals("use")) {
				String fileName = c.substring(4, c.length()) + ".jask";

				if (!Helpers.checkFile(fileName)) {
					if (Helpers.checkFile(libraryPath + "/" + fileName)) {
						fileName = libraryPath + "/" + fileName;
					}
				}

				List<String> importContent = Helpers.readFile(fileName);
				// removes the "use file.jask" statement
				content.remove(i);
				// imports file content
				importContent = preload(importContent);
				importContent.addAll(content);
				content = importContent;
			}
		}

		return content;
	}

	/**
	 * Main program entry
	 *
	 * @param args cmd parameters
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("This is the jask interpreter. No input files!");
			return;
		}

		// convert cmd parameters to list
		List<String> params = new ArrayList<String>(Arrays.asList(args));
		// list for jask files given as cmd parameters
		List<String> files = new ArrayList<String>();

		// parse cmd parameters
		for (int i = 0; i < params.size(); i++) {
			String curParam = params.get(i);

			// extract library path
			if (curParam.contentEquals("-l")) {
				libraryPath = params.get(i+1);
				i++;
			}
			// add file to file list
			else if (Helpers.checkFile(curParam)) {
				files.add(curParam);
			}
			else {
				Error.printErrorNoCMDOption(curParam);
			}
		}

		for (String file : files) {
			Tokenizer tokenizer = new Tokenizer();
			Interpreter interpreter = new Interpreter();

			// reads the file
			List<String> content = Helpers.readFile(file);
			// loads imported files
			content = preload(content);

			interpreter.interpret(tokenizer.parse(content));
		}
	}
}