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
				else if (var instanceof VariableList){
					heap.put(tokens.get(3), new VariableList(var.toString()));
				}
				else {
					heap.put(tokens.get(3), new Variable(var));
				}
			}
			else {
				String res = executeFunction(varStr);
				if (res.contains(":") && !Variable.isString(res)) {
					heap.put(tokens.get(3), new VariableList(res));
				}
				else {
					heap.put(tokens.get(3), new Variable(res));
				}
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
				varD.setStringValue(var1.getStringValue() + var2.getStringValue());
				heap.put(tokens.get(5), varD);
				return;
			}
		}

		Error.printErrorOperatorNotApplicable(operator, var1.toString(), var2.toString());
	}

	private String executeFunction(String token) {
		String functionName = token.substring(0, token.indexOf('('));
		String param = token.substring(token.indexOf('(')+1, token.indexOf(')'));
		List<String> params = Helpers.splitParams(param);

		// check build-in functions
		if (functionName.contentEquals("print")) {
			String temp = params.get(0);
			if (Variable.isString(temp) || Variable.isNumber(temp)) {
				System.out.print(temp.substring(1, temp.length()-1));
				return "";
			}
			Variable var = heap.get(temp);
			if (var == null) {
				Error.printErrorVariableNotDefined(param);
			}
			else {
				if (var instanceof VariableList) {
					System.out.print(((VariableList) var).getPrintString());
				}
				else {
					System.out.print(var.toString());
				}
			}
			return "";
		}

		if (functionName.contentEquals("printLine")) {
			String temp = params.get(0);
			if (Variable.isString(temp) || Variable.isNumber(temp)) {
				System.out.println(temp.substring(1, temp.length()-1));
				return "";
			}
			Variable var = heap.get(temp);
			if (var == null) {
				Error.printErrorVariableNotDefined(param);
			}
			else {
				if (var instanceof VariableList) {
					System.out.println(((VariableList) var).getPrintString());
				}
				else {
					System.out.println(var.toString());
				}
			}
			return "";
		}

		if (functionName.contentEquals("read")) {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			return '"' + scanner.nextLine() + '"';
		}

		if (functionName.contentEquals("list")) {
			return param;
		}

		if (functionName.contentEquals("listGet")) {
			Variable var = heap.get(params.get(0));
			if (var == null || !(var instanceof VariableList)) {
				Error.printErrorVariableIsNotAList(params.get(0));
				return "";
			}

			return ((VariableList)var).getElementAtIndex(Integer.parseInt(params.get(1)));
		}

		if (functionName.contentEquals("listSize")) {
			Variable var = heap.get(params.get(0));
			if (var == null || !(var instanceof VariableList)) {
				Error.printErrorVariableIsNotAList(params.get(0));
				return "";
			}

			return Integer.toString(((VariableList)var).getSize());
		}

		List<Variable> functionHeap = new ArrayList<Variable>();
		if (!param.contentEquals("")) {
			for (int i = 0; i < params.size(); i++) {
				String temp = params.get(i);

				if (Variable.isString(temp) || Variable.isNumber(temp)) {
					functionHeap.add(new Variable(temp));
				}
				else {
					Variable var = heap.get(params.get(i));
					if (var == null) {
						Error.printErrorVariableNotDefined(params.get(i));
					}
					else {
						functionHeap.add(var);
					}
				}
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

		if (Interpreter.isFunction(variableValue)) {
			Variable var = null;
			variableValue = executeFunction(variableValue);
			if (variableValue.contains(":") && !Variable.isString(variableValue)) {
				String[] params = variableValue.split(":");
				String newParams = "";

				for (String param : params) {
					Variable tempVar = heap.get(param);
					if (tempVar != null) {
						newParams += tempVar.toString() + ":";
					}
					else {
						newParams += param + ":";
					}
				}

				// remove last ':' sign from parameter list
				newParams = newParams.substring(0, newParams.length()-1);

				var = new VariableList(newParams);
			}
			else {
				var = new Variable(variableValue);
			}

			heap.put(variableName, var);
			return;
		}

		heap.put(variableName, new Variable(variableValue));
	}

	private void executeRun(List<String> tokens) {
		Variable runner = heap.get(tokens.get(0));
		Variable maxVal = heap.get(tokens.get(2));

		List<String> assignTokens = new ArrayList<String>();
		assignTokens.add("assign");
		assignTokens.add(tokens.get(0));
		assignTokens.add(tokens.get(5));
		assignTokens.add(tokens.get(6));
		assignTokens.add("to");
		assignTokens.add(tokens.get(0));

		Interpreter interpreter = new Interpreter(this);

		for (int i = (int)runner.getDoubleValue(); i < maxVal.getDoubleValue();) {
			interpreter.interpret(tokens.subList(7, tokens.size() - 1));
			executeAssign(assignTokens);
			i = (int)heap.get(tokens.get(0)).getDoubleValue();
		}
	}

	private void executeConvert(List<String> tokens) {
		String variableName = tokens.get(1);
		String convert = tokens.get(3);

		Variable var = heap.get(variableName);
		if (var == null) {
			Error.printErrorVariableNotDefined(variableName);
		}
		else {
			if (convert.contentEquals("string") && var.getType() == VariableType.Number) {
				String temp = "\"" + var.toString() + "\"";
				heap.put(variableName, new Variable(temp));
			}
			else if (convert.contentEquals("number") && var.getType() == VariableType.String) {
				String strValue = var.getStringValue().replace("\"", "");

				if (Variable.isNumber(strValue)) {
					var.setDoubleValue(Double.parseDouble(strValue));
				}
				else {
					Error.printErrorConvertNotApplicable(convert, variableName);
					var.setType(VariableType.NoType);
				}

				heap.put(variableName, var);
			}
			else {
				Error.printErrorConvertNotApplicable(convert, variableName);
			}
		}
	}

	public boolean executeStatement(Expression exp) {
		List<String> tokens = exp.getTokens();
		Variable var1 = heap.get(tokens.get(1));
		Variable var2 = heap.get(tokens.get(3));
		String operator = tokens.get(2);

		if (var1 == null) {
			var1 = new Variable(tokens.get(1));
		}
		if (var2 == null) {
			var2 = new Variable(tokens.get(3));
		}

		if (operator.contentEquals("equals")) {
			return var1.equals(var2);
		}

		if (operator.contentEquals("unequals")) {
			return !var1.equals(var2);
		}

		if (operator.contentEquals("mod") &&
				var1.getType() == VariableType.Number &&
				var2.getType() == VariableType.Number) {

			Variable var3 = heap.get(tokens.get(5));
			if (var3 == null) {
				var3 = new Variable(tokens.get(5));
			}

			if (var3.getType() == VariableType.Number) {
				return (var1.getDoubleValue() % var2.getDoubleValue() == var3.getDoubleValue());
			}
		}

		Error.printErrorOperatorNotApplicable(operator, "", "");

		return false;
	}

	public void execute(Expression exp) {
		if (exp == null) return;

		switch (exp.getType()) {
		case Assign: executeAssign(exp.getTokens()); break;
		case Function: executeFunction(exp.getTokens().get(0)); break;
		case Store: executeStore(exp.getTokens()); break;
		case Runner: executeRun(exp.getTokens()); break;
		case Convert: executeConvert(exp.getTokens()); break;
		default:
			break;
		}
	}
}