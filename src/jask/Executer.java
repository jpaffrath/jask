package jask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Executes jask code
 *
 * @author Julius Paffrath
 *
 */
public class Executer {
	private HashMap<String, Variable> heap;
	private FunctionExecuter functionExecuter;
	private InternalFunctions internalFunctions;
	private List<Executer> modules;
	private String name;

	public Executer() {
		this.heap = new HashMap<>();
		this.functionExecuter = new FunctionExecuter();
		this.internalFunctions = new InternalFunctions();
		this.modules = new ArrayList<Executer>();
		this.name = "Main";
	}

	public Executer(HashMap<String, Variable> heap, FunctionExecuter functionExecuter, List<Executer> modules) {
		this();
		this.heap = heap;
		this.functionExecuter = functionExecuter;
		this.modules = modules;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Variable getVariableFromHeap(String var) {
		return this.heap.get(var);
	}

	public Variable executeFunction(String token) {
		String functionName = token.substring(0, token.indexOf('('));
		String param = token.substring(token.indexOf('(')+1, token.lastIndexOf(')'));
		List<String> params = Helpers.splitParams(param);

		List<Variable> functionHeap = new ArrayList<Variable>();
		if (!param.contentEquals("")) {
			for (int i = 0; i < params.size(); i++) {
				String temp = params.get(i);

				if (Variable.isString(temp) || Variable.isNumber(temp) || Variable.isBoolean(temp) || temp.contentEquals("NULL")) {
					functionHeap.add(new Variable(temp));
				}
				else if (Interpreter.isFunction(temp)) {
					functionHeap.add(executeFunction(temp));
				}
				else {
					Variable var = getVariableFromHeap(params.get(i));
					if (var == null) {
						Error.printErrorVariableNotDefined(params.get(i));
					}
					else {
						functionHeap.add(var);
					}
				}
			}
		}

		if (this.internalFunctions.isInternalFunction(functionName)) {
			return new Variable(this.internalFunctions.executeFunction(functionHeap, functionName, param));
		}

		if (this.internalFunctions.isInternalListFunction(functionName)) {
			return new VariableList(this.internalFunctions.executeFunction(functionHeap, functionName, param));
		}

		String varVal = "";

		if (this.functionExecuter.hasFunction(functionName)) {
			varVal = this.functionExecuter.executeFunction(functionName, functionHeap, this.modules);
			this.setLocalHeapFromFunction(this.functionExecuter.getFunction(functionName).getHeap());
			this.functionExecuter.destroyFunctionHeap(functionName);
		}
		else {
			boolean functionFound = false;

			for (Executer module : this.modules) {
				if (module.functionExecuter.hasFunction(functionName)) {
					varVal = module.functionExecuter.executeFunction(functionName, functionHeap, this.modules);
					module.functionExecuter.destroyFunctionHeap(functionName);
					functionFound = true;
					break;
				}
			}

			if (!functionFound) {
				Error.terminateInterpret("The function '" + functionName + "' is not defined!");
			}
		}

		if (varVal.isEmpty()) return new Variable("NULL");
		if (varVal.contains(":") && !Variable.isString(varVal)) return new VariableList(varVal);
		return new Variable(varVal);
	}

	/**
	 * Adds a new module to the current context
	 *
	 * This method checks if the given module is already
	 * loaded in the current context
	 *
	 * @param module new module for contexts
	 */
	public void addModule(Executer module) {
		if (this.hasModule(module.getName())) {
			Error.printErrorModuleAlreadyLoaded(module.getName());
			return;
		}

		this.modules.add(module);
	}

	/**
	 * Returns a list of the loaded module names in the current context
	 *
	 * @return list of strings containing the names of the loaded modules
	 */
	public List<String> getModuleNames() {
		List<String> moduleNames = new ArrayList<String>(this.modules.size());

		for (Executer module : this.modules) {
			moduleNames.add(module.getName());
		}

		return moduleNames;
	}

	/**
	 * Checks if a given module name is already loaded in the current context
	 *
	 * @param moduleName name of the module
	 * @return true if the given module name is already loaded, false otherwise
	 */
	public boolean hasModule(String moduleName) {
		return this.getModuleNames().contains(moduleName);
	}

	private void executeAssign(List<String> tokens) {
		// assign a to b
		if (tokens.size() == 4) {
			String varStr = tokens.get(1);

			if (!Interpreter.isFunction(varStr)) {
				Variable var = getVariableFromHeap(varStr);
				if (var == null) {
					this.heap.put(tokens.get(3), new Variable(varStr));
				}
				else if (var instanceof VariableList){
					this.heap.put(tokens.get(3), new VariableList(var.toString()));
				}
				else {
					this.heap.put(tokens.get(3), new Variable(var));
				}
			}
			else {
				this.heap.put(tokens.get(3), executeFunction(varStr));
			}
			return;
		}

		//assign a plus b to c
		String var1Str = tokens.get(1);
		String var2Str = tokens.get(3);
		String varDStr = tokens.get(5);

		Variable var1 = null;
		Variable var2 = null;

		if (Interpreter.isFunction(var1Str)) {
			var1 = executeFunction(var1Str);
		}
		else {
			var1 = getVariableFromHeap(var1Str);
		}

		if (Interpreter.isFunction(var2Str)) {
			var2 = executeFunction(var2Str);
		}
		else {
			var2 = getVariableFromHeap(var2Str);
		}

		Variable varD = getVariableFromHeap(varDStr);

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
			else if (operator.contentEquals("mod")) {
				varD.setDoubleValue(var1.getDoubleValue() % var2.getDoubleValue());
			}

			this.heap.put(tokens.get(5), varD);
			return;
		}

		if (var1.getType() == VariableType.String && var2.getType() == VariableType.String) {
			if (operator.contentEquals("plus")) {
				varD.setStringValue(var1.getStringValue() + var2.getStringValue());
				this.heap.put(tokens.get(5), varD);
				return;
			}
		}

		Error.printErrorOperatorNotApplicable(operator, var1.toString(), var2.toString());
	}

