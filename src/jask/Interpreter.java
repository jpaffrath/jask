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

	private boolean isOperator(Token t) {
		return operators.contains(t.getValue());
	}

	private boolean isFunction(Token t) {
		char str[] = t.getValue().toCharArray();
		int isFunction = 0;

		for (int i = 0; i < str.length; i++) {
			if (str[i] == '(') isFunction++;
			else if (str[i] == ')') isFunction++;
		}

		if (isFunction == 2) return true;

		return false;
	}

	public void interpret(List<Token> tokens) {
		Expression exp = null;
		Token t = null;

		for (int i = 0; i < tokens.size(); i++) {
			t = tokens.get(i);

			// checks store expressions
			if (t.getValue().contentEquals("store") &&
					tokens.get(i+2).getValue().contentEquals("in")) {
				exp = new Expression(ExpressionType.Store, tokens.subList(i, i+4));
				i += 3;
			}
			// checks assign expressions
			else if (t.getValue().contentEquals("assign") &&
					isOperator(tokens.get(i+2)) && tokens.get(i+4).getValue().contentEquals("to")) {
				exp = new Expression(ExpressionType.Assign, tokens.subList(i, i+6));
				i += 5;
			}
			// checks function expressions
			else if (isFunction(t)) {
				exp = new Expression(ExpressionType.Function, tokens.subList(i, i+1));
			}
			// checks function define
			else if (t.getValue().contentEquals("function")) {
				String name = tokens.get(++i).getValue();
				List<Token> functionToken = new ArrayList<Token>();
				Token runner = tokens.get(++i);
				for (; !runner.getValue().contentEquals("end"); i++) {
					functionToken.add(runner);
					runner = tokens.get(i+1);
				}
				executer.functionExecuter.addFunction(new Function(name, functionToken));
				continue;
			}

			executer.execute(exp);
			exp = null;
		}
	}
}
