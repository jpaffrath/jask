package jask;

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

	public Expression(ExpressionType type, List<String> tokens) {
		this.type = type;
		this.tokens = tokens;
	}

	public ExpressionType getType() {
		return this.type;
	}

	public List<String> getTokens(){
		return this.tokens;
	}
}
