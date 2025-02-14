package expression;

import java.util.List;

/**
 * Represents a jask expression
 *
 * @author Julius Paffrath
 *
 */
public class Expression {
	private ExpressionType type;
	private List<String> tokens;

	/**
	 * General constructor
	 *
	 * @param type type of the expression
	 * @param tokens tokens of the expression
	 */
	public Expression(ExpressionType type, List<String> tokens) {
		this.type = type;
		this.tokens = tokens;
	}

	/**
	 * Returns the type of the expression
	 *
	 * @return type of the expression
	 */
	public ExpressionType getType() {
		return this.type;
	}

	/**
	 * Returns the tokens of the expression
	 *
	 * @return tokens of the expression
	 */
	public List<String> getTokens(){
		return this.tokens;
	}

	/**
	 * Override of toString
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

	    for (int i = 0; i < this.tokens.size(); i++) {
	        builder.append(this.tokens.get(i));
	        if (i < this.tokens.size() - 1) {
	            builder.append(" ");
	        }
	    }

	    return builder.toString();
	}
}
