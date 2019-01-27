package helper;

/**
 * Error reporting class
 *
 * @author Julius Paffrath
 *
 */
public final class Error {
	
	/**
	 * Private constructor to mark this class as an utility class
	 */
	private Error() { }
	
	private static final String prefix = "Error: ";

	public static void printErrorVariableNotDefined(String var) {
		System.out.println(prefix + "variable '" + var + "' not defined in this context!");
		System.exit(-1);
	}

	public static void printErrorVariableAlreadyDefined(String var) {
		System.out.println(prefix + "variable '" + var + "' already defined in this context!");
		System.exit(-1);
	}

	public static void printErrorOperatorNotApplicable(String operator, String var1, String var2) {
		System.out.println(prefix + "operator '" + operator + "' not applicable for values '" + var1 + "', '" + var2 + "'");
		System.exit(-1);
	}

	public static void printErrorNoValueAtIndex(int index) {
		System.out.println(prefix + "no value at index " + index);
		System.exit(-1);
	}

	public static void printErrorVariableIsNotAList(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a list!");
		System.exit(-1);
	}
	
	public static void printErrorVariableIsNotADictionary(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a dictionary!");
		System.exit(-1);
	}

	public static void printErrorVariableIsNotANumber(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a number!");
		System.exit(-1);
	}

	public static void printErrorVariableIsNotAString(String var) {
		System.out.println(prefix + "variable '" + var + "' is not a string!");
		System.exit(-1);
	}

	public static void printErrorValueNotApplicable(String value) {
		System.out.println(prefix + "value '" + value + "' is not applicable!");
		System.exit(-1);
	}

	public static void printErrorConvertNotApplicable(String operator, String var) {
		System.out.println(prefix + "operator '" + operator + "' not applicable for variable '" + var + "'");
		System.exit(-1);
	}

	public static void printErrorFunctionNotDefined(String function) {
		System.out.println(prefix + "the function '" + function + "' is not defined!");
		System.exit(-1);
	}

	public static void printErrorNoCMDOption(String option) {
		System.out.println(prefix + "cmd parameter '" + option + "' is no jask interpreter option!");
		System.exit(-1);
	}

	public static void printErrorIsKeyword(String var) {
		System.out.println(prefix + "given name '" + var + "' is a keyword!");
		System.exit(-1);
	}

	public static void printErrorFileNotFound(String file) {
		System.out.println(prefix + "the file '" + file + "' was not found!");
		System.exit(-1);
	}
	
	public static void printErrorFileReadError(String file) {
		System.out.println(prefix + "reading the file '" + file + "' failed unexpectetly!");
		System.exit(-1);
	}
	
	public static void printErrorFileWriteError(String file) {
		System.out.println(prefix + "writing the file '" + file + "' failed unexpectetly!");
		System.exit(-1);
	}
	
	public static void printErrorNoProperParametersForDictionary() {
		System.out.println(prefix + "given parameters not suitable for initializing a dictionary!");
		System.exit(-1);
	}
	
	public static void printErrorNoValueGiven() {
		System.out.println(prefix + "no value given!");
		System.exit(-1);
	}
	
	public static void printErrorProcessInterrupted(String processCall) {
		System.out.println(prefix + "the process call '" + processCall + "' has been interrupted!");
		System.exit(-1);
	}
	
	public static void printErrorProcessIOException(String processCall) {
		System.out.println(prefix + "IO Exception on process call '" + processCall + "'!");
		System.exit(-1);
	}

	public static void terminateInterpret(String reason) {
		System.out.println(prefix + reason);
		System.out.println("Due to this reason, the interpreter has been terminated!");
		System.exit(-1);
	}
}