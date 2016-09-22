package jask;

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
		if (Pattern.matches("^\\-?[0-9]*\\d{0,2}(\\.\\d*)?$", value)) return true;
		return false;
	}

	/**
	 * Checks if a given string is a valid jask string
	 *
	 * @param value string to be checked
	 * @return true if the given string represents a valid jask string
	 */
	public static boolean isString(String value) {
		if (Pattern.matches("^[\"]+([^\"]+[\"])?$", value)) return true;
		return false;
	}

	public Variable() {
		this.type = VariableType.NoType;
	}

	/**
	 * Initializes a new variable based on a generic string
	 *
	 * @param genericValue
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
		else if (genericValue.contentEquals("TRUE")) {
			this.boolValue = true;
			this.type = VariableType.Bool;
		}
		else if (genericValue.contentEquals("FALSE")){
			this.boolValue = false;
			this.type = VariableType.Bool;
		}
	}

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

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		this.type = VariableType.String;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
		this.type = VariableType.Number;
	}

	public boolean getBoolValue() {
		return boolValue;
	}

	public void setBoolValue(boolean boolValue) {
		this.boolValue = boolValue;
		this.type = VariableType.Bool;
	}

	public VariableType getType() {
		return type;
	}

	protected void setType(VariableType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		if (type == VariableType.String) return ("\"" + stringValue + "\"");
		if (type == VariableType.Number) return String.valueOf(doubleValue);
		if (type == VariableType.Bool)   return this.boolValue ? "TRUE" : "FALSE";

		return  "NULL";
	}

	public boolean equals(Variable var) {
		if (this.type != var.type) return false;

		if (this.type == VariableType.NoType) return true;

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