package jask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	private static String libraryPath = "";

	/**
	 * Searchs for "use" statements and imports the files
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
				String fileName = c.substring(4, c.length());

				if (!Helpers.checkFile(fileName)) {
					if (Helpers.checkFile(libraryPath + "/" + fileName)) {
						fileName = libraryPath + "/" + fileName;
					}
				}

				List<String> importContent = Helpers.readFile(fileName);
				// removes the "use class.jask" statement
				content.remove(i);
				importContent = preload(importContent);
				importContent.addAll(content);
				content = importContent;
			}
		}

		return content;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("This is the jask interpreter. No input files!");
			return;
		}

		// convert cmd arguments to list
		List<String> params = new ArrayList<String>(Arrays.asList(args));
		List<String> files = new ArrayList<String>();

		// parse cmd arguments
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).contentEquals("-l")) {
				libraryPath = params.get(i+1);
				i++;
			}
			else {
				files.add(params.get(i));
			}
		}

		for (String file : files) {
			Tokenizer tokenizer = new Tokenizer();
			Interpreter interpreter = new Interpreter();

			List<String> content = Helpers.readFile(file);
			content = preload(content);

			System.out.println(content);

			interpreter.interpret(tokenizer.parse(content));
		}
	}
}