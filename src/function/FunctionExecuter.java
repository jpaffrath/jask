package function;

import static jask.Constants.*;

import java.util.ArrayList;
import java.util.Map;
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
	private Map<String, Function> functions;

	/**
	 * Default constructor
	 */
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
	public String executeFunction(String name, List<Variable> heap, Map<String, Variable> localHeap, List<Executer> modules, InternalFunctions internalFunctions) {
		if (!this.hasFunction(name)) {
			Error.printErrorFunctionNotDefined(name);
			return NULL;
		}
		
		// if the object isn't copied, an internal module added inside the function would live after the function execution
		InternalFunctions internalFunctionsCopy = new InternalFunctions(internalFunctions);

		Function function = this.functions.get(name);
		function.setHeap(localHeap);
		function.setParameterHeap(heap);
		Interpreter interpreter = new Interpreter(function, this, new ArrayList<Executer>(modules), internalFunctionsCopy);
		return interpreter.interpret(function.getTokens());
	}

	/**
	 * Destroys the function heap from the function with the given name
	 * 
	 * @param name name of the function
	 */
	public void destroyFunctionHeap(String name) {
		if (!this.hasFunction(name)) {
			Error.printErrorFunctionNotDefined(name);
		}

		Function function = this.functions.get(name);
		function.destroyHeap();
	}

	/**
	 * Adds a new function to the executers heap
	 * 
	 * @param function the new function
	 */
	public void addFunction(Function function) {
		this.functions.put(function.getName(), function);
	}
	
	/**
	 * Removes a function from the executers heap
	 * 
	 * @param function the function to be removed
	 */
	public void removeFunction(Function function) {
		if (this.hasFunction(function.getName())) {
			this.functions.remove(function.getName());
		}
	}

	/**
	 * Returns the function with a given name
	 * 
	 * @param name name of the function
	 * @return the function with the given name or null
	 */
	public Function getFunction(String name) {
		if (this.hasFunction(name)) {
			return this.functions.get(name);
		}

		return null;
	}

	/**
	 * Returns true if the executer holds a function with a given name
	 * 
	 * @param name name of the function
	 * @return true if the executer holds a function with the given name
	 */
	public boolean hasFunction(String name) {
		if (name.endsWith("()")) {
			return this.functions.containsKey(name.substring(0, name.length()-2));
		}
		return this.functions.containsKey(name);
	}
}
