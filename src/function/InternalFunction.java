package function;

import java.util.HashMap;

import variable.Variable;

/**
 * Interface for executing internal functions
 *
 * @author Julius Paffrath
 *
 */
public interface InternalFunction {
	Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params);
}