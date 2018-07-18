package function;

import java.util.HashMap;
import java.util.List;

import variable.Variable;
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
	private HashMap<String, Variable> heap;
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
		if (parameterHeap.isEmpty()) return;

		for (int i = 0; i < params.length; i++) {
			Variable varOld = parameterHeap.get(i);
			Variable varNew = null;

			if (varOld instanceof VariableList) {
				varNew = new VariableList(varOld.toString());
			}
			else {
				varNew = new Variable(varOld);
			}
			this.heap.put(params[i], varNew);
		}
	}

	/**
	 * Sets the heap of the function
	 *
	 * @param heap heap for the function
	 */
	public void setHeap(HashMap<String, Variable> heap) {
		this.heap = heap;
	}

	/**
	 * Returns the heap
	 *
	 * @return local heap
	 */
	public HashMap<String, Variable> getHeap() {
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