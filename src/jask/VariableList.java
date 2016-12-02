package jask;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a jask list variable
 *
 * @author Julius Paffrath
 *
 */
public class VariableList extends Variable {
	private List<Variable> values;

	public VariableList(String genericValue) {
		super();
		this.values = new ArrayList<Variable>();

		for (String curVal : genericValue.split(":")) {
			this.values.add(new Variable(curVal));
		}
	}

	public boolean addElement(String genericValue) {
		this.values.add(new Variable(genericValue));
		return true;
	}

	public boolean addElement(Variable toAdd) {
		this.values.add(new Variable(toAdd));
		return true;
	}

	public boolean setElement(int index, Variable toSet) {
		if (index >= this.values.size()) return false;
		this.values.set(index, new Variable(toSet));
		return true;
	}

	public boolean removeElement(int index) {
		if (index >= this.values.size()) {
			Error.printErrorNoValueAtIndex(index);
			return false;
		}
		this.values.remove(index);
		return true;
	}

	public int getSize() {
		return this.values.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Variable curVal = null;

		if (this.values.size() == 1) {
			curVal = this.values.get(0);
			if (curVal.getType() == VariableType.String) {
				return "\"" + curVal.toString() +"\":";
			}
			return curVal.toString() + ":";
		}

		for (int i = 0; i < this.values.size(); i++) {
			curVal = this.values.get(i);

			if (i == this.values.size()-1) {
				if (curVal.getType() == VariableType.String) {
					builder.append("\"" + curVal.toString() + "\"");
				}
				else {
					builder.append(curVal.toString());
				}
			}
			else {
				if (curVal.getType() == VariableType.String) {
					builder.append("\"" + curVal.toString() + "\":");
				}
				else {
					builder.append(curVal.toString() + ":");
				}
			}
		}

		return builder.toString();
	}

	public String getPrintString() {
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < this.values.size(); i++) {
			if (i == this.values.size()-1) {
				builder.append(this.values.get(i).toString());
			}
			else {
				builder.append(this.values.get(i).toString() + ", ");
			}
		}

		builder.append("]");

		return builder.toString();
	}

	public String getElementAtIndex(int i) {
		if (i > this.values.size()-1) {
			Error.printErrorNoValueAtIndex(i);
			return "";
		}

		Variable retVar = this.values.get(i);
		if (retVar.getType() == VariableType.String) return "\"" + retVar.toString() + "\"";

		return this.values.get(i).toString();
	}

	public String convertToString() {
		String ret = "\"";

		for (Variable var : this.values) {
			ret += var.toString();
		}

		return ret += "\"";
	}

	public boolean contains(Variable var) {
		for (Variable v : this.values) {
			if (v.equals(var)) return true;
		}

		return false;
	}
}