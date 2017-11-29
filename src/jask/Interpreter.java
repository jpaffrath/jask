package jask;

import static jask.Constants.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Interpreter class
 *
 * @author Julius Paffrath
 *
 */
public class Interpreter {
	private static final int historyMax = 10;
	private History history;

	private List<String> operators;
	private List<String> keywords;
	private List<String> values;
	private Executer executer;

	/**
	 * General constructor
	 */
	public Interpreter() {
		this.history = new History(historyMax);

		this.operators = new ArrayList<String>();
		this.operators.add("plus");
		this.operators.add("minus");
		this.operators.add("times");
		this.operators.add("divide");
		this.operators.add("store");
		this.operators.add("assign");
		this.operators.add("mod");
		this.operators.add("equals");
		this.operators.add("unequals");
		this.operators.add("greater");
		this.operators.add("smaller");
		this.operators.add("greaterequals");
		this.operators.add("smallerequals");
		this.operators.add("increment");
		this.operators.add("decrement");
		this.operators.add("call");

		this.keywords = new ArrayList<String>(this.operators);
		this.keywords.add("function");
		this.keywords.add("end");
		this.keywords.add("if");
		this.keywords.add("else");
		this.keywords.add("return");
		this.keywords.add("convert");
		this.keywords.add("to");
		this.keywords.add("number");
		this.keywords.add("string");
		this.keywords.add("in");
		this.keywords.add("run");
		this.keywords.add("with");
		this.keywords.add("while");
		this.keywords.add("times");

		this.values = new ArrayList<String>();
		this.values.add(TRUE);
		this.values.add(FALSE);
		this.values.add(NULL);

		this.executer = new Executer();
	}

	/**
	 * Initializes a new interpreter for function executing
	 *
	 * @param function function to be executed
	 * @param functionExecuter the function executed to be used
	 * @param modules list of modules
	 */
	public Interpreter(Function function, FunctionExecuter functionExecuter, List<Executer> modules) {
		this();
		this.executer = new Executer(function.getHeap(), functionExecuter, modules);
	}

	/**
	 * Initializes a new interpreter for interpreting run statements
	 *
	 * @param executer executer to be used
	 */
	public Interpreter(Executer executer) {
		this();
		this.executer = executer;
	}

	/**
	 * Checks if a given string is a jask operator
	 *
	 * @param t string to check
	 * @return true if the given string is a jask operator
	 */
	private boolean isOperator(String t) {
		return this.operators.contains(t);
	}

	/**
	 * Checks if a given string is a jask keyword
	 *
	 * @param t string to check
	 * @return true if the given string is a jask keyword
	 */
	private boolean isKeyword(String t) {
		return this.keywords.contains(t);
	}

	/**
	 * Checks if a given string is a jask value
	 *
	 * @param t string to check
	 * @return true if the given string is a jask value
	 */
	private boolean isValue(String t) {
		return this.values.contains(t);
	}

	/**
	 * Checks if a given string is a jask function
	 *
	 * @param t string to check
	 * @return true if the given string is a jask function
	 */
	public static boolean isFunction(String t) {
		if (t.lastIndexOf(')') == t.length() -1) return true;
		return false;
	}

	/**
	 * Returns local executer
	 *
	 * @return executer
	 */
	public Executer getExecuter() {
		return this.executer;
	}

