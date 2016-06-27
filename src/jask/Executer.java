package jask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Executer {
	private HashMap<String, Variable> heap;
	public FunctionExecuter functionExecuter;

	public Executer() {
		heap = new HashMap<>();
		functionExecuter = new FunctionExecuter();
	}

	public Executer(HashMap<String, Variable> heap) {
		this.heap = heap;
	}

	public Variable getVariableFromHeap(String var) {
		return heap.get(var);
	}

	private void executeAssign(List<String> tokens) {
		// assign a to b
		if (tokens.size() == 4) {
			String varStr = tokens.get(1);

			if (!Interpreter.isFunction(varStr)) {
				Variable var = heap.get(varStr);
				if (var == null) {
					heap.put(tokens.get(3), new Variable(varStr));
				}
				else {
					heap.put(tokens.get(3), new Variable(var));
				}
			}
			else {
				heap.put(tokens.get(3), new Variable(executeFunction(varStr)));
			}
			return;
		}


		//assign a plus b to c

		String var1Str = tokens.get(1);
		String var2Str = tokens.get(3);
		String varDStr = tokens.get(5);

		Variable var1 = heap.get(var1Str);
		Variable var2 = heap.get(var2Str);
		Variable varD = heap.get(varDStr);

		if (varD == null) {
			Error.printErrorVariableNotDefined(varDStr);
			return;
		}

		String operator = tokens.get(2);

		if (var1 == null) {
			var1 = new Variable(var1Str);
		}
		if (var2 == null) {
			var2 = new Variable(var2Str);
		}

		if (var1.getType() == VariableType.Number && var2.getType() == VariableType.Number) {
			varD.setStringValue(null);
			if (operator.contentEquals("plus")) {
				varD.setDoubleValue(var1.getDoubleValue() + var2.getDoubleValue());
			}
			else if (operator.contentEquals("minus")) {
				varD.setDoubleValue(var1.getDoubleValue() - var2.getDoubleValue());
			}
			else if (operator.contentEquals("times")) {
				varD.setDoubleValue(var1.getDoubleValue() * var2.getDoubleValue());
			}
			else if (operator.contentEquals("divide")) {
				varD.setDoubleValue(var1.getDoubleValue() / var2.getDoubleValue());
			}

			heap.put(tokens.get(5), varD);
			return;
		}

		if (var1.getType() == VariableType.String && var2.getType() == VariableType.String) {
			if (operator.contentEquals("plus")) {
				varD.setDoubleValue(0.0);
				StringBuilder builder = new StringBuilder();
				builder.append(var1.getStringValue().substring(0, var1.getStringValue().length()-1));
				builder.append(var2.getStringValue().substring(1, var2.getStringValue().length()));
				varD.setStringValue(builder.toString());
				heap.put(tokens.get(5), varD);
				return;
			}
		}

		Error.printErrorOperatorNotApplicable(operator, var1.toString(), var2.toString());
	}

	private String executeFunction(String token) {
		String functionName = token.substring(0, token.indexOf('('));
		String param = token.substring(token.indexOf('(')+1, token.indexOf(')'));
		String[] params = param.split(":");

		// check build-in functions
		if (functionName.contentEquals("print")) {
			Variable var = heap.get(params[0]);
			if (var == null) {
				Error.printErrorVariableNotDefined(param);
			}
			else {
				System.out.println(var.toString());
			}
			return "";
		}

		if (functionName.contentEquals("read")) {
			Scanner scanner = new Scanner(System.in);
			String input =  '"' + scanner.nextLine() + '"';
			scanner.close();
			return input;
		}

		List<Variable> functionHeap = new ArrayList<Variable>();
		for (int i = 0; i < params.length; i++) {
			Variable var = heap.get(params[i]);
			if (var == null) {
				Error.printErrorVariableNotDefined(params[i]);
			}
			else {
				functionHeap.add(var);
			}
		}
		return functionExecuter.executeFunction(functionName, functionHeap);
	}

	private void executeStore(List<String> tokens) {
		String variableValue = tokens.get(1);
		String variableName = tokens.get(3);

		if (heap.containsKey(variableName)) {
			Error.printErrorVariableAlreadyDefined(variableName);
			return;
		}

		heap.put(variableName, new Variable(variableValue));
	}

	public void execute(Expression exp) {
		if (exp == null) return;

		switch (exp.getType()) {
		case Assign: executeAssign(exp.getTokens()); break;
		case Function: executeFunction(exp.getTokens().get(0)); break;
		case Store: executeStore(exp.getTokens()); break;
		default:
			break;
		}
	}

	public boolean executeStatement(Expression exp) {
		List<String> tokens = exp.getTokens();
		Variable var1 = heap.get(tokens.get(1));
		Variable var2 = heap.get(tokens.get(3));
		String operator = tokens.get(2);

		if (operator.contentEquals("equals")) {
			return var1.equals(var2);
		}

		Error.printErrorOperatorNotApplicable(operator, "", "");

		return false;
	}
}
