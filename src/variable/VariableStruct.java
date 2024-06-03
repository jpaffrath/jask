package variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a jask struct variable
 *
 * @author Julius Paffrath
 *
 */
public class VariableStruct extends Variable {
	private String name;
	private Map<String, Variable> heap;
	
	public VariableStruct() {
		super();
		this.setType(VariableType.Struct);
	}
	
	/**
	 * Initializes a new struct with a given name and heap
	 * 
	 * @param name name of the new struct
	 * @param heap heap of the new struct
	 */
	public VariableStruct(String name, Map<String, Variable> heap) {
		this();
		this.name = name;
		this.heap = heap;
	}
	
	/**
	 * Initializes a new struct based on a given struct and a given name 
	 * 
	 * @param name name of the new struct
	 * @param variable struct variable
	 */
	public VariableStruct(String name, VariableStruct variable) {
		this();
		this.name = name;
		this.heap = new HashMap<String, Variable>(variable.heap);
	}
	
	/**
	 * Initializes a new struct based on a given struct
	 * 
	 * @param variable struct variable
	 */
	public VariableStruct(VariableStruct variable) {
		this();
		this.name = variable.name;
		this.heap = variable.heap;
	}
	
	/**
	 * Initializes a new struct based on a given init string
	 * 
	 * @param name
	 * @param initString
	 */
	public VariableStruct(String name, String initString) {
		this();
		this.name = name;
		this.heap = new HashMap<String, Variable>();
		
		initString = initString.substring("Struct [".length());
		initString = initString.substring(0, initString.length()-1);
		
		for (String val : initString.split("\\), \\(")) {
			
			if (val.startsWith("(")) {
				val = val.substring(1);
			}
			if (val.endsWith(")")) {
				val = val.substring(0, val.length()-1);
			}
			
			name = val.substring(0, val.indexOf(","));
			val = val.substring(val.indexOf(",") + 2);
			
			if (Variable.isBoolean(val) == false && Variable.isNumber(val) == false) {
				this.setVariable(new Variable('"' + val + '"'), name);
			}
			else {
				this.setVariable(new Variable(val), name);
			}
		}
	}
	
	/**
	 * Sets a struct member
	 * 
	 * @param var variable to set
	 * @param name name of the variable
	 */
	public void setVariable(Variable var, String name) {
		this.heap.put(name, var);
	}
	
	/**
	 * Gets a struct member
	 * 
	 * @param name name of the variable
	 * @return the variable
	 */
	public Variable getVariable(String name) {
		return this.heap.get(name);
	}
	
	/**
	 * Override of toString
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Struct [");
		List<String> keys = new ArrayList<>(this.heap.keySet());
		
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			
			if (i == keys.size()-1) {
				builder.append("(" + key + ", " + this.heap.get(key) + ")");
			}
			else {
				builder.append("(" + key + ", " + this.heap.get(key) + "), ");
			}
		}
		
		builder.append("]");
		return builder.toString();
	}
}