package function;

import java.util.Map;
import java.util.HashMap;

/**
 * Base class for implementations of internal modules
 *
 * @author Julius Paffrath
 *
 */
public class InternalFunctionsBase {
	protected Map<String, InternalFunction> functions;
	
	/**
	 * General constructor
	 */
	public InternalFunctionsBase() {
		this.functions = new HashMap<String, InternalFunction>();
		this.setUpFunctions();
	}
	
	/**
	 * Checks if a given function name is an internal function
	 *
	 * @param functionName name of the function
	 * @return true if the given function name is an internal function
	 */
	public boolean isInternalFunction(String functionName) {
		return this.functions.containsKey(functionName);
	}
	
	/**
	 * Returns the functions of the internal module
	 * 
	 * @return the functions of the module
	 */
	public Map<String, InternalFunction> getFunctions() {
		return this.functions;
	}
	
	/**
	 * Adds internal functions to functions heap
	 * 
	 * Inheriting classes must overwrite this method
	 */
	protected void setUpFunctions() { }
}