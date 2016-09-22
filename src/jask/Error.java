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

	public static void printErrorListMultipleTypes() {
		System.out.println(prefix + "multiple types given for list!");
	}

	public static void printErrorVariableIsNotAList(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a list!");
	}

	public static void printErrorConvertNotApplicable(String operator, String var) {
		System.out.println(prefix + "operator '" + operator + "' not applicable for variable '" + var + "'");
	}

	public static void printErrorNoCMDOption(String option) {
		System.out.println(prefix + "cmd parameter '" + option + "' is no jask interpreter option!");
	}

	public static void printErrorIsKeyword(String var) {
		System.out.println(prefix + "given name '" + var + "' is a keyword!");
	}

	public static void terminateInterpret(String reason) {
		System.out.println(prefix + reason);
		System.out.println("Due to this reason, the interpreter has been terminated!");
		System.exit(-1);
	}
}