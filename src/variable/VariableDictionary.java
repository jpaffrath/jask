package variable;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a jask dictionary variable
 *
 * @author Julius Paffrath
 *
 */
public class VariableDictionary extends Variable {
	private HashMap<String, Variable> dictionary;
	
	/**
	 * Default constructor
	 */
	public VariableDictionary() {
		super();
		this.dictionary = new HashMap<>();
	}
	
	/**
	 * Initializes a new dictionary variable based on given keys and values
	 * 
	 * @param keys list of keys
	 * @param values list of values
	 */
	public VariableDictionary(List<Variable> keys, List<Variable> values) {
		super();
		this.dictionary = new HashMap<>();
		
		for (int i = 0; i < keys.size(); i++) {
			this.dictionary.put(keys.get(i).toString(), new Variable(values.get(i)));
		}
	}

	/**
	 * Initializes a new dictionary variable based on a given dictionary variable
	 * 
	 * This constructor will create a deep copy of the given dictionary variable
	 *
	 * @param dictionary dictionary variable to be used
	 */
	public VariableDictionary(VariableDictionary dictionary) {
		super();
		
		this.dictionary = new HashMap<>();
		
		for (String key : dictionary.dictionary.keySet()) {
			this.dictionary.put(key, new Variable(dictionary.dictionary.get(key)));
		}
	}
	
	/**
	 * Puts a new entry to the dictionary
	 * 
	 * @param key the new key
	 * @param value the new value
	 */
	public void put(Variable key, Variable value) {
		this.dictionary.put(key.toString(), value);
	}
	
	/**
	 * Gets the value associated to the provided key
	 * 
	 * @param key key in the dictionary
	 * @return value from the associated key
	 */
	public Variable get(Variable key) {
		return this.dictionary.get(key.toString());
	}
	
	/**
	 * Checks if the dictionary contains a given key
	 * 
	 * @param key key to be checked
	 * @return true if the dictionary contains the given key
	 */
	public boolean hasKey(Variable key) {
		return this.dictionary.containsKey(key.toString());
	}
	
	/**
	 * Returns a string representation based on the variables content
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		
		for (String key : this.dictionary.keySet()) {
			builder.append("(" + key + ", " + this.dictionary.get(key) + ")");
		}
		
		builder.append("]");
		return builder.toString();
	}
}