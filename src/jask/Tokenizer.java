package jask;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

	List<Token> parse(List<String> lines) {
		List<Token> tokens = new ArrayList<Token>();

		for (String line : lines) {
			for (String t : line.split(" ")) {
				tokens.add(new Token(t));
			}
		}

		return tokens;
	}
}
