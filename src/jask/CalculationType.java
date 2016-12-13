package jask;

/**
 * Enumeration for different types of calculations
 *
 * @author Julius Paffrath
 *
 */
public enum CalculationType {
	Plus, Minus, Times, Divide, NoType;

	/**
	 * Returns the appropriate {@link CalculationType} based on a given string
	 *
	 * @param type type as string
	 * @return {@link CalculationType}
	 */
	public static CalculationType getType(String type) {
		if (type.contentEquals("plus"))   return CalculationType.Plus;
		if (type.contentEquals("minus"))  return CalculationType.Minus;
		if (type.contentEquals("times"))  return CalculationType.Times;
		if (type.contentEquals("divide")) return CalculationType.Divide;

		return CalculationType.NoType;
	}
}