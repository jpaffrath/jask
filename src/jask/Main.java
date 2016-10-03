package jask;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main java file
 *
 * @author Julius Paffrath
 *
 */
public class Main {

	/**
	 * Creates jask code for the global argument list
	 */
	private static List<String> setUpEnv(String[] args) {
		List<String> env = new ArrayList<String>(args.length);
		env.add("store list(\"" + args[0] + "\") in _ENV");

		for (int i = 1; i < args.length; i++) {
			env.add("listAdd(_ENV:\"" + args[i] + "\")");
		}

		return env;
	}

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
					Error.terminateInterpret("The file '" + fileName + "' can not be found!");
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
	 * Performs the interactive mode
	 */
	private static void interactiveMode() {
		Tokenizer tokenizer = new Tokenizer();
		Interpreter interpreter = new Interpreter();

		Scanner scanner = new Scanner(System.in);
		List<String> tempList = new ArrayList<String>();
		String line = "";

		System.out.print("jask ~> ");

		while (true) {
			line = scanner.nextLine();

			// stops the interactive mode
			if (line.contentEquals("exit")) break;

			// if a function is added, add lines to list until the function ends
			if (line.length() > 8 && line.substring(0, 8).contentEquals("function")) {
				tempList.add(line);
				System.out.print("func ~> ");

				while (true) {
					line = scanner.nextLine();
					if (line.contentEquals("end")) break;

					tempList.add(line);
					System.out.print("func ~> ");
				}
			}

			// if a statement is added, add lines to list until the statement ends
			if (line.length() > 1 && line.substring(0, 2).contentEquals("if")) {
				int ifCount = 1;
				int exCount = 0;

				tempList.add(line);
				System.out.print("if ~> ");

				while (true) {
					line = scanner.nextLine();

					if (line.length() > 1 && line.substring(0, 2).contentEquals("if")) ifCount++;
					else if (line.contentEquals("endif")) exCount++;

					if (ifCount == exCount) break;

					tempList.add(line);
					System.out.print("if ~> ");
				}
			}

			// if a run statement is added, add lines to list until the statement ends
			if (line.length() > 2 && line.substring(0, 3).contentEquals("run")) {
				int ruCount = 1;
				int exCount = 0;

				tempList.add(line);
				System.out.print("run ~> ");

				while (true) {
					line = scanner.nextLine();

					if (line.length() > 2 && line.substring(0, 3).contentEquals("run")) ruCount++;
					else if (line.contentEquals("endrun")) exCount++;

					if (ruCount == exCount) break;

					tempList.add(line);
					System.out.print("run ~> ");
				}
			}

			tempList.add(line);
			System.out.print("jask ~> ");

			interpreter.interpret(tokenizer.parse(tempList));
			tempList.clear();
		}

		scanner.close();
	}

	/**
	 * Main program entry
	 *
	 * @param args cmd parameters
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			interactiveMode();
			return;
		}

		String file = args[0];

		if (!Helpers.checkFile(file)) {
			Error.terminateInterpret("The file '" + file + "' can not be found!");
		}

		List<String> content = setUpEnv(args);
		content.addAll(Helpers.readFile(file));
		content = preload(content);

		new Interpreter().interpret(new Tokenizer().parse(content));
	}
}