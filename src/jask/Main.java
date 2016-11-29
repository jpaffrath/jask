package jask;

import java.util.ArrayList;
import java.util.List;

/**
 * Main java file
 *
 * @author Julius Paffrath
 *
 */
public class Main {
	private static final String version = "0.0.1";

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
	 * Main program entry
	 *
	 * @param args cmd parameters
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			InteractiveMode interactiveMode = new InteractiveMode(version);
			interactiveMode.execute();
			return;
		}

		String file = "";

		boolean flagHelp = false;
		boolean flagVersion = false;

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.contentEquals("-h") || arg.contentEquals("--help")) {
				flagHelp = true;
			}
			if (arg.contentEquals("-v") || arg.contentEquals("--version")) {
				flagVersion = true;
			}
			if (Helpers.checkFile(arg)) {
				file = arg;
			}
		}

		if (flagHelp) {
			System.out.println("jask interpreter version " + version);
			System.out.println("Need help? Visit the jask project on GitHub: github.com/jpaffrath/jask");
			return;
		}

		if (flagVersion) {
			System.out.println("jask interpreter version " + version);
			return;
		}

		if (file == "") {
			Error.terminateInterpret("Input file can not be found!");
		}

		List<String> content = setUpEnv(args);
		content.addAll(Helpers.readFile(file));
		new Interpreter().interpret(new Tokenizer().parse(content));
	}
}