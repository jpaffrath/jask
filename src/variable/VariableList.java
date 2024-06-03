package variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helper.Error;

/**
 * Represents a jask list variable
 *
 * @author Julius Paffrath
 *
 */
public class VariableList extends Variable {
	private List<Variable> values;
	
	public VariableList() {
		super();
		this.setType(VariableType.List);
	}

	/**
	 * Initializes a new list variable based on a generic value
	 *
	 * @param genericValue value as string
	 */
	public VariableList(String genericValue) {
		this();
		this.values = new ArrayList<Variable>();

		for (String curVal : genericValue.split(":")) {
			this.values.add(new Variable(curVal));
		}
	}

	/**
	 * Initializes a new list variable based on a given list variable
	 * 
	 * This constructor will create a deep copy of the given list variable
	 *
	 * @param var list variable to be used
	 */
	public VariableList(VariableList var) {
		this();

		this.values = new ArrayList<Variable>();

		for (Variable v : var.values) {
			if (v instanceof VariableList) {
				this.values.add(new VariableList((VariableList)v));
			}
			else if (v instanceof VariableDictionary) {
				this.values.add(new VariableDictionary((VariableDictionary)v));
			}
			else {
				this.values.add(new Variable(v));
			}
		}
	}
	
	/**
	 * Initializes a new list variable based on a given list of values
	 * 
	 * This constructor deep copies the given values
	 * 
	 * @param values values for the new list variable
	 */
	public VariableList(List<Variable> values) {
		this();
		
		this.values = new ArrayList<Variable>();
		
		for (Variable v : values) {
			if (v instanceof VariableList) {
				this.values.add(new VariableList((VariableList)v));
			}
			else if (v instanceof VariableDictionary) {
				this.values.add(new VariableDictionary((VariableDictionary)v));
			}
			else {
				this.values.add(new Variable(v));
			}
		}
	}

	/**
	 * Adds an element to the list based on a generic value
	 *
	 * @param genericValue value to add to the list
	 * @return true
	 */
	public boolean addElement(String genericValue) {
		this.values.add(new Variable(genericValue));
		return true;
	}

	/**
	 * Adds a variable to the list
	 *
	 * @param toAdd variable to add to the list
	 * @return true
	 */
	public boolean addElement(Variable toAdd) {
		if (toAdd instanceof VariableList) {
			this.values.add(new VariableList((VariableList)toAdd));
		}
		else if (toAdd instanceof VariableDictionary) {
			this.values.add(new VariableDictionary((VariableDictionary)toAdd));
		}
		else {
			this.values.add(new Variable(toAdd));
		}
		
		return true;
	}

	/**
	 * Sets a given variable at a given index
	 *
	 * @param index index to set
	 * @param toSet variable to set
	 * @return true if success
	 */
	public boolean setElement(int index, Variable toSet) {
		if (index >= this.values.size()) {
			return false;
		}
		
		this.values.set(index, new Variable(toSet));
		return true;
	}

	/**
	 * Removes an element at a given index
	 *
	 * @param index index to remove the element
	 * @return true if success
	 */
	public boolean removeElement(int index) {
		if (index >= this.values.size()) {
			Error.printErrorNoValueAtIndex(index);
			return false;
		}
		this.values.remove(index);
		return true;
	}

	/**
	 * Returns the size of the list
	 *
	 * @return size of the list
	 */
	public int getSize() {
		return this.values.size();
	}

