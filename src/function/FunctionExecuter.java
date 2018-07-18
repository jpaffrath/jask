package function;

import static jask.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.Error;
import jask.Executer;
import jask.Interpreter;
import variable.Variable;

/**
 * Executer for functions
 *
 * @author Julius Paffrath
 *
 */
public class FunctionExecuter {
	private HashMap<String, Function> functions;

	public FunctionExecuter() {
		this.functions = new HashMap<>();
	}

	/**
	 * Executes a function
	 *
	 * @param name name of the function to be executed
	 * @param heap local heap for the function
	 * @return result of the function
	 */
	public String executeFunction(String name, List<Variable> heap, HashMap<String, Variable> localHeap, List<Executer> modules) {
		if (!this.hasFunction(name)) {
			Error.printErrorFunctionNotDefined(name);
			return NULL;
		}

		Function function = this.functions.get(name);
		function.setHeap(localHeap);
		function.setParameterHeap(heap);
		Interpreter interpreter = new Interpreter(function, this, new ArrayList<Executer>(modules));
		return interpreter.interpret(function.getTokens());
	}

	public void destroyFunctionHeap(String name) {
		if (!this.hasFunction(name)) {
			Error.printErrorFunctionNotDefined(name);
		}

		Function function = this.functions.get(name);
		function.destroyHeap();
	}

	public void addFunction(Function function) {
		this.functions.put(function.getName(), function);
	}

	public Function getFunction(String name) {
		if (this.hasFunction(name)) {
			return this.functions.get(name);
		}

		return null;
	}

	public boolean hasFunction(String name) {
		return this.functions.containsKey(name);
	}
}
