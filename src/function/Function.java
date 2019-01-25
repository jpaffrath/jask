package function;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import variable.Variable;
import variable.VariableFunction;
import variable.VariableList;

/**
 * Represents a jask function
 *
 * @author Julius Paffrath
 *
 */
public class Function {
	private String name;
	private String params[];
	private Map<String, Variable> heap;
	private List<String> tokens;

	/**
	 * Creates a new function
	 *
	 * @param initialString e.g. print(parameter)
	 * @param tokens tokens of the function
	 */
	public Function(String initialString, List<String> tokens) {
		this.name = initialString.substring(0, initialString.indexOf('('));
		this.tokens = tokens;
		this.heap = new HashMap<>();

		String param = initialString.substring(initialString.indexOf('(')+1, initialString.indexOf(')'));
		this.params = param.split(":");
	}

	/**
	 * Sets the parameter heap of the function
	 *
	 * @param parameterHeap list of variables
	 */
	public void setParameterHeap(List<Variable> parameterHeap) {
		if (parameterHeap.isEmpty()) {
			return;
		}

		for (int i = 0; i < params.length; i++) {
			Variable varOld = parameterHeap.get(i);
			Variable varNew = null;

			// if variable is a list, add a new list variable
			if (varOld instanceof VariableList) {
				varNew = new VariableList(varOld.toString());
			}
			
			// if the variable is a function, add a new function variable with the corresponding parameter name
			else if (varOld instanceof VariableFunction) {
				Function func = ((VariableFunction)varOld).getFunction();
				func.setName(params[i]);
				
				varNew = new VariableFunction(func);
			}
			
			// copy old variable to a new variable
			else {
				varNew = new Variable(varOld);
			}
			
			// put the new variable with the corresponding parameter name on the heap
			this.heap.put(params[i], varNew);
		}
	}

	/**
	 * Sets the heap of the function
	 *
	 * @param heap heap for the function
	 */
	public void setHeap(Map<String, Variable> heap) {
		this.heap = heap;
	}

	/**
	 * Returns the heap
	 *
	 * @return local heap
	 */
	public Map<String, Variable> getHeap() {
		return this.heap;
	}

	/**
	 * Returns the name
	 *
	 * @return local function name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of the function
	 * 
	 * @param name new name of the function
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the parameter list of the function
	 * 
	 * @return an array containing the parameters of the function
	 */
	public String[] getParameter() {
		return this.params;
	}

	/**
	 * Returns the tokens
	 *
	 * @return tokens of the function
	 */
	public List<String> getTokens() {
		return this.tokens;
	}

	/**
	 * Destroys the heap of the function
	 *
	 * Must be called after the function has been executed!
	 */
	public void destroyHeap() {
		this.heap.clear();
	}
}