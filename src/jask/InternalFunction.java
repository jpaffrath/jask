package jask;

import java.util.HashMap;

/**
 * Interface for executing internal functions
 *
 * @author Julius Paffrath
 *
 */
public interface InternalFunction {
	String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params);
}