	/**
	 * Interprets a given list of tokens
	 *
	 * @param tokens list of tokens
	 * @return result of interpreting
	 */
	public String interpret(List<String> tokens) {
		Expression exp = null;
		String t = null;
		String retVal = "";
		boolean ifRunning = false;

		for (int i = 0; i < tokens.size(); i++) {
			t = tokens.get(i);

			// checks return statement
			if (t.contentEquals("return")) {
				String str = tokens.get(i+1);
				Variable ret = null;

				if (isFunction(str)) {
					ret = this.executer.executeFunction(str);
				}
				else {
					ret = this.executer.getVariableFromHeap(str);
				}

				if (ret == null) {
					return str;
				}
				else {
					if (ret.getType() == VariableType.String) {
						return "\"" + ret.toString() + "\"";
					}
					return ret.toString();
				}
			}

			// checks store expressions
			if (t.contentEquals("store") && tokens.get(i+2).contentEquals("in")) {
				// check if the variable name is a keyword
				if (isKeyword(tokens.get(i+1))) {
					Error.terminateInterpret(tokens.get(i+1) + " is a keyword!");
				}

				if (isKeyword(tokens.get(i+3)) || isValue(tokens.get(i+3))) {
					Error.terminateInterpret(tokens.get(i+3) + " is a keyword!");
				}

				exp = new Expression(ExpressionType.Store, tokens.subList(i, i+4));
				i += 3;
			}
			// checks assign expressions
			else if (t.contentEquals("assign")) {
				if (isOperator(tokens.get(i+2)) && tokens.get(i+4).contentEquals("to")) {
					if (isKeyword(tokens.get(i+1))) {
						Error.terminateInterpret(tokens.get(i+1) + " is a keyword!");
					}

					if (isKeyword(tokens.get(i+3))) {
						Error.terminateInterpret(tokens.get(i+3) + " is a keyword!");
					}

					if (isKeyword(tokens.get(i+5)) || isValue(tokens.get(i+5))) {
						Error.terminateInterpret(tokens.get(i+5) + " is a keyword!");
					}

					exp = new Expression(ExpressionType.Assign, tokens.subList(i, i+6));
					i += 5;
				}
				else {
					if (isKeyword(tokens.get(i+1))) {
						Error.terminateInterpret(tokens.get(i+1) + " is a keyword!");
					}

					if (isKeyword(tokens.get(i+3)) || isValue(tokens.get(i+3))) {
						Error.terminateInterpret(tokens.get(i+3) + " is a keyword!");
					}

					exp = new Expression(ExpressionType.Assign, tokens.subList(i, i+4));
					i += 3;
				}
			}
			// checks function expressions
			else if (isFunction(t)) {
				exp = new Expression(ExpressionType.Function, tokens.subList(i, i+1));
			}
			// checks function define
			else if (t.contentEquals("function")) {
				String name = tokens.get(++i);
				int start = i+1;
				int funcCount = 1;
				int endCount = 0;

				while (true) {
					t = tokens.get(i++);

					if (t.contentEquals("function")) funcCount++;
					else if (t.contentEquals("end")) endCount++;

					if (funcCount == endCount) break;
				}

				this.executer.addFunction(new Function(name, tokens.subList(start, --i)));
				continue;
			}

			// checks if statement
			else if (t.contentEquals("if")) {
				int ifType = 3;
				if (tokens.get(i+2).contentEquals("mod")) {
					ifType += 2;
				}
				if (this.executer.executeStatement(new Expression(ExpressionType.Statement, tokens.subList(i, i+ifType+1)))) {
					i += ifType;
					ifRunning = true;

				}
				else {
					ifRunning = true;

					int ifCount = 1;
					int elCount = 0;

					while (true) {
						t = tokens.get(++i);

						if (t.contentEquals("if")) ifCount++;
						else if (t.contentEquals("else")) elCount++;

						if (ifCount == elCount) break;
					}
				}

				continue;
			}

			else if (t.contentEquals("else") && ifRunning) {
				while (!(t = tokens.get(++i)).equals("endif")) { }
			}

			// check run statement
			else if (t.contentEquals("run") || t.contentEquals("while")) {
				int runCount = 1;
				int endCount = 0;
				int runner = i;

				while (true) {
					t = tokens.get(++runner);

					if (t.contentEquals("run") || t.contentEquals("while")) runCount++;
					else if (t.contentEquals("endrun")) endCount++;

					if (runCount == endCount) break;
				}

				exp = new Expression(ExpressionType.Runner, tokens.subList(i, runner+1));
				i = runner;
			}

			// check convert statement
			else if (t.contentEquals("convert")) {
				exp = new Expression(ExpressionType.Convert, tokens.subList(i, i+4));
				i += 3;
			}

			// check increment statement
			else if (t.contentEquals("increment")) {
				exp = new Expression(ExpressionType.Increment, tokens.subList(++i, i+1));
			}

			// check decrement statement
			else if (t.contentEquals("decrement")) {
				exp = new Expression(ExpressionType.Decrement, tokens.subList(++i, i+1));
			}

			// check use statement
			else if (t.startsWith("use")) {
				String moduleName = tokens.get(++i);
				String module = moduleName + ".jask";

				if (!Helpers.checkFilename(module)) {
					Error.terminateInterpret("Can't find module named '" + module + "'");
				}

				Interpreter moduleInterpreter = new Interpreter();
				moduleInterpreter.interpret(new Tokenizer().parse(new File(module)));
				moduleInterpreter.executer.setName(moduleName);
				this.executer.addModule(moduleInterpreter.executer);
			}

			// check module remove statement
			else if (t.startsWith("remove")) {
				String moduleName = tokens.get(++i);
				this.executer.removeModule(moduleName);
			}
			
			// check call statement
			else if (t.contentEquals("call")) {
				exp = new Expression(ExpressionType.Call, tokens.subList(++i, i+2));
				i += 2;
			}

			// try-catch is a little bit bumpy
			try {
				this.history.addToHistory(exp.toString());
				retVal = this.executer.execute(exp);
			}
			catch (Exception e) {
				// disable history for debugging
				//this.history.printHistoryDESC();
			}

			exp = null;
			if (retVal != "") return retVal;
		}

		return "";
	}
}