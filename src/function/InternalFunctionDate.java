package function;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import variable.Variable;
import helper.Error;

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
		this.functions.put("getFormatDate", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return getFormatDate(heap, params);
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
	
	/**
	 * Internal implementation of getFormatDate
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return new Variable containing the formatted date
	 */
	private Variable getFormatDate(Map<String, Variable> heap, String[] params) {
		Variable formatString = heap.get(params[0]);
		if (formatString == null) {
			formatString = new Variable(heap.get(params[0]));
		}
		
		Variable date = new Variable();
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		
		try {
			dateFormat.applyPattern(formatString.toString());
		}
		catch (IllegalArgumentException e) {
			Error.printErrorDateFormatIllegal(formatString.toString());
		}
		
		if (params.length == 2) {
			Variable unixSeconds = heap.get(params[1]);
			if (unixSeconds == null) {
				unixSeconds = new Variable(params[1]);
			}
			date.setStringValue(dateFormat.format(new Date((long)unixSeconds.getDoubleValue() * 1000L)));
		}
		else {
			date.setStringValue(dateFormat.format(new Date()));
		}
		
		return date;
	}
}