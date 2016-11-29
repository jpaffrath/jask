package jask;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InteractiveMode {
	private static final int historyMax = 99;

	private String version;
	private String history[];
	private int historyIndex;

	public InteractiveMode(String version) {
		this.version = version;
		this.history = new String[historyMax];
		this.historyIndex = 0;
	}

	/**
	 * Adds a line to the history
	 *
	 * @param line line of jask code
	 */
	private void addToHistory(String line) {
		if (line.isEmpty()) return;

		// if the history is full, clear it
		if (historyIndex + 1 > historyMax) {
			history = new String[historyMax];
			historyIndex = 0;
		}

		history[historyIndex++] = line;
	}

	/**
	 * Prints the history to the standard out
	 */
	private void printHistory() {
		for (int i = 0; i < historyIndex; i++) {
			System.out.println("[" + (i + 1) +"]" + " " + history[i]);
		}
	}

	/**
	 * Executes the interactive mode
	 */
	public void execute() {
		Tokenizer tokenizer = new Tokenizer();
		Interpreter interpreter = new Interpreter();

		Scanner scanner = new Scanner(System.in);
		List<String> tempList = new ArrayList<String>();
		String line = "";

		System.out.println("jask interpreter version " + this.version);
		System.out.print("jask ~> ");

		while (true) {
			line = scanner.nextLine();

			// stops the interactive mode
			if (line.contentEquals("exit")) break;

			// prints the history
			if (line.contentEquals("history")) {
				this.printHistory();
				System.out.print("jask ~> ");
				continue;
			}

			// if a function is added, add lines to list until the function ends
			if (line.length() > 8 && line.substring(0, 8).contentEquals("function")) {
				tempList.add(line);
				System.out.print("func ~>     ");

				while (true) {
					line = scanner.nextLine();
					if (line.contentEquals("end")) break;

					tempList.add(line);
					System.out.print("func ~>     ");
				}
			}

			// if a statement is added, add lines to list until the statement ends
			if (line.length() > 1 && line.substring(0, 2).contentEquals("if")) {
				int ifCount = 1;
				int exCount = 0;

				tempList.add(line);
				System.out.print("if   ~>    ");

				while (true) {
					line = scanner.nextLine();

					if (line.length() > 1 && line.substring(0, 2).contentEquals("if"))
						ifCount++;
					else if (line.contentEquals("endif")) exCount++;

					if (ifCount == exCount) break;

					tempList.add(line);
					System.out.print("if   ~>    ");
				}
			}

			// if a run statement is added, add lines to list until the statement ends
			if ((line.length() > 2 && line.substring(0, 3).contentEquals("run")) ||
					(line.length() > 4 && line.substring(0, 5).contentEquals("while"))) {
				int ruCount = 1;
				int exCount = 0;

				tempList.add(line);
				System.out.print("run  ~>     ");

				while (true) {
					line = scanner.nextLine();

					if ((line.length() > 2 && line.substring(0, 3).contentEquals("run")) ||
							(line.length() > 4 && line.substring(0, 5).contentEquals("while")))
						ruCount++;
					else if (line.contentEquals("endrun")) exCount++;

					if (ruCount == exCount) break;

					tempList.add(line);
					System.out.print("run  ~>     ");
				}
			}

			tempList.add(line);
			this.addToHistory(line);
			System.out.print("jask ~> ");

			interpreter.interpret(tokenizer.parse(tempList));
			tempList.clear();
		}

		scanner.close();
	}
}
