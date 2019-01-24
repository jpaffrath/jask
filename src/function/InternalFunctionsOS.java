package function;

import java.util.HashMap;
import variable.Variable;

/**
 * Implements the internal module jask.os
 * 
 * @author jpaffrath
 *
 */
public class InternalFunctionsOS extends InternalFunctionsBase {
	
	/**
	 * General constructor
	 */
	public InternalFunctionsOS() {
		super();
	}
	
	/**
	 * Sets up the functions of the module
	 */
	protected void setUpFunctions() {
		this.functions.put("getOSName", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				String osName = System.getProperty("os.name");
				return new Variable('"' + osName + '"');
			}
		});
	}
}