package jask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private void executeAssign(List<Token> tokens) {
		//assign a plus b to c
		Variable var1 = heap.get(tokens.get(1).getValue());
		Variable var2 = heap.get(tokens.get(3).getValue());
		Variable varD = heap.get(tokens.get(5).getValue());

		String operator = tokens.get(2).getValue();

		if (var1.getType() == VariableType.Number &&
				var2.getType() == VariableType.Number &&
				varD.getType() == VariableType.Number) {
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

			heap.put(tokens.get(5).getValue(), varD);
			return;
		}

		if (var1.getType() == VariableType.String &&
				var2.getType() == VariableType.String &&
				varD.getType() == VariableType.String) {
			if (operator.contentEquals("plus")) {
				StringBuilder builder = new StringBuilder();
				builder.append(var1.getStringValue().substring(0, var1.getStringValue().length()-1));
				builder.append(var2.getStringValue().substring(1, var2.getStringValue().length()));
				varD.setStringValue(builder.toString());
				heap.put(tokens.get(5).getValue(), varD);
				return;
			}
		}

		Error.printErrorOperatorNotApplicable(operator, var1.toString(), var2.toString());
	}

	private void executeFunction(List<Token> tokens) {
		String value = tokens.get(0).getValue();
		String functionName = value.substring(0, value.indexOf('('));
		String param = value.substring(value.indexOf('(')+1, value.indexOf(')'));

		// check build-in functions
		if (functionName.contentEquals("print")) {
			Variable var = heap.get(param);
			if (var == null) {
				Error.printErrorVariableNotDefined(param);
			}
			else {
				System.out.println(var.toString());
			}
			return;
		}

		List<Variable> functionHeap = new ArrayList<Variable>();
		functionHeap.add(heap.get(param));
		functionExecuter.executeFunction(functionName, functionHeap);
	}

	private void executeStore(List<Token> tokens) {
		String variableValue = tokens.get(1).getValue();
		String variableName = tokens.get(3).getValue();

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
		case Function: executeFunction(exp.getTokens()); break;
		case Store: executeStore(exp.getTokens()); break;
		default:
			break;
		}
	}
}
