package variable;

import function.Function;

/**
 * Represents a jask variable holding a function
 *
 * @author Julius Paffrath
 *
 */
public class VariableFunction extends Variable {
	private Function function;
	
	/**
	 * Default constructor
	 */
	public VariableFunction(Function function) {
		this.function = function;
	}
	
	/**
	 * Returns the function
	 * 
	 * @return the function of the variable
	 */
	public Function getFunction() {
		return this.function;
	}
	
	/**
	 * Sets the function
	 * 
	 * @param function the new function to be set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}
	
	/**
	 * Sets the name of the function
	 * 
	 * @param name the new name of the function
	 */
	public void setFunctionName(String name) {
		this.function.setName(name);
	}
	
	/**
	 * Returns the string representation of the variable
	 */
	@Override
	public String toString() {
		return "Function variable '" + this.function.getName() + "'";
	}
}