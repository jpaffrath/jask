package jask;

import java.util.List;

public class Expression {
	private ExpressionType type;
	private List<Token> tokens;
	
	public Expression(ExpressionType type, List<Token> tokens) {
		this.type = type;
		this.tokens = tokens;
	}
	
	public ExpressionType getType() {
		return type;
	}
	
	public List<Token> getTokens(){
		return tokens;
	}
}
