package jask;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import helper.History;

/**
 * jask interactive mode
 *
 * @author Julius Paffrath
 *
 */
public class InteractiveMode {
	private static final int historyMax = 99;

	private String version;
	private History history;

	/**
	 * General constructor
	 *
	 * @param version jask version
	 */
	public InteractiveMode(String version) {
		this.version = version;
		this.history = new History(historyMax);
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
				this.history.printHistory();
				System.out.print("jask ~> ");
				continue;
			}

			// prints the loaded modules
			if (line.contentEquals("modules")) {
				List<String> names = interpreter.getExecuter().getModuleNames();

				if (names.isEmpty()) {
					System.out.println("No loaded modules in context");
					System.out.print("jask ~> ");
					continue;
				}

				System.out.print("Loaded modules: [");
				for (int i = 0; i < names.size(); i++) {
					if (i == names.size()-1) {
						System.out.print(names.get(i));
					}
					else {
						System.out.print(names.get(i) + ", ");
					}
				}
				System.out.println("]");

				System.out.print("jask ~> ");
				continue;
			}

			// wraps the internal implementation of read()
			if (line.contains("read()")) {
				int lastIndex = 0;
				int count = 0;

				// count occurrences of read() functions
				while (lastIndex != -1) {
				    lastIndex = line.indexOf("read()", lastIndex);

				    if (lastIndex != -1) {
				        count ++;
				        lastIndex += "read()".length();
				    }
				}

				// replace read() in line with input
				for (int i = 0; i < count; i++) {
					System.out.print("read ~> ");
					line = line.substring(0, line.indexOf("read()")) + '"' + scanner.nextLine() + '"' + line.substring(line.indexOf("read()") + 6, line.length());
				}

				interpreter.interpret(tokenizer.parse(line));
				System.out.print("jask ~> ");
				continue;
			}

			// if a function is added, add lines to list until the function ends
			if (line.length() > 8 && line.substring(0, 8).contentEquals("function")) {
				if (line.contains("end")) {
					this.history.addToHistory(line);
					interpreter.interpret(tokenizer.parse(line));
					System.out.print("jask ~> ");
					continue;
				}

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
				if (line.contains("endif")) {
					this.history.addToHistory(line);
					interpreter.interpret(tokenizer.parse(line));
					System.out.print("jask ~> ");
					continue;
				}

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
				if (line.contains("endrun")) {
					this.history.addToHistory(line);
					interpreter.interpret(tokenizer.parse(line));
					System.out.print("jask ~> ");
					continue;
				}

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
			
			// if the current line is a variable, print its content
			if (interpreter.getExecuter().hasVariable(line)) {
				System.out.println("jask ~> " + interpreter.getExecuter().getVariableFromHeap(line).toString());
			}

			tempList.add(line);
			this.history.addToHistory(line);
			System.out.print("jask ~> ");

			interpreter.interpret(tokenizer.parse(tempList));
			tempList.clear();
		}

		scanner.close();
	}
}
