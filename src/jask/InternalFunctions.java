package jask;

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
	private HashMap<String, InternalFunction> functions;

	private final String TRUE = "TRUE";
	private final String FALSE = "FALSE";
	private final String NULL = "NULL";

	public InternalFunctions() {
		this.functions = new HashMap<String, InternalFunction>();
		this.setUpFunctions();
	}

	private void setUpFunctions() {
		this.functions.put("print", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return print(heap, params, false);
			}
		});
		this.functions.put("printLine", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return print(heap, params, true);
			}
		});
		this.functions.put("read", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				if (params.length == 0) {
					Scanner scanner = new Scanner(System.in);
					String line = '"' + scanner.nextLine() + '"';
					scanner.close();
					return line;
				}
				return "";
			}
		});
		this.functions.put("list", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				if (!param.contains(":")) {
					return param + ":";
				}

				return param;
			}
		});
		this.functions.put("listGet", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listGet(heap, params);
			}
		});
		this.functions.put("listSize", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listSize(heap, params);
			}
		});
		this.functions.put("listAdd", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listAdd(heap, params);
			}
		});
		this.functions.put("listRemove", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listRemove(heap, params);
			}
		});
		this.functions.put("listSet", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listSet(heap, params);
			}
		});
		this.functions.put("listFromString", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listFromString(heap, params);
			}
		});
		this.functions.put("listToString", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listToString(heap, params);
			}
		});
		this.functions.put("listContains", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listContains(heap, params);
			}
		});
		this.functions.put("isString", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return isString(heap, params);
			}
		});
		this.functions.put("isNumber", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return isNumber(heap, params);
			}
		});
		this.functions.put("isBool", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return isBool(heap, params);
			}
		});
		this.functions.put("isList", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return isList(heap, params);
			}
		});
		this.functions.put("exit", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return exit(heap, params);
			}
		});
		this.functions.put("_pow", new InternalFunction() {
			@Override
			public String execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return String.valueOf(Math.pow(heap.get(params[0]).getDoubleValue(), heap.get(params[1]).getDoubleValue()));
			}
		});
	}

	public String executeFunction(List<Variable> _heap, String functionName, String param) {
		List<String> temp = Helpers.splitParams(param);
		String params[] = new String[temp.size()];
		params = temp.toArray(params);

		return this.functions.get(functionName).execute(convertHeap(_heap, params), functionName, param, params);
	}

	public boolean isInternalFunction(String functionName) {
		if (isInternalListFunction(functionName)) return false;
		return this.functions.containsKey(functionName);
	}

	public boolean isInternalListFunction(String functionName) {
		return (functionName.contentEquals("list") ||
				functionName.contentEquals("listFromString") ||
				functionName.contentEquals("listAdd") ||
				functionName.contentEquals("listRemove"));
	}

	public HashMap<String, Variable> convertHeap(List<Variable> _heap, String[] params) {
		HashMap<String, Variable> heap = new HashMap<String, Variable>();

		for (int i = 0; i < params.length; i++) {
			heap.put(params[i], _heap.get(i));
		}

		return heap;
	}

	private String print(HashMap<String, Variable> heap, String[] params, boolean newLine) {
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
				return FALSE;
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

		return TRUE;
	}

	private String listGet(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return NULL;
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (!Variable.isNumber(params[1])) {
				Error.printErrorValueNotApplicable(params[1]);
				return NULL;
			}

			return ((VariableList)var).getElementAtIndex(Integer.parseInt(params[1]));
		}

		if (index.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return NULL;
		}

		return ((VariableList)var).getElementAtIndex((int)index.getDoubleValue());
	}

	private String listSize(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return NULL;
		}

		return Integer.toString(((VariableList)var).getSize());
	}

	private String listAdd(HashMap<String, Variable> heap, String[] params) {
		Variable var = new VariableList((VariableList)heap.get(params[0]));
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return NULL;
		}

		Variable toAdd = heap.get(params[1]);
		if (toAdd == null) {
			if (!((VariableList)var).addElement(params[1])) {
				return NULL;
			}
		}
		else {
			if (!((VariableList)var).addElement(toAdd)) {
				return NULL;
			}
		}

		return var.toString();
	}

	private String listRemove(HashMap<String, Variable> heap, String[] params) {
		Variable var = new VariableList((VariableList)heap.get(params[0]));
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return NULL;
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (!Variable.isNumber(params[1])) {
				return NULL;
			}

			((VariableList)var).removeElement(Integer.parseInt(params[1]));
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return NULL;
			}

			((VariableList)var).removeElement((int)index.getDoubleValue());
		}

		return var.toString();
	}

	private String listSet(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return FALSE;
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (Variable.isNumber(params[1])) {
				((VariableList)var).removeElement(Integer.parseInt(params[1]));
				return TRUE;
			}

			return FALSE;
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return FALSE;
			}
		}

		Variable toSet = heap.get(params[2]);
		if (toSet == null) {
			toSet = new Variable(params[2]);
			if (toSet.getType() == VariableType.NoType) return FALSE;
		}

		if (((VariableList)var).setElement((int)index.getDoubleValue(), toSet)) {
			return TRUE;
		}

		return FALSE;
	}

	private String listFromString(HashMap<String, Variable> heap, String[] params) {
		String strVal = "";
		String retVal = "";
		Variable var = heap.get(params[0]);

		if (var == null) {
			if (Variable.isString(params[0])) {
				strVal = params[0];
			}
			else {
				Error.printErrorVariableIsNotAString(params[0]);
				return NULL;
			}
		}
		else {
			if (var.getType() == VariableType.String) {
				strVal = var.getStringValue();
			}
			else {
				Error.printErrorVariableIsNotAString(params[0]);
				return NULL;
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

	private String listToString(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return NULL;
		}

		return ((VariableList)var).convertToString();
	}

	private String listContains(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null || !(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return NULL;
		}

		Variable element = heap.get(params[1]);
		if (element == null) {
			element = new Variable(params[1]);
		}

		return ((VariableList)var).contains(element) ? TRUE : FALSE;
	}

	private String isString(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null) {
			if (Variable.isString(params[0])) return TRUE;
			return FALSE;
		}

		if (var.getType() == VariableType.String) return TRUE;
		return FALSE;
	}

	private String isNumber(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null) {
			if (Variable.isNumber(params[0])) return TRUE;
			return FALSE;
		}

		if (var.getType() == VariableType.Number) return TRUE;
		return FALSE;
	}

	private String isBool(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		if (var == null) {
			String val = params[0];
			if (val.contentEquals(TRUE) || val.contentEquals(FALSE)) return TRUE;
			return FALSE;
		}

		if (var.getType() == VariableType.Bool) return TRUE;
		return FALSE;
	}

	private String isList(HashMap<String, Variable> heap, String[] params) {
		if (heap.get(params[0]) instanceof VariableList) return TRUE;
		return FALSE;
	}

	private String exit(HashMap<String, Variable> heap, String[] params) {
		int code = -1;

		Variable var = heap.get(params[0]);
		if (var == null) {
			code = Integer.parseInt(params[0]);
		}

		System.exit(code);
		return NULL;
	}
}