	private void executeIncrement(String increment) {
		Variable var = this.getVariableFromHeap(increment);

		if (var == null) {
			Error.printErrorVariableNotDefined(increment);
			return;
		}

		if (var.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(increment);
			return;
		}

		var.setDoubleValue(var.getDoubleValue()+1);
	}

	private void executeDecrement(String decrement) {
		Variable var = this.getVariableFromHeap(decrement);

		if (var == null) {
			Error.printErrorVariableNotDefined(decrement);
			return;
		}

		if (var.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(decrement);
			return;
		}

		var.setDoubleValue(var.getDoubleValue()-1);
	}

	private void executeStore(List<String> tokens) {
		String variableValue = tokens.get(1);
		String variableName = tokens.get(3);

		if (this.heap.containsKey(variableName)) {
			Error.printErrorVariableAlreadyDefined(variableName);
			return;
		}

		if (Interpreter.isFunction(variableValue)) {
			this.heap.put(variableName, executeFunction(variableValue));
		}
		else {
			if (this.heap.containsKey(variableValue)) {
				Variable varOld = this.getVariableFromHeap(variableValue);
				if (varOld instanceof VariableList) {
					this.heap.put(variableName, new VariableList(varOld.toString()));
				}
				else {
					this.heap.put(variableName, new Variable(varOld));
				}
			}
			else {
				Variable var = new Variable(variableValue);
				if (var.getType() == VariableType.NoType && !variableValue.contentEquals("NULL")) {
					Error.printErrorValueNotApplicable(variableValue);
					return;
				}

				this.heap.put(variableName, var);
			}
		}
	}

