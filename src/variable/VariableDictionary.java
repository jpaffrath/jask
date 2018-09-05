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
	private HashMap<Variable, Variable> dictionary;
	
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
			this.dictionary.put(new Variable(keys.get(i)), new Variable(values.get(i)));
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
		
		for (Variable key : dictionary.dictionary.keySet()) {
			this.dictionary.put(new Variable(key), new Variable(dictionary.dictionary.get(key)));
		}
	}
	
	/**
	 * Returns a string representation based on the variables content
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		
		for (Variable key : this.dictionary.keySet()) {
			builder.append("(" + key + ", " + this.dictionary.get(key) + ")");
		}
		
		builder.append("]");
		return builder.toString();
	}
}