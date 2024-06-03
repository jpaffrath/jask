package variable;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a jask dictionary variable
 *
 * @author Julius Paffrath
 *
 */
public class VariableDictionary extends Variable {
	private Map<String, Variable> dictionary;
	
	/**
	 * Default constructor
	 */
	public VariableDictionary() {
		super();
		this.dictionary = new HashMap<>();
		this.setType(VariableType.Dictionary);
	}
	
	/**
	 * Initializes a new dictionary variable based on given keys and values
	 * 
	 * @param keys list of keys
	 * @param values list of values
	 */
	public VariableDictionary(List<Variable> keys, List<Variable> values) {
		this();
		this.dictionary = new HashMap<>();
		
		for (int i = 0; i < keys.size(); i++) {
			Variable v = values.get(i);
			
			if (v instanceof VariableList) {
				this.dictionary.put(keys.get(i).toString(), new VariableList((VariableList)v));
			}
			else if (v instanceof VariableDictionary) {
				this.dictionary.put(keys.get(i).toString(), new VariableDictionary((VariableDictionary)v));
			}
			else {
				this.dictionary.put(keys.get(i).toString(), new Variable(values.get(i)));
			}
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
		this();
		
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
	 * Puts new entries to the dictionary based on given lists
	 * 
	 * @param keys list of keys
	 * @param values list of values
	 * @return a new dictionary containing the given keys and values
	 */
	public boolean putList(VariableList keys, VariableList values) {
		List<Variable> listKeys = keys.getValues();
		List<Variable> listValues = values.getValues();
		
		if (listKeys.size() != listValues.size()) {
			return false;
		}
		
		for (int i = 0; i < listKeys.size(); i++) {
			this.put(listKeys.get(i), listValues.get(i));
		}
		
		return true;
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
	 * Returns the size of the dictionary
	 * 
	 * @return the size of the dictionary
	 */
	public int getSize() {
		return this.dictionary.size();
	}
	
	/**
	 * Returns the keys of the dictionary
	 * 
	 * @return a new VariableList containing the keys of the dictionary
	 */
	public VariableList getKeys() {
		List<Variable> keyEntries = new ArrayList<Variable>();
		
		for (String key : this.dictionary.keySet()) {
			Variable var = new Variable();
			var.setStringValue(key);
			keyEntries.add(var);
		}
		
		return new VariableList(keyEntries);
	}
	
	/**
	 * Returns the values of the dictionary
	 * 
	 * @return a new VariableList containing the values of the dictionary
	 */
	public VariableList getValues() {
		List<Variable> valueEntries = new ArrayList<Variable>();
		
		for (Variable value : this.dictionary.values()) {
			valueEntries.add(value);
		}
		
		return new VariableList(valueEntries);
	}
	
	/**
	 * Returns a string representation based on the variables content
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		List<String> keys = new ArrayList<>(this.dictionary.keySet());
		
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			
			if (i == keys.size()-1) {
				builder.append("(" + key + ", " + this.dictionary.get(key) + ")");
			}
			else {
				builder.append("(" + key + ", " + this.dictionary.get(key) + "), ");
			}
		}
		
		builder.append("]");
		return builder.toString();
	}
}