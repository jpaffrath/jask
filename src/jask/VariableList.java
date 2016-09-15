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
	private List<String> values;

	public VariableList(String genericValue) {
		super();

		String[] e = genericValue.split(":");
		values = new ArrayList<String>();

		boolean isString = isString(e[0]);
		values.add(e[0]);

		for (int i = 1; i < e.length; i++) {
			if (isString) {
				if (!isString(e[i])) {
					Error.printErrorListMultipleTypes();
					return;
				}
			}
			else {
				if (!isNumber(e[i])) {
					Error.printErrorListMultipleTypes();
					return;
				}
			}
			values.add(e[i]);
		}

		if (isString) setType(VariableType.String);
		else setType(VariableType.Number);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < values.size(); i++) {
			if (i == values.size()-1) {
				builder.append(values.get(i));
			}
			else {
				builder.append(values.get(i) + ":");
			}
		}

		return builder.toString();
	}

	public String getPrintString() {
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < values.size(); i++) {
			if (i == values.size()-1) {
				builder.append(values.get(i));
			}
			else {
				builder.append(values.get(i) + ", ");
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

		return values.get(i);
	}
}