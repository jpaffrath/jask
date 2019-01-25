package variable;

import static jask.Constants.*;

import java.util.regex.Pattern;

/**
 * Represents a jask variable
 *
 * @author Julius Paffrath
 *
 */
public class Variable {
	private String stringValue;
	private double doubleValue;
	private boolean boolValue;
	private VariableType type;

	/**
	 * Checks if a given string is a valid jask number, e.g. 7, 242.42
	 *
	 * @param value string to be checked
	 * @return true if the given string represents a valid jask number
	 */
	public static boolean isNumber(String value) {
		try {
			Double.parseDouble(value);
		}
		catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if a given string is a valid jask string
	 *
	 * @param value string to be checked
	 * @return true if the given string represents a valid jask string
	 */
	public static boolean isString(String value) {
		return Pattern.matches("^[\"]+([^\"]+[\"])?$", value);
	}

	/**
	 * Checks if a given string is a valid boolean value
	 *
	 * @param value string to be checked
	 * @return true if the given string is a valid boolean value
	 */
	public static boolean isBoolean(String value) {
		return value.contentEquals(TRUE) || value.contentEquals(FALSE);
	}

	/**
	 * Default constructor
	 */
	public Variable() {
		this.type = VariableType.NoType;
	}

	/**
	 * Initializes a new variable based on a generic string
	 *
	 * @param genericValue generic string value
	 */
	public Variable(String genericValue) {
		this.type = VariableType.NoType;

		// matches integers and doubles
		if (isNumber(genericValue)) {
			this.doubleValue = Double.parseDouble(genericValue);
			this.type = VariableType.Number;
		}
		// matches strings
		else if (isString(genericValue)) {
			this.stringValue = genericValue.substring(1, genericValue.length()-1);
			this.type = VariableType.String;
		}
		// matches boolean values
		else if (genericValue.contentEquals(TRUE)) {
			this.boolValue = true;
			this.type = VariableType.Bool;
		}
		else if (genericValue.contentEquals(FALSE)){
			this.boolValue = false;
			this.type = VariableType.Bool;
		}
	}

	/**
	 * Initializes a new variable based on a given variable
	 *
	 * @param var variable to initialize the new object
	 */
	public Variable(Variable var) {
		this.type = var.type;

		if (type == VariableType.Number) {
			this.doubleValue = var.doubleValue;
		}
		else if (type == VariableType.String) {
			this.stringValue = var.stringValue;
		}
		else if (type == VariableType.Bool) {
			this.boolValue = var.boolValue;
		}
	}

	/**
	 * Initializes a new variable based on a given double value
	 *
	 * @param value double to initialize the new object
	 */
	public Variable(double value) {
		this.type = VariableType.Number;
		this.doubleValue = value;
	}

	/**
	 * Returns the string value
	 * @return the current variables string value
	 */
	public String getStringValue() {
		return this.stringValue;
	}

	/**
	 * Sets the variable to a string variable
	 *
	 * Using this method will change the variables type to string
	 *
	 * @param stringValue new string value
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		this.type = VariableType.String;
	}

	/**
	 * Returns the double value
	 *
	 * @return the current variables double value
	 */
	public double getDoubleValue() {
		return this.doubleValue;
	}

	/**
	 * Sets the variable to a number variable
	 *
	 * Using this method will change the variables type to number
	 *
	 * @param doubleValue new double value
	 */
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
		this.type = VariableType.Number;
	}

	/**
	 * Returns the boolean value
	 *
	 * @return the current variables boolean value
	 */
	public boolean getBoolValue() {
		return this.boolValue;
	}

	/**
	 * Sets the variable to a boolean variable
	 *
	 * Using this method will change the variables type to boolean
	 *
	 * @param boolValue new boolean value
	 */
	public void setBoolValue(boolean boolValue) {
		this.boolValue = boolValue;
		this.type = VariableType.Bool;
	}

	/**
	 * Returns the type
	 *
	 * @return the current variables type
	 */
	public VariableType getType() {
		return this.type;
	}

	/**
	 * Sets the variables type
	 *
	 * @param type new type
	 */
	public void setType(VariableType type) {
		this.type = type;
	}

	/**
	 * Returns a string representation based on the variables content
	 */
	@Override
	public String toString() {
		if (this.type == VariableType.String) {
			return this.stringValue;
		}
		
		if (this.type == VariableType.Number) {
			// if double has no decimal part...
			if (this.doubleValue % 1 == 0) {
				// ...trim values like 2.0 to 2
				String temp = String.valueOf(this.doubleValue);
				return temp.substring(0, temp.indexOf('.'));
			}
			return String.valueOf(this.doubleValue);
		}
		if (this.type == VariableType.Bool) {
			return this.boolValue ? TRUE : FALSE;
		}

		return NULL;
	}

	/**
	 * Compares the current object with a given variable
	 *
	 * @param var variable to compare
	 * @return true if the given variable equals the current object. False otherwise
	 */
	public boolean equals(Variable var) {
		if (this.type != var.type) {
			return false;
		}

		if (this.type == VariableType.NoType) {
			return true;
		}

		if (this.type == VariableType.Number) {
			return this.doubleValue == var.doubleValue;
		}

		if (this.type == VariableType.String) {
			return this.stringValue.contentEquals(var.stringValue);
		}

		if (this.type == VariableType.Bool) {
			return this.boolValue == var.boolValue;
		}

		return false;
	}
}