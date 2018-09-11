package jask;

import static jask.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import expression.Expression;
import expression.ExpressionType;
import function.Function;
import function.FunctionExecuter;
import function.InternalFunctions;
import helper.Error;
import helper.Helpers;
import variable.Variable;
import variable.VariableFunction;
import variable.VariableList;
import variable.VariableType;

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

	/**
	 * General constructor
	 */
	public Executer() {
		this.heap = new HashMap<>();
		this.functionExecuter = new FunctionExecuter();
		this.internalFunctions = new InternalFunctions();
		this.modules = new ArrayList<Executer>();
		this.name = "Main";
	}

	/**
	 * Initializes a new executer for function executing
	 *
	 * @param heap heap of the function
	 * @param functionExecuter functionExecuter to be used
	 * @param modules list of modules
	 */
	public Executer(HashMap<String, Variable> heap, FunctionExecuter functionExecuter, List<Executer> modules) {
		this();
		this.heap = heap;
		this.functionExecuter = functionExecuter;
		this.modules = modules;
	}

	/**
	 * Sets the name of the executer
	 *
	 * @param name name to be set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name
	 *
	 * @return local name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns variable from heap with given name
	 *
	 * @param var name of the variable
	 * @return variable from the heap
	 */
	public Variable getVariableFromHeap(String var) {
		return this.heap.get(var);
	}
	
	/**
	 * Checks if the heap contains a variable with a given name
	 * 
	 * @param variableName name of the variable
	 * @return true if the heap contains a variable with the given name
	 */
	public boolean hasVariable(String variableName) {
		return this.heap.containsKey(variableName);
	}

	/**
	 * Executes a function
	 *
	 * @param token tokens of the function
	 * @return result of the execution
	 */
	public Variable executeFunction(String token) {
		String functionName = token.substring(0, token.indexOf('('));
		String parameterList = token.substring(token.indexOf('(')+1, token.lastIndexOf(')'));
		
		List<String> params = Helpers.splitParams(parameterList);
		List<Variable> functionHeap = new ArrayList<Variable>();
		
		// if the function call contains parameters, parse them and add the variables to the function heap
		if (parameterList.isEmpty() == false) {
			for (String var : params) {
				
				// if the parameter is passed as a value, create a new variable on the heap
				if (Variable.isString(var) || Variable.isNumber(var) || Variable.isBoolean(var) || var.contentEquals(NULL)) {
					functionHeap.add(new Variable(var));
				}
				// if the parameter is a function call, execute the function and push its return value on the heap
				else if (Interpreter.isFunction(var)) {
					functionHeap.add(executeFunction(var));
				}
				// if parameter is a function, create a new function variable on the heap
				else if (this.functionExecuter.hasFunction(var)) {
					Function func = this.functionExecuter.getFunction(var);
					functionHeap.add(new VariableFunction(func));
				}
				// check if the parameter is a local variable and add it to the heap
				else {
					Variable tempvar = getVariableFromHeap(var);
					if (tempvar == null) {
						Error.printErrorVariableNotDefined(var);
					}
					else {
						functionHeap.add(tempvar);
					}
				}
			}
		}

		// check if the call is an internal function
		if (this.internalFunctions.isInternalFunction(functionName)) {
			return this.internalFunctions.executeFunction(functionHeap, functionName, parameterList);
		}

		String varVal = "";

		// check if the call is a local implemented function or if the call was passed as a parameter variable
		if (this.functionExecuter.hasFunction(functionName) || this.hasVariable(functionName)) {
			if (this.hasVariable(functionName)) {
				this.functionExecuter.addFunction(((VariableFunction)this.getVariableFromHeap(functionName)).getFunction());
			}
			
			varVal = this.functionExecuter.executeFunction(functionName, functionHeap, getLocalHeapForFunction(), this.modules);
			this.setLocalHeapFromFunction(this.functionExecuter.getFunction(functionName).getHeap());
			this.functionExecuter.destroyFunctionHeap(functionName);
			
			if (this.hasVariable(functionName)) {
				this.functionExecuter.removeFunction(((VariableFunction)this.getVariableFromHeap(functionName)).getFunction());
			}
		}
		// check if the call is a module implemented function
		else {
			boolean functionFound = false;

			for (Executer module : this.modules) {
				if (module.functionExecuter.hasFunction(functionName)) {
					varVal = module.functionExecuter.executeFunction(functionName, functionHeap, getLocalHeapForFunction(), this.modules);
					module.functionExecuter.destroyFunctionHeap(functionName);
					functionFound = true;
					break;
				}
			}

			if (!functionFound) {
				Error.terminateInterpret("The function '" + functionName + "' is not defined!");
			}
		}

		// create proper variable before returning it
		if (varVal.isEmpty()) return new Variable(NULL);
		if (varVal.contains(":") && !Variable.isString(varVal)) return new VariableList(varVal);
		return new Variable(varVal);
	}

	/**
	 * Adds a new module to the current context
	 *
	 * If the module is already loaded, it will
	 * be removed and loaded again
	 *
	 * @param module new module for contexts
	 */
	public void addModule(Executer module) {
		if (this.hasModule(module.getName())) {
			this.removeModule(module.getName());
		}

		this.modules.add(module);
	}

	/**
	 * Removes a loaded module from the current context
	 *
	 * @param module name of the module to remove
	 */
	public void removeModule(String module) {
		for (Executer curModule : this.modules) {
			if (curModule.getName().contentEquals(module)) {
				this.modules.remove(curModule);
				return;
			}
		}
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

	/**
	 * Executes a calculation
	 *
	 * @param operand1 first operand
	 * @param operand2 second operand
	 * @param type type of the calculation
	 * @return a new variable containing the result of the calculation
	 */
	public Variable executeCalculation(Variable operand1, Variable operand2, CalculationType type) {
		// number calculations
		if ((operand1.getType() == VariableType.Number) && (operand2.getType() == VariableType.Number)) {
			switch (type) {
			case Plus:   return new Variable(operand1.getDoubleValue() + operand2.getDoubleValue());
			case Minus:  return new Variable(operand1.getDoubleValue() - operand2.getDoubleValue());
			case Times:  return new Variable(operand1.getDoubleValue() * operand2.getDoubleValue());
			case Divide: return new Variable(operand1.getDoubleValue() / operand2.getDoubleValue());
			case NoType:
			default:
				return null;
			}
		}

		// string concatenation
		if ((operand1.getType() == VariableType.String) && (operand2.getType() == VariableType.String)) {
			switch (type) {
			case Plus: return new Variable('"' + operand1.getStringValue() + operand2.getStringValue() + '"');
			default:
				return null;
			}
		}
		if ((operand1.getType() == VariableType.String) && (operand2.getType() == VariableType.Number)) {
			switch (type) {
			case Times:
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < (int)operand2.getDoubleValue(); i++) {
					buf.append(operand1.getStringValue());
				}
				return new Variable('"' + buf.toString() + '"');
			default:
				return null;
			}
		}

		return null;
	}

	/**
	 * Executes assign
	 *
	 * @param tokens tokens of the assign
	 */
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
				Variable gen = executeFunction(varStr);
				if (gen instanceof VariableList) {
					this.heap.put(tokens.get(3), new VariableList((VariableList)gen));
				}
				else {
					this.heap.put(tokens.get(3), gen);
				}
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

		varD = this.executeCalculation(var1, var2, CalculationType.getType(operator));

		if (varD == null) {
			Error.printErrorOperatorNotApplicable(operator, var1.toString(), var2.toString());
		}

		this.heap.put(tokens.get(5), varD);
	}

	/**
	 * Executes increment
	 *
	 * @param increment variable to be incremented
	 */
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

	/**
	 * Executes decrement
	 *
	 * @param decrement variable to be decremented
	 */
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

	/**
	 * Executes store
	 *
	 * @param tokens tokens of the store
	 */
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
				if (var.getType() == VariableType.NoType && !variableValue.contentEquals(NULL)) {
					Error.printErrorValueNotApplicable(variableValue);
					return;
				}

				this.heap.put(variableName, var);
			}
		}
	}

	/**
	 * Executes run statement
	 *
	 * @param tokens tokens of the run
	 * @return result of the run
	 */
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

		if (tokens.get(0).contentEquals("run")) {
			// variables containing the start and end values for the loop
			Variable startVal = getVariableFromHeap(tokens.get(3));
			Variable endVal = getVariableFromHeap(tokens.get(5));
			
			// the name of the variable containing the number at each iteration
			String runnerName = tokens.get(1);
			
			// error if it already exists as a variable
			if (this.hasVariable(runnerName)) {
				Error.printErrorVariableAlreadyDefined(runnerName);
				return "";
			}

			// if start variable is passed as a number, create new temporary variable
			if (startVal == null) {
				if (Variable.isNumber(tokens.get(3))) {
					startVal = new Variable(tokens.get(3));
				}
				else if (Interpreter.isFunction(tokens.get(3))) {
					startVal = new Variable(executeFunction(tokens.get(3)));
				}
				else {
					Error.printErrorVariableIsNotANumber(tokens.get(3));
					return "";
				}
			}
			else if (startVal.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(tokens.get(3));
				return "";
			}
			
			// if end variable is passed as a number, create new temporary variable
			if (endVal == null) {
				if (Variable.isNumber(tokens.get(5))) {
					endVal = new Variable(tokens.get(5));
				}
				else if (Interpreter.isFunction(tokens.get(5))) {
					endVal = new Variable(executeFunction(tokens.get(5)));
				}
				else {
					Error.printErrorVariableIsNotANumber(tokens.get(5));
					return "";
				}
			}
			else if (endVal.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(tokens.get(3));
				return "";
			}

			// jask code for performing the assign at each iteration
			List<String> assignTokens = new ArrayList<String>();
			assignTokens.add("assign");
			assignTokens.add(tokens.get(1));
			assignTokens.add(tokens.get(8));
			assignTokens.add(tokens.get(9));
			assignTokens.add("to");
			assignTokens.add(tokens.get(1));

			Interpreter interpreter = new Interpreter(this);
			Variable runner = new Variable(runnerName);
			
			// perform actual loop
			
			int startValInteger = (int)startVal.getDoubleValue();
			int endValInteger = (int)endVal.getDoubleValue();
			
			if (startValInteger <= endValInteger) {
				for (int i = startValInteger; i < endValInteger;) {
					runner.setDoubleValue(i);
					interpreter.getExecuter().heap.put(runnerName, runner);
					
					ret = interpreter.interpret(tokens.subList(10, tokens.size() - 1));
					if (ret != "") break;
					executeAssign(assignTokens);
					i = (int)getVariableFromHeap(tokens.get(1)).getDoubleValue();
				}
			}
			else {
				for (int i = startValInteger; i > endValInteger;) {
					runner.setDoubleValue(i);
					interpreter.getExecuter().heap.put(runnerName, runner);
					
					ret = interpreter.interpret(tokens.subList(10, tokens.size() - 1));
					if (ret != "") break;
					executeAssign(assignTokens);
					i = (int)getVariableFromHeap(tokens.get(1)).getDoubleValue();
				}
			}
			
			// remove variable from heap, because it only exists in the context of the run loop
			interpreter.getExecuter().heap.remove(runnerName);

			return ret;
		}
		
		if (tokens.get(0).contentEquals("for")) {
			// this variable list contains the elements
			Variable runner = getVariableFromHeap(tokens.get(3));
			
			// if the list does not exists, it is probably a list() function call
			if (runner == null) {
				// if if is a list() call, execute it so we have a temporarily runner variable
				if (Interpreter.isFunction(tokens.get(3))) {
					runner =  executeFunction(tokens.get(3));
				}
			}
			
			if (runner instanceof VariableList == false) {
				Error.printErrorVariableIsNotAList(tokens.get(3));
				return "";
			}
			
			// the name of the variable containing the elements at each iteration
			String elementName = tokens.get(1);
			
			// error if it already exists as a variable
			if (this.hasVariable(elementName)) {
				Error.printErrorVariableAlreadyDefined(elementName);
				return "";
			}
			
			Interpreter interpreter = new Interpreter(this);
			VariableList elements = (VariableList)runner;
			
			for (int i = 0; i < ((VariableList)runner).getSize(); i++) {
				interpreter.getExecuter().heap.put(elementName, new Variable(elements.getElementAtIndex(i)));
				ret = interpreter.interpret(tokens.subList(4, tokens.size()-1));
				if (ret != "") break;
			}
			
			// remove variable from heap, because it only exists in the context of the for loop
			interpreter.getExecuter().heap.remove(elementName);
			
			return ret;
		}
		
		return NULL;
	}

	/**
	 * Executes convert
	 *
	 * @param tokens tokens of the convert
	 */
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
	
	/**
	 * Executes call statement
	 * 
	 * @param token tokens of the call statement
	 */
	private void executeCall(List<String> tokens) {
		String function = tokens.get(0);
		Variable count = getVariableFromHeap(tokens.get(1));
		
		if (count == null) {
			count = new Variable(tokens.get(1));
		}
		
		if (count.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(tokens.get(1));
			return;
		}
		
		for (int i = 0; i < (int)count.getDoubleValue(); i++) {
			executeFunction(function);
		}
	}

	/**
	 * Executes if statement
	 *
	 * @param exp expression containing the statement
	 * @return result of the statement
	 */
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
			if (var1 instanceof VariableList && var2 instanceof VariableList) {
				return ((VariableList)var1).equals(((VariableList)var2));
			}
			return var1.equals(var2);
		}

		if (operator.contentEquals("unequals")) {
			if (var1 instanceof VariableList && var2 instanceof VariableList) {
				return !((VariableList)var1).equals(((VariableList)var2));
			}
			return !var1.equals(var2);
		}

		if (operator.contentEquals("greater")) {
			if (var1 instanceof VariableList && var2 instanceof VariableList) {
				return ((VariableList)var1).getSize() > ((VariableList)var2).getSize();
			}

			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("greater", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() > var2.getDoubleValue();
		}

		if (operator.contentEquals("smaller")) {
			if (var1 instanceof VariableList && var2 instanceof VariableList) {
				return ((VariableList)var1).getSize() < ((VariableList)var2).getSize();
			}

			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("smaller", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() < var2.getDoubleValue();
		}

		if (operator.contentEquals("greaterequals")) {
			if (var1 instanceof VariableList && var2 instanceof VariableList) {
				return ((VariableList)var1).getSize() >= ((VariableList)var2).getSize();
			}

			if (var1.getType() != VariableType.Number ||
					var2.getType() != VariableType.Number) {
				Error.printErrorOperatorNotApplicable("greater", tokens.get(1), tokens.get(3));
				return false;
			}

			return var1.getDoubleValue() >= var2.getDoubleValue();
		}

		if (operator.contentEquals("smallerequals")) {
			if (var1 instanceof VariableList && var2 instanceof VariableList) {
				return ((VariableList)var1).getSize() <= ((VariableList)var2).getSize();
			}

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

	/**
	 * Adds a function to the functionExecuter
	 *
	 * @param function function to add
	 */
	public void addFunction(Function function) {
		this.functionExecuter.addFunction(function);
	}
	
	/**
	 * Returns a copy of the local heap for a function call
	 * 
	 * @return a copy of the local heap with access operators
	 */
	private HashMap<String, Variable> getLocalHeapForFunction() {
		HashMap<String, Variable> functionHeap = new HashMap<>(this.heap.size());

		// copy local heap and add access operator to keys
		for (String key : this.heap.keySet()) {
			functionHeap.put('!' + key, this.heap.get(key));
		}
		
		return functionHeap;
	}

	/**
	 * Sets the local heap based on a function result
	 *
	 * This is used after a function call to copy any
	 * changes the function has made to the local heap
	 *
	 * @param functionHeap heap of the function
	 */
	private void setLocalHeapFromFunction(HashMap<String, Variable> functionHeap) {
		for (String key : functionHeap.keySet()) {
			if (key.charAt(0) == '!') {
				this.heap.put(key.substring(1), functionHeap.get(key));
			}
		}
	}

	/**
	 * Executes an expression
	 *
	 * @param exp expression to be executed
	 * @return result of expression
	 */
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
		case Call: executeCall(exp.getTokens()); break;
		default:
			break;
		}

		return ret;
	}
}