	private String executeRun(List<String> tokens) {
		String ret = "";

		if (tokens.get(0).contentEquals("while")) {
			List<String> whileTokens = new ArrayList<String>();
			whileTokens.add("if");
			whileTokens.add(tokens.get(1));
			whileTokens.add(tokens.get(2));
			whileTokens.add(tokens.get(3));

			Interpreter interpreter = new Interpreter(this);
			Expression ifExp = new Expression(ExpressionType.Statement, whileTokens);

			while (executeStatement(ifExp)) {
				ret = interpreter.interpret(tokens.subList(4, tokens.size() - 1));
				if (ret != "") break;
			}

			return ret;
		}

		Variable runner = getVariableFromHeap(tokens.get(1));
		Variable maxVal = getVariableFromHeap(tokens.get(3));

		if (maxVal == null) {
			if (Variable.isNumber(tokens.get(3))) {
				maxVal = new Variable(tokens.get(3));
			}
			else {
				Error.printErrorVariableIsNotANumber(tokens.get(3));
				return "";
			}
		}
		else if (maxVal.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(tokens.get(3));
			return "";
		}

		List<String> assignTokens = new ArrayList<String>();
		assignTokens.add("assign");
		assignTokens.add(tokens.get(1));
		assignTokens.add(tokens.get(6));
		assignTokens.add(tokens.get(7));
		assignTokens.add("to");
		assignTokens.add(tokens.get(1));

		Interpreter interpreter = new Interpreter(this);

		for (int i = (int)runner.getDoubleValue(); i < maxVal.getDoubleValue();) {
			ret = interpreter.interpret(tokens.subList(8, tokens.size() - 1));
			if (ret != "") break;
			executeAssign(assignTokens);
			i = (int)getVariableFromHeap(tokens.get(1)).getDoubleValue();
		}

		return ret;
	}

	private void executeConvert(List<String> tokens) {
		String variableName = tokens.get(1);
		String convert = tokens.get(3);

		Variable var = getVariableFromHeap(variableName);
		if (var == null) {
			Error.printErrorVariableNotDefined(variableName);
		}
		else {
			if (convert.contentEquals("string") && var.getType() == VariableType.Number) {
				String temp = "\"" + var.toString() + "\"";
				this.heap.put(variableName, new Variable(temp));
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

				this.heap.put(variableName, var);
			}
			else {
				Error.printErrorConvertNotApplicable(convert, variableName);
			}
		}
	}

	public boolean executeStatement(Expression exp) {
		List<String> tokens = exp.getTokens();
		Variable var1 = null;
		Variable var2 = null;

		if (Interpreter.isFunction(tokens.get(1))) {
			var1 = executeFunction(tokens.get(1));
		}
		else {
			var1 = getVariableFromHeap(tokens.get(1));
		}

		if (Interpreter.isFunction(tokens.get(3))) {
			var2 = executeFunction(tokens.get(3));
		}
		else {
			var2 = getVariableFromHeap(tokens.get(3));
		}

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

		if (operator.contentEquals("greater")) {
			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("greater", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() > var2.getDoubleValue();
		}

		if (operator.contentEquals("smaller")) {
			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("smaller", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() < var2.getDoubleValue();
		}

		if (operator.contentEquals("greaterequal")) {
			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("greater", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() >= var2.getDoubleValue();
		}

		if (operator.contentEquals("smallerequal")) {
			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("smaller", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() <= var2.getDoubleValue();
		}

		if (operator.contentEquals("mod") &&
				var1.getType() == VariableType.Number &&
				var2.getType() == VariableType.Number) {

			Variable var3 = getVariableFromHeap(tokens.get(5));
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

	public void addFunction(Function function) {
		HashMap<String, Variable> functionHeap = new HashMap<>(this.heap.size());

		// copy local heap and add access operator to keys
		for (String key : this.heap.keySet()) {
			functionHeap.put('!' + key, this.heap.get(key));
		}

		function.setHeap(functionHeap);
		this.functionExecuter.addFunction(function);
	}

	private void setLocalHeapFromFunction(HashMap<String, Variable> functionHeap) {
		for (String key : functionHeap.keySet()) {
			if (key.charAt(0) == '!') {
				this.heap.put(key.substring(1), functionHeap.get(key));
			}
		}
	}

	public String execute(Expression exp) {
		if (exp == null) return "";
		String ret = "";

		switch (exp.getType()) {
		case Assign: executeAssign(exp.getTokens()); break;
		case Function: executeFunction(exp.getTokens().get(0)); break;
		case Store: executeStore(exp.getTokens()); break;
		case Runner: ret = executeRun(exp.getTokens()); break;
		case Convert: executeConvert(exp.getTokens()); break;
		case Increment: executeIncrement(exp.getTokens().get(0)); break;
		case Decrement: executeDecrement(exp.getTokens().get(0)); break;
		default:
			break;
		}

		return ret;
	}
}