package jask;

import java.util.HashMap;
import java.util.List;

public class FunctionExecuter {
	private HashMap<String, Function> functions;

	public FunctionExecuter() {
		functions = new HashMap<>();
	}

	public String executeFunction(String name, List<Variable> heap) {
		Function function = functions.get(name);
		function.setHeap(heap);
		Interpreter interpreter = new Interpreter(function);
		return interpreter.interpret(function.getTokens());
	}

	public void addFunction(Function function) {
		functions.put(function.getName(), function);
	}
}
