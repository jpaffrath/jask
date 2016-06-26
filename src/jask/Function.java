package jask;

import java.util.HashMap;
import java.util.List;

public class Function {
	private String name;
	private String params[];
	private HashMap<String, Variable> heap;
	private List<String> tokens;

	public Function(String initialString, List<String> tokens) {
		this.name = initialString.substring(0, initialString.indexOf('('));
		this.tokens = tokens;

		String param = initialString.substring(initialString.indexOf('(')+1, initialString.indexOf(')'));
		this.params = param.split(":");
	}

	public void setHeap(List<Variable> _heap) {
		heap = new HashMap<>();

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
