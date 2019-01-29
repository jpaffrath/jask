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
	
	/**
	 * Initializes a new struct with a given name and heap
	 * 
	 * @param name name of the new struct
	 * @param heap heap of the new struct
	 */
	public VariableStruct(String name, Map<String, Variable> heap) {
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
		this.name = name;
		this.heap = new HashMap<String, Variable>(variable.heap);
	}
	
	/**
	 * Initializes a new struct based on a given struct
	 * 
	 * @param variable struct variable
	 */
	public VariableStruct(VariableStruct variable) {
		this.name = variable.name;
		this.heap = variable.heap;
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