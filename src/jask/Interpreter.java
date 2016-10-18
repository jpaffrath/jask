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
	private List<String> operators;
	private List<String> keywords;
	private List<String> values;
	private Executer executer;

	public Interpreter() {
		operators = new ArrayList<String>();
		operators.add("plus");
		operators.add("minus");
		operators.add("times");
		operators.add("divide");
		operators.add("store");
		operators.add("assign");
		operators.add("mod");
		operators.add("equals");
		operators.add("unequals");
		operators.add("greater");
		operators.add("smaller");
		operators.add("greaterequal");
		operators.add("smallerequal");

		keywords = new ArrayList<String>(operators);
		keywords.add("function");
		keywords.add("end");
		keywords.add("if");
		keywords.add("else");
		keywords.add("return");
		keywords.add("convert");
		keywords.add("to");
		keywords.add("number");
		keywords.add("string");
		keywords.add("in");
		keywords.add("run");
		keywords.add("with");

		values = new ArrayList<String>();
		values.add("TRUE");
		values.add("FALSE");
		values.add("NULL");

		executer = new Executer();
	}

	public Interpreter(Function function) {
		this();
		executer = new Executer(function.getHeap());
	}

	public Interpreter(Executer executer) {
		this();
		this.executer = executer;
	}

	private boolean isOperator(String t) {
		return operators.contains(t);
	}

	private boolean isKeyword(String t) {
		return keywords.contains(t);
	}

	private boolean isValue(String t) {
		return values.contains(t);
	}

	public static boolean isFunction(String t) {
		if (t.lastIndexOf(')') == t.length() -1) return true;
		return false;
	}

	public String interpret(List<String> tokens) {
		Expression exp = null;
		String t = null;
		boolean ifRunning = false;

		for (int i = 0; i < tokens.size(); i++) {
			t = tokens.get(i);

			// checks return statement
			if (t.contentEquals("return")) {
				String str = tokens.get(i+1);
				Variable ret = executer.getVariableFromHeap(str);
				if (ret == null) {
					return str;
				}
				else {
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
				List<String> functionToken = new ArrayList<String>();
				String runner = tokens.get(++i);
				for (; !runner.contentEquals("end"); i++) {
					functionToken.add(runner);
					runner = tokens.get(i+1);
				}
				executer.functionExecuter.addFunction(new Function(name, functionToken));
				continue;
			}

			// checks if statement
			else if (t.contentEquals("if")) {
				int ifType = 3;
				if (tokens.get(i+2).contentEquals("mod")) {
					ifType += 2;
				}
				if (executer.executeStatement(new Expression(ExpressionType.Statement, tokens.subList(i, i+ifType+1)))) {
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
			else if (t.contentEquals("run")) {
				int runCount = 1;
				int endCount = 0;
				int runner = i;

				while (true) {
					t = tokens.get(++runner);

					if (t.contentEquals("run")) runCount++;
					else if (t.contentEquals("endrun")) endCount++;

					if (runCount == endCount) break;
				}

				exp = new Expression(ExpressionType.Runner, tokens.subList(i+1, ++runner));
			}

			// check convert statement
			else if (t.contentEquals("convert")) {
				exp = new Expression(ExpressionType.Convert, tokens.subList(i, i+4));
				i += 3;
			}

			executer.execute(exp);
			exp = null;
		}

		return "";
	}
}