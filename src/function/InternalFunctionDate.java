package function;

import java.time.Instant;
import java.util.Map;
import variable.Variable;

/**
 * Implements the internal module date.os
 * 
 * @author Julius Paffrath
 *
 */
public class InternalFunctionDate extends InternalFunctionsBase {
	/**
	 * Sets up the functions of the module
	 */
	protected void setUpFunctions() {
		this.functions.put("getUnixTime", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return getUnixTime(heap, params);
			}
		});
	}
	
	/**
	 * Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z.
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return new Variable containing the unix time seconds
	 */
	private Variable getUnixTime(Map<String, Variable> heap, String[] params) {
		return new Variable((float)Instant.now().getEpochSecond());
	}
}