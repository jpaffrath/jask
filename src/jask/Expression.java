package jask;

import java.util.List;

public class Expression {
	private ExpressionType type;
	private List<String> tokens;

	public Expression(ExpressionType type, List<String> tokens) {
		this.type = type;
		this.tokens = tokens;
	}

	public ExpressionType getType() {
		return type;
	}

	public List<String> getTokens(){
		return tokens;
	}
}
