package jask;

import java.io.IOException;
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
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			InteractiveMode interactiveMode = new InteractiveMode(version);
			interactiveMode.execute();
			return;
		}

		String file = args[0];

		if (!Helpers.checkFile(file)) {
			Error.terminateInterpret("The file '" + file + "' can not be found!");
		}

		List<String> content = setUpEnv(args);
		content.addAll(Helpers.readFile(file));
		new Interpreter().interpret(new Tokenizer().parse(file));
	}
}