package jask;

import java.util.HashMap;
import java.util.List;

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

		String param = initialString.substring(initialString.indexOf('(')+1, initialString.indexOf(')'));
		this.params = param.split(":");
	}

	/**
	 * Sets the heap of the function
	 *
	 * @param _heap list of variables
	 */
	public void setHeap(List<Variable> _heap) {
		heap = new HashMap<>();
		if (_heap.isEmpty()) return;

		for (int i = 0; i < params.length; i++) {
			heap.put(params[i], _heap.get(i));
		}
	}

	public HashMap<String, Variable> getHeap() {
		return this.heap;
	}

	public String getName() {
		return this.name;
	}

	public List<String> getTokens() {
		return this.tokens;
	}
}