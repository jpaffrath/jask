package jask;

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
		this.operators.add("greaterequal");
		this.operators.add("smallerequal");

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

		this.values = new ArrayList<String>();
		this.values.add("TRUE");
		this.values.add("FALSE");
		this.values.add("NULL");

		this.executer = new Executer();
	}

	public Interpreter(Function function, FunctionExecuter functionExecuter) {
		this();
		this.executer = new Executer(function.getHeap(), functionExecuter);
	}

	public Interpreter(Executer executer) {
		this();
		this.executer = executer;
	}

	private boolean isOperator(String t) {
		return this.operators.contains(t);
	}

	private boolean isKeyword(String t) {
		return this.keywords.contains(t);
	}

	private boolean isValue(String t) {
		return this.values.contains(t);
	}

	public static boolean isFunction(String t) {
		if (t.lastIndexOf(')') == t.length() -1) return true;
		return false;
	}

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

			// check use statement
			else if (t.startsWith("use")) {
				String module = tokens.get(++i) + ".jask";

				if (!Helpers.checkFile(module)) {
					Error.terminateInterpret("Can't find module named '" + module + "'");
				}

				List<String> moduleContent = Helpers.readFile(module);
				Interpreter moduleInterpreter = new Interpreter();

				moduleInterpreter.interpret(new Tokenizer().parse(moduleContent));
				this.executer.addModule(moduleInterpreter.executer);
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