package jask;

import java.util.regex.Pattern;

public class Variable {
	private String stringValue;
	private double doubleValue;
	private VariableType type;

	public Variable(String genericValue) {
		// matches integers and doubles
		if (Pattern.matches("([0-9]*)\\.([0-9]*)", genericValue) ||
				Pattern.matches("^\\d+$", genericValue)) {
			this.doubleValue = Double.parseDouble(genericValue);
			this.type = VariableType.Number;
		}
		// matches strings
		else if (Pattern.matches("^[\"]+([^\"]+[\"])?$", genericValue)) {
			this.stringValue = genericValue;
			this.type = VariableType.String;
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

	public VariableType getType() {
		return type;
	}

	@Override
	public String toString() {
		if (type == VariableType.String) return stringValue;
		if (type == VariableType.Number) return String.valueOf(doubleValue);

		return "";
	}

	public boolean equals(Variable var) {
		if (this.type != var.type) return false;

		if (this.type == VariableType.Number) {
			return this.doubleValue == var.doubleValue;
		}

		if (this.type == VariableType.String) {
			return this.stringValue.contentEquals(var.stringValue);
		}

		return false;
	}
}
