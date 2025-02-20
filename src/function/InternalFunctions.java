package function;

import static jask.Constants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import helper.Error;
import helper.Helpers;
import variable.Variable;
import variable.VariableDictionary;
import variable.VariableList;
import variable.VariableStruct;
import variable.VariableType;

/**
 * Implements internal functions
 *
 * @author Julius Paffrath
 *
 */
public class InternalFunctions {
	private Map<String, InternalFunction> functions;
	private List<String> internalModules;

	/**
	 * General constructor
	 */
	public InternalFunctions() {
		this.functions = new HashMap<String, InternalFunction>();
		this.internalModules = new ArrayList<>();
		this.setUpFunctions();
	}
	
	public InternalFunctions(InternalFunctions internalFunctions) {
		this.functions = new HashMap<String, InternalFunction>();
		this.functions.putAll(internalFunctions.functions);
		
		this.setUpFunctions();
	}

	/**
	 * Adds internal functions to functions heap
	 */
	private void setUpFunctions() {
		this.functions.put("print", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return print(heap, params, false);
			}
		});
		this.functions.put("printLine", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return print(heap, params, true);
			}
		});
		this.functions.put("read", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
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
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return list(heap, params);
			}
		});
		this.functions.put("listGet", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listGet(heap, params);
			}
		});
		this.functions.put("listSize", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listSize(heap, params);
			}
		});
		this.functions.put("listAdd", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listAdd(heap, params);
			}
		});
		this.functions.put("listRemove", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listRemove(heap, params);
			}
		});
		this.functions.put("listSet", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listSet(heap, params);
			}
		});
		this.functions.put("listFromString", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listFromString(heap, params);
			}
		});
		this.functions.put("listToString", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listToString(heap, params);
			}
		});
		this.functions.put("listContains", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listContains(heap, params);
			}
		});
		this.functions.put("listReverse", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listReverse(heap, params);
			}
		});
		this.functions.put("listExtend", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listExtend(heap, params);
			}
		});
		this.functions.put("listGetRange", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listGetRange(heap, params);
			}
		});
		this.functions.put("listRemoveRange", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return listRemoveRange(heap, params);
			}
		});
		this.functions.put("readFile", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return readFile(heap, params);
			}
		});
		this.functions.put("writeFile", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return writeFile(heap, params);
			}
		});
		this.functions.put("appendToFile", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return appendToFile(heap, params);
			}
		});
		this.functions.put("isString", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(heap.get(params[0]).getType() == VariableType.String ? TRUE : FALSE);
			}
		});
		this.functions.put("isNumber", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(heap.get(params[0]).getType() == VariableType.Number ? TRUE : FALSE);
			}
		});
		this.functions.put("isBool", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(heap.get(params[0]).getType() == VariableType.Bool ? TRUE : FALSE);
			}
		});
		this.functions.put("isList", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(heap.get(params[0]) instanceof VariableList ? TRUE : FALSE);
			}
		});
		this.functions.put("isDictionary", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(heap.get(params[0]) instanceof VariableDictionary ? TRUE : FALSE);
			}
		});
		this.functions.put("isStruct", new InternalFunction() {	
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(heap.get(params[0]) instanceof VariableStruct ? TRUE : FALSE);
			}
		});
		this.functions.put("exit", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return exit(heap, params);
			}
		});
		this.functions.put("_pow", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return new Variable(String.valueOf(Math.pow(heap.get(params[0]).getDoubleValue(), heap.get(params[1]).getDoubleValue())));
			}
		});
		this.functions.put("dictionary", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionary(heap, params);
			}
		});
		this.functions.put("dictionaryGet", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionaryGet(heap, params);
			}
		});
		this.functions.put("dictionaryPut", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionaryPut(heap, params);
			}
		});
		this.functions.put("dictionarySize", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionarySize(heap, params);
			}
		});
		this.functions.put("dictionaryGetKeys", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionaryGetKeys(heap, params);
			}
		});
		this.functions.put("dictionaryGetValues", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionaryGetValues(heap, params);
			}
		});
		this.functions.put("dictionaryPutLists", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return dictionaryPutLists(heap, params);
			}
		});
		this.functions.put("random", new InternalFunction() {
			@Override
			public Variable execute(Map<String, Variable> heap, String functionName, String param, String[] params) {
				return random(heap, params);
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
		List<String> n = null;
		
		if (Helpers.isFunction(param)) {
			n = new ArrayList<>();
			n.add(param);
		}
		else {
			n = Helpers.splitParams(param);
		}

		List<String> temp = n;
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
	 * Checks if a given module name is an internal module
	 * 
	 * @param moduleName name of the module
	 * @return true if the given module name is an internal module
	 */
	public static boolean isInternalModule(String moduleName) {
		switch (moduleName) {
		case "jask.os":
		case "jask.system":
		case "jask.date":
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Loads an internal module
	 * 
	 * @param moduleName name of the module
	 */
	public void loadInternalModule(String moduleName) {
		InternalFunctionsBase module = new InternalFunctionsBase();
		
		switch (moduleName) {
		case "jask.os":
			module = new InternalFunctionsOS();
			this.internalModules.add("jask.os");
			break;
		case "jask.system":
			module = new InternalFunctionsSystem();
			this.internalModules.add("jask.system");
			break;
		case "jask.date":
			module = new InternalFunctionDate();
			this.internalModules.add("jask.date");
			break;
		default:
			return;
		}
		
		this.functions.putAll(module.getFunctions());
	}
	
	/**
	 * Returns a list containing all loaded internal modules
	 * 
	 * @return
	 */
	public List<String> getLoadedModuleNames() {
		return this.internalModules;
	}

	/**
	 * Converts given heap and parameter to usable internal heap
	 *
	 * @param _heap heap to convert
	 * @param params parameter names as array
	 * @return a new heap from the given heap and parameter array
	 */
	private Map<String, Variable> convertHeap(List<Variable> _heap, String[] params) {
		Map<String, Variable> heap = new HashMap<String, Variable>();

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
	private Variable print(Map<String, Variable> heap, String[] params, boolean newLine) {
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
				output += ((VariableList)var).getPrintString();
			}
			else {
				output += var.toString();
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
	 * Internal implementation of list
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new list
	 */
	private Variable list(Map<String, Variable> heap, String[] params) {
		List<Variable> values = new ArrayList<>();
		for (String parameter : params) {
			values.add(heap.get(parameter));
		}
		return new VariableList(values);
	}

	/**
	 * Internal implementation of listGet
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return element from list at given index
	 */
	private Variable listGet(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable index = heap.get(params[1]);
		Variable val = ((VariableList)var).getElementAtIndex((int)index.getDoubleValue());
		
		if (val instanceof VariableList) {
			return new VariableList((VariableList)val);
		}
		else if (val instanceof VariableDictionary) {
			return new VariableDictionary((VariableDictionary)val);
		}
		else if (val instanceof VariableStruct) {
			return new VariableStruct((VariableStruct)val);
		}
		
		return new Variable(val);
	}

	/**
	 * Internal implementation of listSize
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return size of list
	 */
	private Variable listSize(Map<String, Variable> heap, String[] params) {
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
	private Variable listAdd(Map<String, Variable> heap, String[] params) {
		Variable var = new VariableList((VariableList)heap.get(params[0]));

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable toAdd = heap.get(params[1]);
		if (!((VariableList)var).addElement(toAdd)) {
			return new Variable(NULL);
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
	private Variable listRemove(Map<String, Variable> heap, String[] params) {
		VariableList var = new VariableList((VariableList)heap.get(params[0]));

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable index = heap.get(params[1]);
		if (index.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return new Variable(NULL);
		}

		((VariableList)var).removeElement((int)index.getDoubleValue());
		return var;
	}

	/**
	 * Internal implementation of listSet
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return new list or NULL
	 */
	private Variable listSet(Map<String, Variable> heap, String[] params) {
		VariableList var = new VariableList((VariableList)heap.get(params[0]));

		if (!(var instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}

		Variable index = heap.get(params[1]);
		if (index.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return new Variable(NULL);
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
	private Variable listFromString(Map<String, Variable> heap, String[] params) {
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
	private Variable listToString(Map<String, Variable> heap, String[] params) {
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
	private Variable listContains(Map<String, Variable> heap, String[] params) {
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
	private Variable listReverse(Map<String, Variable> heap, String[] params) {
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
	private Variable listExtend(Map<String, Variable> heap, String[] params) {
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
	private Variable listGetRange(Map<String, Variable> heap, String[] params) {
		Variable list = heap.get(params[0]);

		if (!(list instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}
		
		Variable varStart = heap.get(params[1]);
		if (varStart.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return new Variable(NULL);
		}
		
		int start = (int)varStart.getDoubleValue();
		
		Variable varEnd = heap.get(params[2]);
		if (varEnd.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[2]);
			return new Variable(NULL);
		}
		
		int end = (int)varEnd.getDoubleValue();
		
		VariableList newList = ((VariableList)list).getRange(start, end);
		
		if (newList == null) {
			return new Variable(NULL);
		}
	
		return new VariableList(newList);
	}
	
	/**
	 * Internal implementation of listRemoveRange
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new list initialized with the values in the given range
	 */
	private Variable listRemoveRange(Map<String, Variable> heap, String[] params) {
		Variable list = heap.get(params[0]);
		
		if (!(list instanceof VariableList)) {
			Error.printErrorVariableIsNotAList(params[0]);
			return new Variable(NULL);
		}
		
		Variable varStart = heap.get(params[1]);
		if (varStart.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return new Variable(NULL);
		}
		
		int start = (int)varStart.getDoubleValue();
		
		int end = 0;
		Variable varEnd = heap.get(params[2]);
		if (varEnd.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[2]);
			return new Variable(NULL);
		}
			
		end = (int)varEnd.getDoubleValue();
		
		VariableList newList = ((VariableList)list).removeRange(start, end);
		
		if (newList == null) {
			return new Variable(NULL);
		}
	
		return new VariableList(newList);
	}
	
	/**
	 * Internal implementation of readFile
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new variable containing the content of the file
	 */
	private Variable readFile(Map<String, Variable> heap, String[] params) {
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
	private Variable writeFile(Map<String, Variable> heap, String[] params) {
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
	private Variable appendToFile(Map<String, Variable> heap, String[] params) {
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
	private Variable dictionary(Map<String, Variable> heap, String[] params) {
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
	 * Internal implementation of dictionaryGet
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new variable
	 */
	private Variable dictionaryGet(Map<String, Variable> heap, String[] params) {
		if (params.length != 2) {
			Error.printErrorNoProperParametersForDictionary();
			return new Variable(NULL);
		}
		
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableDictionary)) {
			Error.printErrorVariableIsNotADictionary(params[0]);
			return new Variable(NULL);
		}

		Variable key = heap.get(params[1]);
		if (key == null) {
			Error.printErrorNoValueGiven();
			return new Variable(NULL);
		}
		
		Variable value = ((VariableDictionary)var).get(key);
		
		if (value == null) {
			return new Variable(NULL);
		}
		
		return value;
	}
	
	/**
	 * Internal implementation of dictionaryPut
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new dictionary
	 */
	private Variable dictionaryPut(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		VariableDictionary dictionary = null;
		
		if (!(var instanceof VariableDictionary)) {
			Error.printErrorVariableIsNotADictionary(params[0]);
			return new Variable(NULL);
		}
		else {
			dictionary = new VariableDictionary((VariableDictionary)var);
		}

		Variable key = heap.get(params[1]);
		if (key == null) {
			Error.printErrorNoValueGiven();
			return new Variable(NULL);
		}
		
		Variable value = heap.get(params[2]);
		if (value == null) {
			Error.printErrorNoValueGiven();
			return new Variable(NULL);
		}
		
		dictionary.put(key, value);
		return dictionary;
	}
	
	/**
	 * Internal implementation of dictionaryPutLists
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a new dictionary
	 */
	private Variable dictionaryPutLists(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		VariableDictionary dictionary = null;
		
		if (!(var instanceof VariableDictionary)) {
			Error.printErrorVariableIsNotADictionary(params[0]);
			return new Variable(NULL);
		}
		else {
			dictionary = new VariableDictionary((VariableDictionary)var);
		}

		Variable keys = heap.get(params[1]);
		if (keys == null) {
			Error.printErrorNoValueGiven();
			return new Variable(NULL);
		}
		
		Variable values = heap.get(params[2]);
		if (values == null) {
			Error.printErrorNoValueGiven();
			return new Variable(NULL);
		}
		
		if (keys instanceof VariableList == false) {
			Error.printErrorVariableIsNotAList(params[1]);
		}
		
		if (values instanceof VariableList == false) {
			Error.printErrorVariableIsNotAList(params[2]);
		}
		
		dictionary.putList((VariableList)keys, (VariableList)values);
		return dictionary;
	}
	
	/**
	 * Internal implementation of dictionarySize
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return size of dictionary
	 */
	private Variable dictionarySize(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableDictionary)) {
			Error.printErrorVariableIsNotADictionary(params[0]);
			return new Variable(NULL);
		}

		return new Variable(Integer.toString(((VariableDictionary)var).getSize()));
	}
	
	/**
	 * Internal implementation of dictionaryGetKeys
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return keys of the dictionary
	 */
	private Variable dictionaryGetKeys(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableDictionary)) {
			Error.printErrorVariableIsNotADictionary(params[0]);
			return new Variable(NULL);
		}

		return ((VariableDictionary)var).getKeys();
	}
	
	/**
	 * Internal implementation of dictionaryGetValues
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return values of the dictionary
	 */
	private Variable dictionaryGetValues(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);

		if (!(var instanceof VariableDictionary)) {
			Error.printErrorVariableIsNotADictionary(params[0]);
			return new Variable(NULL);
		}

		return ((VariableDictionary)var).getValues();
	}
	
	/**
	 * Internal implementation of random
	 * 
	 * @param heap function heap
	 * @param params function parameters
	 * @return a random number in a given range
	 */
	private Variable random(Map<String, Variable> heap, String[] params) {
		Variable start = heap.get(params[0]);
		Variable end = heap.get(params[1]);
		
		if (start.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[0]);
			return new Variable(NULL);
		}
		
		if (end.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[1]);
			return new Variable(NULL);
		}
		
		return new Variable(ThreadLocalRandom.current().nextDouble(start.getDoubleValue(), end.getDoubleValue()+1));
	}

	/**
	 * Internal implementation of exit
	 *
	 * @param heap function heap
	 * @param params function parameters
	 * @return NULL
	 */
	private Variable exit(Map<String, Variable> heap, String[] params) {
		Variable var = heap.get(params[0]);
		
		if (var.getType() != VariableType.Number) {
			Error.printErrorVariableIsNotANumber(params[0]);
		}

		System.exit((int)var.getDoubleValue());
		return new Variable(NULL);
	}
}