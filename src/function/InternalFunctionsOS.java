package function;

import java.util.Map;
import variable.Variable;

/**
 * Implements the internal module jask.os
 * 
 * @author Julius Paffrath
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
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				String osName = System.getProperty("os.name");
				return new Variable('"' + osName + '"');
			}
		});
		this.functions.put("getOSArch", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				String osArch = System.getProperty("os.arch");
				return new Variable('"' + osArch + '"');
			}
		});
		this.functions.put("getOSVersion", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				String osVersion = System.getProperty("os.version");
				return new Variable('"' + osVersion + '"');
			}
		});
	}
}