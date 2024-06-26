package function;

import static jask.Constants.FALSE;
import static jask.Constants.TRUE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import variable.Variable;
import variable.VariableType;
import helper.Error;

/**
 * Implements the internal module jask.system
 * 
 * @author Julius Paffrath
 *
 */
public class InternalFunctionsSystem extends InternalFunctionsBase {
	/**
	 * Sets up the functions of the module
	 */
	protected void setUpFunctions() {
		this.functions.put("sleep", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				if (heap.size() != 1) {
					Error.printErrorFunctionExpectingParameters(functionName, "milliseconds[Number]");
				}
				return sleep(heap, params);
			}
		});
		this.functions.put("exec", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return exec(heap, params);
			}
		});
	}
	
	/**
	 * Internal implementation of sleep
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return True if execution was successful
	 */
	private Variable sleep(Map<String, Variable> heap, String[] params) {
		Variable sleepTime = heap.get(params[0]);
		
		if (sleepTime.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[0]);
			return new Variable(FALSE);
		}
		
		try {
			Thread.sleep((long)sleepTime.getDoubleValue());
		}
		catch (InterruptedException e) {
			Error.terminateInterpret("Execution of sleep() failed!");
			e.printStackTrace();
		}
		
		return new Variable(TRUE);
	}

	/**
	 * Internal implementation of exec
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return new Variable containing the output of the process
	 */
	private Variable exec(Map<String, Variable> heap, String[] params) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		String exec = heap.get(params[0]).toString();
		
		// check if call has additional parameters
		if (params.length > 1) {
			// add additional parameter to process call
			for (int i = 1; i < params.length; i++) {
				exec += " " + heap.get(params[i]).toString();
			}
		}
		
		try {
			// execute process and wait for finish
			process = runtime.exec(exec);
			process.waitFor();
		}
		catch (IOException ioExc) {
			Error.printErrorProcessIOException(exec);
		}
		catch (InterruptedException interruptExc) {
			Error.printErrorProcessInterrupted(exec);
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuffer outputBuffer = new StringBuffer();
		
		try {
			// collect process output
			String output = "";
			while ((output = reader.readLine()) != null) {
				outputBuffer.append(output);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// build new variable with process output
		Variable out = new Variable();
		out.setStringValue(outputBuffer.toString());
		
		return out;
	}
}