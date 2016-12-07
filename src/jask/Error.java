package jask;

/**
 * Error reporting class
 *
 * @author Julius Paffrath
 *
 */
public class Error {
	private static final String prefix = "Error: ";

	public static void printErrorVariableNotDefined(String var) {
		System.out.println(prefix + "variable '" + var + "' not defined in this context!");
	}

	public static void printErrorVariableAlreadyDefined(String var) {
		System.out.println(prefix + "variable '" + var + "' already defined in this context!");
	}

	public static void printErrorOperatorNotApplicable(String operator, String var1, String var2) {
		System.out.println(prefix + "operator '" + operator + "' not applicable for values '" + var1 + "', '" + var2 + "'");
	}

	public static void printErrorNoValueAtIndex(int index) {
		System.out.println(prefix + "no value at index " + index);
	}

	public static void printErrorVariableIsNotAList(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a list!");
	}

	public static void printErrorVariableIsNotANumber(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a number!");
	}

	public static void printErrorVariableIsNotAString(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a string!");
	}

	public static void printErrorValueNotApplicable(String value) {
		System.out.println(prefix + "value '" + value + "' is not applicable!");
	}

	public static void printErrorConvertNotApplicable(String operator, String var) {
		System.out.println(prefix + "operator '" + operator + "' not applicable for variable '" + var + "'");
	}

	public static void printErrorFunctionNotDefined(String function) {
		System.out.println(prefix + "the function '" + function + "' is not defined!");
	}

	public static void printErrorNoCMDOption(String option) {
		System.out.println(prefix + "cmd parameter '" + option + "' is no jask interpreter option!");
	}

	public static void printErrorIsKeyword(String var) {
		System.out.println(prefix + "given name '" + var + "' is a keyword!");
	}

	public static void printErrorFileNotFound(String file) {
		System.out.println(prefix + "the file '" + file + ".jask' was not found!");
	}

	public static void printErrorModuleAlreadyLoaded(String module) {
		System.out.println(prefix + "the module '" + module + "' is already loaded in this context!");
	}

	public static void terminateInterpret(String reason) {
		System.out.println(prefix + reason);
		System.out.println("Due to this reason, the interpreter has been terminated!");
		System.exit(-1);
	}
}