	/**
	 * Override of toString
	 *
	 * Can be used to initialize a new list variable
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Variable curVal = null;

		if (this.values.size() == 1) {
			curVal = this.values.get(0);
			if (curVal.getType() == VariableType.String) {
				return "\"" + curVal.toString() +"\":";
			}
			return curVal.toString() + ":";
		}

		for (int i = 0; i < this.values.size(); i++) {
			curVal = this.values.get(i);

			if (i == this.values.size()-1) {
				if (curVal.getType() == VariableType.String) {
					builder.append("\"" + curVal.toString() + "\"");
				}
				else {
					builder.append(curVal.toString());
				}
			}
			else {
				if (curVal.getType() == VariableType.String) {
					builder.append("\"" + curVal.toString() + "\":");
				}
				else {
					builder.append(curVal.toString() + ":");
				}
			}
		}

		return builder.toString();
	}

	/**
	 * Returns a pretty formatted representation of the list variable
	 *
	 * @return a pretty formatted representation string
	 */
	public String getPrintString() {
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < this.values.size(); i++) {
			Variable val = this.values.get(i);
			
			if (val instanceof VariableList) {
				builder.append(((VariableList)val).getPrintString());
			}
			else {
				builder.append(val.toString());
			}
			
			if (i != this.values.size()-1) {
				builder.append(", ");
			}
		}

		builder.append("]");

		return builder.toString();
	}

	/**
	 * Returns the element at a given index as a string
	 *
	 * @param i index
	 * @return element at given index as string or ""
	 */
	public Variable getElementAtIndex(int i) {
		if (i > this.values.size()-1) {
			Error.printErrorNoValueAtIndex(i);
			return null;
		}

		return this.values.get(i);
	}

	/**
	 * Converts list variable to jask string
	 *
	 * @return jask string representing the list
	 */
	public String convertToString() {
		String ret = "\"";

		for (Variable var : this.values) {
			ret += var.toString();
		}

		return ret += "\"";
	}

	/**
	 * Checks if the list contains a given variable
	 *
	 * @param var variable to check
	 * @return true if the list contains the given variable
	 */
	public boolean contains(Variable var) {
		for (Variable v : this.values) {
			if (v.equals(var)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Compares this list with a given list based on their values
	 * 
	 * @param var the list to compare to
	 * @return true if both lists are equal
	 */
	public boolean equals(VariableList var) {
		if (this.values.size() != var.values.size()) {
			return false;
		}
		
		for (int i = 0; i < this.values.size(); i++) {
			if (this.values.get(i).equals(var.values.get(i)) == false) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Creates a deep copy of this list and reverses it
	 * 
	 * @return jask string representing the list
	 */
	public String reverseList() {
		VariableList newList = new VariableList(this);
		Collections.reverse(newList.values);
		return newList.toString();
	}

	/**
	 * Creates a deep copy of this list and adds the given elements
	 * 
	 * @param extender list with elements
	 * @return jask string representing the list
	 */
	public VariableList extend(VariableList extender) {
		VariableList newList = new VariableList(this);
		newList.values.addAll(new VariableList(extender).values);
		return newList;
	}
	
	/**
	 * Returns a new list initialized with the values in the given range
	 * 
	 * @param start included start index
	 * @param end included end index
	 * @return jask string representing the list
	 */
	public VariableList getRange(int start, int end) {
		if (start < 0 || start > this.values.size() - 1 || end < start || end > this.values.size() - 1) {
			return null;
		}
		
		List<Variable> values = this.values.subList(start, end + 1);
		return new VariableList(values);
	}
	
	/**
	 * Returns a new list initialized with the values after removing the given range
	 * 
	 * @param start included start index
	 * @param end included end index
	 * @return jask string representing the list
	 */
	public VariableList removeRange(int start, int end) {
		if (start < 0 || start > this.values.size() - 1 || end < start || end > this.values.size() - 1) {
			return null;
		}
		
		List<Variable> sub = new ArrayList<Variable>(this.values.subList(start, end + 1));
		List<Variable> ret = new ArrayList<Variable>(this.values);
		
		if (!ret.removeAll(sub)) {
			return null;
		}
		
		return new VariableList(ret);
	}
	
	/**
	 * Returns the values from the VariableList
	 *
	 * @return the values as a list
	 */
	public List<Variable> getValues() {
		return this.values;
	}
}