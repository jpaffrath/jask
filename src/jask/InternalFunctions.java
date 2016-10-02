package jask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Implemens internal functions
 *
 * @author Julius Paffrath
 *
 */
public class InternalFunctions {
	private HashMap<String, Variable> heap;
	String functionName;
	String param;
	List<String> params;

	public InternalFunctions(HashMap<String, Variable> heap, String token) {
		this.heap = heap;

		functionName = token.substring(0, token.indexOf('('));
		param = token.substring(token.indexOf('(')+1, token.indexOf(')'));
		params = Helpers.splitParams(param);
	}

	public static boolean isInternalFunction(String functionName) {
		List<String> internals = new ArrayList<String>();
		internals.add("print");
		internals.add("printLine");
		internals.add("read");
		internals.add("list");
		internals.add("listGet");
		internals.add("listSize");
		internals.add("listAdd");
		return internals.contains(functionName);
	}

	public String executeFunction() {
		switch (functionName) {
		case "print":     return print();
		case "printLine": return printLine();
		case "read":      return read();
		case "list":      return list();
		case "listGet":   return listGet();
		case "listSize":  return listSize();
		case "listAdd":   return listAdd();
		}

		return "";
	}

	private String print() {
		String temp = params.get(0);

		if (Variable.isString(temp) || Variable.isNumber(temp)) {
			System.out.print(temp);
			return "";
		}

		Variable var = heap.get(temp);
		if (var == null) {
			Error.printErrorVariableNotDefined(temp);
		}
		else {
			if (var instanceof VariableList) {
				System.out.print(((VariableList)var).getPrintString());
			}
			else {
				System.out.print(var.toString());
			}
		}

		return "";
	}

	private String printLine() {
		String temp = params.get(0);

		if (Variable.isString(temp) || Variable.isNumber(temp)) {
			System.out.println(temp);
			return "";
		}

		Variable var = heap.get(temp);
		if (var == null) {
			Error.printErrorVariableNotDefined(param);
		}
		else {
			if (var instanceof VariableList) {
				System.out.println(((VariableList)var).getPrintString());
			}
			else {
				System.out.println(var.toString());
			}
		}

		return "";
	}

	private String read() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		return '"' + scanner.nextLine() + '"';
	}

	private String list() {
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
}