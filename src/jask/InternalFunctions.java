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
	private String params[];
	String param;

	public InternalFunctions(List<Variable> _heap, String functionName, String param) {
		this.functionName = functionName;
		this.param = param;

		List<String> temp = Helpers.splitParams(param);
		this.params = new String[temp.size()];
		temp.toArray(params);

		this.setHeap(_heap);
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
		internals.add("listSet");
		internals.add("listToString");
		internals.add("isString");
		internals.add("isNumber");
		internals.add("isBool");
		internals.add("isList");
		internals.add("exit");
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
		case "listSet":        return listSet();
		case "listFromString": return listFromString();
		case "listToString":   return listToString();
		case "isString":       return isString();
		case "isNumber":       return isNumber();
		case "isBool":         return isBool();
		case "isList":         return isList();
		case "exit":           return exit();
		}

		return "";
	}

	public void setHeap(List<Variable> _heap) {
		heap = new HashMap<>();
		if (_heap.isEmpty()) return;

		for (int i = 0; i < params.length; i++) {
			heap.put(params[i], _heap.get(i));
		}
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
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		scanner.close();
		return '"' + line + '"';
	}

	private String list() {
		if (!param.contains(":")) {
			return param + ":";
		}

		return param;
	}

	private String listGet() {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return "NULL";
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (!Variable.isNumber(params[1])) {
				Error.printErrorValueNotApplicable(params[1]);
				return "NULL";
			}

			return ((VariableList)var).getElementAtIndex(Integer.parseInt(params[1]));
		}

		if (index.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return "NULL";
		}

		return ((VariableList)var).getElementAtIndex((int)index.getDoubleValue());
	}

	private String listSize() {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return "NULL";
		}

		return Integer.toString(((VariableList)var).getSize());
	}

	private String listAdd() {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return "FALSE";
		}

		Variable toAdd = heap.get(params[1]);
		if (toAdd == null) {
			if (((VariableList)var).addElement(params[1])) {
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
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return "FALSE";
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (Variable.isNumber(params[1])) {
				((VariableList)var).removeElement(Integer.parseInt(params[1]));
				return "TRUE";
			}

			return "FALSE";
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return "FALSE";
			}

			((VariableList)var).removeElement((int)index.getDoubleValue());
			return "TRUE";
		}
	}

	private String listSet() {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return "FALSE";
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (Variable.isNumber(params[1])) {
				((VariableList)var).removeElement(Integer.parseInt(params[1]));
				return "TRUE";
			}

			return "FALSE";
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return "FALSE";
			}
		}

		Variable toSet = heap.get(params[2]);
		if (toSet == null) {
			toSet = new Variable(params[2]);
			if (toSet.getType() == VariableType.NoType) return "FALSE";
		}

		if (((VariableList)var).setElement((int)index.getDoubleValue(), toSet)) {
			return "TRUE";
		}

		return "FALSE";
	}

	private String listFromString() {
		String strVal = "";
		String retVal = "";
		Variable var = heap.get(params[0]);

		if (var == null) {
			if (Variable.isString(params[0])) {
				strVal = params[0];
			}
			else {
				Error.printErrorVariableIsNotAString(params[0]);
				return "NULL";
			}
		}
		else {
			if (var.getType() == VariableType.String) {
				strVal = var.getStringValue();
			}
			else {
				Error.printErrorVariableIsNotAString(params[0]);
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

	private String listToString() {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return "NULL";
		}

		return ((VariableList)var).convertToString();
	}

	private String isString() {
		Variable var = heap.get(params[0]);
		if (var == null) {
			if (Variable.isString(params[0])) return "TRUE";
			return "FALSE";
		}

		if (var.getType() == VariableType.String) return "TRUE";
		return "FALSE";
	}

	private String isNumber() {
		Variable var = heap.get(params[0]);
		if (var == null) {
			if (Variable.isNumber(params[0])) return "TRUE";
			return "FALSE";
		}

		if (var.getType() == VariableType.Number) return "TRUE";
		return "FALSE";
	}

	private String isBool() {
		Variable var = heap.get(params[0]);
		if (var == null) {
			String val = params[0];
			if (val.contentEquals("TRUE") || val.contentEquals("FALSE")) return "TRUE";
			return "FALSE";
		}

		if (var.getType() == VariableType.Bool) return "TRUE";
		return "FALSE";
	}

	private String isList() {
		if (heap.get(params[0]) instanceof VariableList) return "TRUE";
		return "FALSE";
	}

	private String exit() {
		int code = -1;

		Variable var = heap.get(params[0]);
		if (var == null) {
			code = Integer.parseInt(params[0]);
		}

		System.exit(code);
		return "NULL";
	}
}