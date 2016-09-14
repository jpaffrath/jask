package jask;

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
}
