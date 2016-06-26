package jask;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {
	private List<String> operators;
	private Executer executer;

	public Interpreter() {
		operators = new ArrayList<String>();
		operators.add("plus");
		operators.add("minus");
		operators.add("times");
		operators.add("divide");

		executer = new Executer();
	}

	public Interpreter(Function function) {
		this();
		executer = new Executer(function.getHeap());
	}

	private boolean isOperator(String t) {
		return operators.contains(t);
	}

	private boolean isFunction(String t) {
		char str[] = t.toCharArray();
		int isFunction = 0;

		for (int i = 0; i < str.length; i++) {
			if (str[i] == '(') isFunction++;
			else if (str[i] == ')') isFunction++;
		}

		if (isFunction == 2) return true;

		return false;
	}

	public void interpret(List<String> tokens) {
		Expression exp = null;
		String t = null;
		boolean ifRunning = false;

		for (int i = 0; i < tokens.size(); i++) {
			t = tokens.get(i);

			// checks store expressions
			if (t.contentEquals("store") && tokens.get(i+2).contentEquals("in")) {
				exp = new Expression(ExpressionType.Store, tokens.subList(i, i+4));
				i += 3;
			}
			// checks assign expressions
			else if (t.contentEquals("assign")) {
				if (isOperator(tokens.get(i+2)) && tokens.get(i+4).contentEquals("to")) {
					exp = new Expression(ExpressionType.Assign, tokens.subList(i, i+6));
					i += 5;
				}
				else {
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
				if (executer.executeStatement(new Expression(ExpressionType.Statement, tokens.subList(i, i+4)))) {
					i += 3;
					ifRunning = true;

				}
				else {
					ifRunning = false;
					while (!(t = tokens.get(++i)).equals("else")) { }
				}

				continue;
			}

			else if (t.contentEquals("else") && ifRunning) {
				while (!(t = tokens.get(++i)).equals("endif")) { }
			}

			executer.execute(exp);
			exp = null;
		}
	}
}
