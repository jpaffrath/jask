package jask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import helper.Error;
import helper.Helpers;

/**
 * Main java file
 *
 * @author Julius Paffrath
 *
 */
public class Main {
	private static final String VERSION = "0.0.1";

	/**
	 * Creates jask code for the global argument list
	 */
	private static List<String> setUpEnv(String[] args) {
		List<String> env = new ArrayList<>(args.length);
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
	public static void main(String[] args) {
		// if no arguments are provided, enter the jask interactive mode
		if (args.length == 0) {
			new InteractiveMode(VERSION).execute();
			return;
		}
		
		// the first argument should hold a jask file
		String file = args[0];

		if (!Helpers.checkFilename(file)) {
			Error.terminateInterpret("The file '" + file + "' can not be found!");
		}

		Tokenizer tokenizer = new Tokenizer();

		// add provided arguments to new jask context
		List<String> tokens = tokenizer.parse(setUpEnv(args));

		// parse input file
		tokens.addAll(tokenizer.parse(new File(file)));

		// interpret tokens and measure execution time
		long startTime = System.currentTimeMillis();
		String code = new Interpreter().interpret(tokens);
		long endTime = System.currentTimeMillis();
		
		// print status to output
		if (code.equals("")) {
			System.out.println("Program exited in " + ((endTime - startTime) / 1000.0) + " seconds");
		}
		else {
			System.out.println("Program exited with status code " + code + " in " + ((endTime - startTime) / 1000.0) + " seconds");
		}
	}
}