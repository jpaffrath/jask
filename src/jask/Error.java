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
}
