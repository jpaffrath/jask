package function;

import java.util.Map;
import variable.Variable;

/**
 * Interface for executing internal functions
 *
 * @author Julius Paffrath
 *
 */
public interface InternalFunction {
	Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params);
}