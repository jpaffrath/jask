package jask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Implements internal functions
 *
 * @author Julius Paffrath
 *
 */
public class InternalFunctions {
	private HashMap<String, Variable> heap;
	String functionName;
	List<String> params;
	String param;

	public InternalFunctions(HashMap<String, Variable> heap, String functionName, String param) {
		this.heap = heap;
		this.functionName = functionName;
		this.param = param;
		this.params = Helpers.splitParams(param);
	}

	public static boolean isInternalFunction(String functionName) {
		List<String> internals = new ArrayList<String>();
		internals.add("print");
		internals.add("printLine");
		internals.add("read");
		internals.add("listGet");
		internals.add("listSize");
		internals.add("listAdd");
		internals.add("listRemove");
		internals.add("isString");
		internals.add("isNumber");
		internals.add("isBool");
		internals.add("isList");
		return internals.contains(functionName);
	}

	public static boolean isInternalListFunction(String functionName) {
		List<String> internals = new ArrayList<String>();
		internals.add("list");
		internals.add("listFromString");
		return internals.contains(functionName);
	}

	public String executeFunction() {
		switch (functionName) {
		case "print":          return print(false);
		case "printLine":      return print(true);
		case "read":           return read();
		case "list":           return list();
		case "listGet":        return listGet();
		case "listSize":       return listSize();
		case "listAdd":        return listAdd();
		case "listRemove":     return listRemove();
		case "listFromString": return listFromString();
		case "isString":       return isString();
		case "isNumber":       return isNumber();
		case "isBool":         return isBool();
		case "isList":         return isList();
		}

		return "";
	}

	private String print(boolean newLine) {
		String output = "";

		for (String out : params) {
			if (Variable.isString(out)) {
				output += out.substring(1, out.length()-1);
				continue;
			}

			if (Variable.isNumber(out)) {
				output += out;
				continue;
			}

			Variable var = heap.get(out);
			if (var == null) {
				Error.printErrorVariableNotDefined(out);
				return "FALSE";
			}
			else {
				if (var instanceof VariableList) {
					output += (((VariableList)var).getPrintString());
				}
				else {
					output += (var.toString());
				}
			}
		}

		if (newLine) {
			System.out.println(output);
		}
		else {
			System.out.print(output);
		}

		return "TRUE";
	}

	private String read() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		return '"' + scanner.nextLine() + '"';
	}

	private String list() {
		if (!param.contains(":")) {
			return param + ":";
		}

		return param;
	}

	private String listGet() {
		Variable var = heap.get(params.get(0));
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params.get(0));
			return "NULL";
		}

		Variable index = heap.get(params.get(1));
		if (index == null) {
			if (!Variable.isNumber(params.get(1))) {
				Error.printErrorValueNotApplicable(params.get(1));
				return "NULL";
			}

			return ((VariableList)var).getElementAtIndex(Integer.parseInt(params.get(1)));
		}

		if (index.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params.get(1));
			return "NULL";
		}

		return ((VariableList)var).getElementAtIndex((int)index.getDoubleValue());
	}

	private String listSize() {
		Variable var = heap.get(params.get(0));
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params.get(0));
			return "NULL";
		}

		return Integer.toString(((VariableList)var).getSize());
	}

	private String listAdd() {
		Variable var = heap.get(params.get(0));
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params.get(0));
			return "NULL";
		}

		Variable toAdd = heap.get(params.get(1));
		if (toAdd == null) {
			if (((VariableList)var).addElement(params.get(1))) {
				return "TRUE";
			}

			return "FALSE";
		}
		else {
			if (((VariableList)var).addElement(toAdd)) {
				return "TRUE";
			}

			return "FALSE";
		}
	}

	private String listRemove() {
		Variable var = heap.get(params.get(0));
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params.get(0));
			return "NULL";
		}

		Variable index = heap.get(params.get(1));
		if (index == null) {
			if (Variable.isNumber(params.get(1))) {
				((VariableList)var).removeElement(Integer.parseInt(params.get(1)));
				return "TRUE";
			}

			return "FALSE";
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params.get(1));
				return "FALSE";
			}

			((VariableList)var).removeElement((int)index.getDoubleValue());
			return "TRUE";
		}
	}

	private String listFromString() {
		String strVal = "";
		String retVal = "";
		Variable var = heap.get(params.get(0));

		if (var == null) {
			if (Variable.isString(params.get(0))) {
				strVal = params.get(0);
			}
			else {
				Error.printErrorVariableIsNotAString(params.get(0));
				return "NULL";
			}
		}
		else {
			if (var.getType() == VariableType.String) {
				strVal = var.getStringValue();
			}
			else {
				Error.printErrorVariableIsNotAString(params.get(0));
				return "NULL";
			}
		}

		for (int i = 0; i < strVal.length(); i++) {
			if (i == strVal.length()-1) {
				retVal += "\"" + strVal.charAt(i) + "\"";
			}
			else {
				retVal += "\"" + strVal.charAt(i) + "\"" + ":";
			}
		}

		return retVal;
	}

	private String isString() {
		Variable var = heap.get(params.get(0));
		if (var == null) {
			if (Variable.isString(params.get(0))) return "TRUE";
			return "FALSE";
		}

		if (var.getType() == VariableType.String) return "TRUE";
		return "FALSE";
	}

	private String isNumber() {
		Variable var = heap.get(params.get(0));
		if (var == null) {
			if (Variable.isNumber(params.get(0))) return "TRUE";
			return "FALSE";
		}

		if (var.getType() == VariableType.Number) return "TRUE";
		return "FALSE";
	}

	private String isBool() {
		Variable var = heap.get(params.get(0));
		if (var == null) {
			String val = params.get(0);
			if (val.contentEquals("TRUE") || val.contentEquals("FALSE")) return "TRUE";
			return "FALSE";
		}

		if (var.getType() == VariableType.Bool) return "TRUE";
		return "FALSE";
	}

	private String isList() {
		if (heap.get(params.get(0)) instanceof VariableList) return "TRUE";
		return "FALSE";
	}
}