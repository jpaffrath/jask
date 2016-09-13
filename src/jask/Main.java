package jask;

import java.util.List;

public class Main {

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

		for (int i = 0; i < args.length; i++) {
			Tokenizer tokenizer = new Tokenizer();
			Interpreter interpreter = new Interpreter();

			List<String> content = Helpers.readFile(args[i]);
			content = preload(content);

			interpreter.interpret(tokenizer.parse(content));
		}
	}
}