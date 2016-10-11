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
		values = new ArrayList<Variable>();

		for (String curVal : genericValue.split(":")) {
			values.add(new Variable(curVal));
		}
	}

	public VariableList(VariableList list) {
		super();
		values = new ArrayList<Variable>();
		values.add(list);
	}

	public boolean addElement(String genericValue) {
		values.add(new Variable(genericValue));
		return true;
	}

	public boolean addElement(Variable toAdd) {
		values.add(new Variable(toAdd));
		return true;
	}

	public boolean removeElement(int index) {
		if (index >= values.size()) {
			Error.printErrorNoValueAtIndex(index);
			return false;
		}
		values.remove(index);
		return true;
	}

	public int getSize() {
		return values.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < values.size(); i++) {
			if (i == values.size()-1) {
				builder.append(values.get(i).toString());
			}
			else {
				builder.append(values.get(i).toString() + ":");
			}
		}

		return builder.toString();
	}

	public String getPrintString() {
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < values.size(); i++) {
			if (i == values.size()-1) {
				builder.append(values.get(i).toString());
			}
			else {
				builder.append(values.get(i).toString() + ", ");
			}
		}

		builder.append("]");

		return builder.toString();
	}

	public String getElementAtIndex(int i) {
		if (i > values.size()-1) {
			Error.printErrorNoValueAtIndex(i);
			return "";
		}

		return values.get(i).toString();
	}
}