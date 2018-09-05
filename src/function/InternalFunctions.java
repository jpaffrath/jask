package function;

import static jask.Constants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import helper.Error;
import helper.Helpers;
import variable.Variable;
import variable.VariableDictionary;
import variable.VariableList;
import variable.VariableType;

/**
 * Implements internal functions
 *
 * @author Julius Paffrath
 *
 */
public class InternalFunctions {
	private HashMap<String, InternalFunction> functions;

	/**
	 * General constructor
	 */
	public InternalFunctions() {
		this.functions = new HashMap<String, InternalFunction>();
		this.setUpFunctions();
	}

	/**
	 * Adds internal functions to functions heap
	 */
	private void setUpFunctions() {
		this.functions.put("print", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return print(heap, params, false);
			}
		});
		this.functions.put("printLine", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return print(heap, params, true);
			}
		});
		this.functions.put("read", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				if (params.length == 0) {
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(System.in);
					return new Variable('"' + scanner.nextLine() + '"');
				}
				return new Variable("");
			}
		});
		this.functions.put("list", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				if (!param.contains(":")) {
					return new VariableList(param + ":");
				}

				return new VariableList(param);
			}
		});
		this.functions.put("listGet", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listGet(heap, params);
			}
		});
		this.functions.put("listSize", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listSize(heap, params);
			}
		});
		this.functions.put("listAdd", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listAdd(heap, params);
			}
		});
		this.functions.put("listRemove", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listRemove(heap, params);
			}
		});
		this.functions.put("listSet", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listSet(heap, params);
			}
		});
		this.functions.put("listFromString", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listFromString(heap, params);
			}
		});
		this.functions.put("listToString", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listToString(heap, params);
			}
		});
		this.functions.put("listContains", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listContains(heap, params);
			}
		});
		this.functions.put("listReverse", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listReverse(heap, params);
			}
		});
		this.functions.put("listExtend", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listExtend(heap, params);
			}
		});
		this.functions.put("listGetRange", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listGetRange(heap, params);
			}
		});
		this.functions.put("listRemoveRange", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return listRemoveRange(heap, params);
			}
		});
		this.functions.put("readFile", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return readFile(heap, params);
			}
		});
		this.functions.put("writeFile", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return writeFile(heap, params);
			}
		});
		this.functions.put("appendToFile", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return appendToFile(heap, params);
			}
		});
		this.functions.put("isString", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable((heap.get(params[0]).getType() == VariableType.String ? TRUE : FALSE));
			}
		});
		this.functions.put("isNumber", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable((heap.get(params[0]).getType() == VariableType.Number ? TRUE : FALSE));
			}
		});
		this.functions.put("isBool", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable((heap.get(params[0]).getType() == VariableType.Bool ? TRUE : FALSE));
			}
		});
		this.functions.put("isList", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable((heap.get(params[0]) instanceof VariableList ? TRUE : FALSE));
			}
		});
		this.functions.put("exit", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return exit(heap, params);
			}
		});
		this.functions.put("_pow", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(String.valueOf(Math.pow(heap.get(params[0]).getDoubleValue(), heap.get(params[1]).getDoubleValue())));
			}
		});
		this.functions.put("dictionary", new InternalFunction() {
			@Override
			public Variable execute(HashMap<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionary(heap, params);
			}
		});
	}

	/**
	 * Executes given function
	 *
	 * @param _heap heap of the function
	 * @param functionName name of the function
	 * @param param parameter as string
	 * @return result of function execution
	 */
	public Variable executeFunction(List<Variable> _heap, String functionName, String param) {
		List<String> temp = Helpers.splitParams(param);
		String params[] = new String[temp.size()];
		params = temp.toArray(params);

		return this.functions.get(functionName).execute(convertHeap(_heap, params), functionName, param, params);
	}

	/**
	 * Checks if a given function name is an internal function
	 *
	 * @param functionName name of the function
	 * @return true if the given function name is an internal function
	 */
	public boolean isInternalFunction(String functionName) {
		return this.functions.containsKey(functionName);
	}

	/**
	 * Converts given heap and parameter to usable internal heap
	 *
	 * @param _heap heap to convert
	 * @param params parameter names as array
	 * @return a new heap from the given heap and parameter array
	 */
	private HashMap<String, Variable> convertHeap(List<Variable> _heap, String[] params) {
		HashMap<String, Variable> heap = new HashMap<String, Variable>();

		for (int i = 0; i < params.length; i++) {
			heap.put(params[i], _heap.get(i));
		}

		return heap;
	}

	/**
	 * Internal implementation of print
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @param newLine toggles if a newline should be append at the end of the print
	 * @return TRUE or FALSE
	 */
	private Variable print(HashMap<String, Variable> heap, String[] params, boolean newLine) {
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
			
			if (var instanceof VariableList) {
				output += (((VariableList)var).getPrintString());
			}
			else {
				output += (var.toString());
			}
		}

		if (newLine) {
			System.out.println(output);
		}
		else {
			System.out.print(output);
		}

		return new Variable("TRUE");
	}

	/**
	 * Internal implementation of listGet
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return element from list at given index
	 */
	private Variable listGet(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (!Variable.isNumber(params[1])) {
				Error.printErrorValueNotApplicable(params[1]);
				return new Variable(NULL);
			}

			return new VariableList(((VariableList)var).getElementAtIndex(Integer.parseInt(params[1])));
		}

		if (index.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return new Variable(NULL);
		}

		return new Variable(((VariableList)var).getElementAtIndex((int)index.getDoubleValue()));
	}

	/**
	 * Internal implementation of listSize
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return size of list
	 */
	private Variable listSize(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		return new Variable(Integer.toString(((VariableList)var).getSize()));
	}

	/**
	 * Internal implementation of listAdd
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return new list or NULL
	 */
	private Variable listAdd(HashMap<String, Variable> heap, String[] params) {
		VariableList var = new VariableList((VariableList)heap.get(params[0]));

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable toAdd = heap.get(params[1]);
		if (toAdd == null) {
			if (!((VariableList)var).addElement(params[1])) {
				return new Variable(NULL);
			}
		}
		else {
			if (!((VariableList)var).addElement(toAdd)) {
				return new Variable(NULL);
			}
		}

		return var;
	}

	/**
	 * Internal implementation of listRemove
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return new list or NULL
	 */
	private Variable listRemove(HashMap<String, Variable> heap, String[] params) {
		VariableList var = new VariableList((VariableList)heap.get(params[0]));

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (!Variable.isNumber(params[1])) {
				return new Variable(NULL);
			}

			((VariableList)var).removeElement(Integer.parseInt(params[1]));
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return new Variable(NULL);
			}

			((VariableList)var).removeElement((int)index.getDoubleValue());
		}

		return var;
	}

	/**
	 * Internal implementation of listSet
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return new list or NULL
	 */
	private Variable listSet(HashMap<String, Variable> heap, String[] params) {
		VariableList var = new VariableList((VariableList)heap.get(params[0]));

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable index = heap.get(params[1]);
		if (index == null) {
			if (!Variable.isNumber(params[1])) {
				return new Variable(NULL);
			}

			((VariableList)var).removeElement(Integer.parseInt(params[1]));
			return var;
		}
		else {
			if (index.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return new Variable(NULL);
			}
		}

		((VariableList)var).setElement((int)index.getDoubleValue(), heap.get(params[2]));
		return var;
	}

	/**
	 * Internal implementation of listFromString
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return new list or NULL
	 */
	private Variable listFromString(HashMap<String, Variable> heap, String[] params) {
		String strVal = "";
		String retVal = "";
		Variable var = heap.get(params[0]);

		if (var.getType() == VariableType.String) {
			strVal = var.getStringValue();
		}
		else {
			Error.printErrorVariableIsNotAString(params[0]);
			return new Variable(NULL);
		}
		
		for (int i = 0; i < strVal.length(); i++) {
			if (i == strVal.length()-1) {
				retVal += "\"" + strVal.charAt(i) + "\"";
			}
			else {
				retVal += "\"" + strVal.charAt(i) + "\"" + ":";
			}
		}

		return new VariableList(retVal);
	}

	/**
	 * Internal implementation of listToString
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return new string or NULL
	 */
	private Variable listToString(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		return new Variable(((VariableList)var).convertToString());
	}

	/**
	 * Internal implementation of listContains
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return TRUE, FALSE or NULL
	 */
	private Variable listContains(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		return new Variable(((VariableList)var).contains(heap.get(params[1])) ? TRUE : FALSE);
	}
	
	/**
	 * Internal implementation of listReverse
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a deep copy of the original list reversed
	 */
	private Variable listReverse(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		return new VariableList(((VariableList)var).reverseList());
	}
	
	/**
	 * Internal implementation of listExtend
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a deep copy of the original list extended
	 */
	private Variable listExtend(HashMap<String, Variable> heap, String[] params) {
		Variable toExtend = heap.get(params[0]);

		if (!(toExtend instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable extender = heap.get(params[1]);

		if (!(extender instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[1]);
			return new Variable(NULL);
		}

		return new VariableList(((VariableList)toExtend).extend((VariableList)extender));
	}
	
	/**
	 * Internal implementation of listGetRange
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new list initialized with the values in the given range
	 */
	private Variable listGetRange(HashMap<String, Variable> heap, String[] params) {
		Variable list = heap.get(params[0]);

		if (!(list instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}
		
		Variable varStart = heap.get(params[1]);
		Variable varEnd = heap.get(params[2]);
		
		int start = 0;
		int end = 0;
		
		if (varStart == null) {
			start = Integer.parseInt(params[1]);
		}
		else {
			if (varStart.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return new Variable(NULL);
			}
			
			start = (int)varStart.getDoubleValue();
		}
		
		if (varEnd == null) {
			end = Integer.parseInt(params[2]);
		}
		else {
			if (varEnd.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[2]);
				return new Variable(NULL);
			}
			
			end = (int)varEnd.getDoubleValue();
		}
	
		return new VariableList(((VariableList)list).getRange(start, end));
	}
	
	/**
	 * Internal implementation of listRemoveRange
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new list initialized with the values in the given range
	 */
	private Variable listRemoveRange(HashMap<String, Variable> heap, String[] params) {
		Variable list = heap.get(params[0]);
		
		if (!(list instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}
		
		Variable varStart = heap.get(params[1]);
		Variable varEnd = heap.get(params[2]);
		
		int start = 0;
		int end = 0;
		
		if (varStart == null) {
			start = Integer.parseInt(params[1]);
		}
		else {
			if (varStart.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[1]);
				return new Variable(NULL);
			}
			
			start = (int)varStart.getDoubleValue();
		}
		
		if (varEnd == null) {
			end = Integer.parseInt(params[2]);
		}
		else {
			if (varEnd.getType() != VariableType.Number) {
				Error.printErrorVariableIsNotANumber(params[2]);
				return new Variable(NULL);
			}
			
			end = (int)varEnd.getDoubleValue();
		}
	
		return new VariableList(((VariableList)list).removeRange(start, end));
	}
	
	/**
	 * Internal implementation of readFile
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new variable containing the content of the file
	 */
	private Variable readFile(HashMap<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		String fileName = "";
		
		if (var.getType() == VariableType.String) {
			fileName = var.getStringValue();
		}
		else {
			Error.printErrorVariableIsNotAString(params[0]);
			return new Variable(NULL);
		}
		
		try {
			byte[] content = Files.readAllBytes(Paths.get(fileName));
			return new Variable('"' + new String(content) + '"');
		} catch (IOException e) {
			Error.printErrorFileReadError(fileName);
		}
		
		return new Variable(NULL);
	}
	
	/**
	 * Internal implementation of writeFile
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return TRUE or FALSE
	 */
	private Variable writeFile(HashMap<String, Variable> heap, String[] params) {
		Variable varFileName = heap.get(params[0]);
		String fileName = "";
		
		if (varFileName.getType() == VariableType.String) {
			fileName = varFileName.getStringValue();
		}
		else {
			Error.printErrorVariableIsNotAString(params[0]);
			return new Variable(FALSE);
		}
		
		Variable varFileContent = heap.get(params[1]);
		String fileContent = "";
		
		if (varFileContent.getType() == VariableType.String) {
			fileContent = varFileContent.getStringValue();
		}
		else {
			Error.printErrorVariableIsNotAString(params[1]);
			return new Variable(FALSE);
		}
		
		try {
			Files.write(Paths.get(fileName), fileContent.getBytes());
			return new Variable(TRUE);
		}
		catch (IOException e) {
			Error.printErrorFileWriteError(fileName);
		}
		
		return new Variable(FALSE);
	}
	
	/**
	 * Internal implementation of appendToFile
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return the content of the file
	 */
	private Variable appendToFile(HashMap<String, Variable> heap, String[] params) {
		Variable varFileName = heap.get(params[0]);
		String fileName = "";
		
		if (varFileName.getType() == VariableType.String) {
			fileName = varFileName.getStringValue();
		}
		else {
			Error.printErrorVariableIsNotAString(params[0]);
			return new Variable(FALSE);
		}
		
		Variable varFileContent = heap.get(params[1]);
		String fileContent = "";
		
		if (varFileContent.getType() == VariableType.String) {
			fileContent = varFileContent.getStringValue();
		}
		else {
			Error.printErrorVariableIsNotAString(params[1]);
			return new Variable(FALSE);
		}
		
		try {
			Files.write(Paths.get(fileName), fileContent.getBytes(), StandardOpenOption.APPEND);
			return new Variable(TRUE);
		} catch (IOException e) {
			Error.printErrorFileWriteError(fileName);
		}
		
		return new Variable(FALSE);
	}
	
	/**
	 * Internal implementation of dictionary
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new initialized dictionary
	 */
	private Variable dictionary(HashMap<String, Variable> heap, String[] params) {
		if (params.length == 0) {
			return new VariableDictionary();
		}
		
		// exit when the number of parameters cannot be divided by 2
		if (params.length % 2 != 0) {
			Error.printErrorNoProperParametersForDictionary();
			return new Variable(NULL);
		}
		
		List<Variable> keys = new ArrayList<>();
		List<Variable> vals = new ArrayList<>();
		
		for (int i = 0; i < params.length; i+=2) {
			keys.add(heap.get(params[i]));
			vals.add(heap.get(params[i+1]));
		}
		
		return new VariableDictionary(keys, vals);
	}

	/**
	 * Internal implementation of exit
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return NULL
	 */
	private Variable exit(HashMap<String, Variable> heap, String[] params) {
		int code = -1;

		Variable var = heap.get(params[0]);
		if (var == null) {
			code = Integer.parseInt(params[0]);
		}

		System.exit(code);
		return new Variable(NULL);
	}
}