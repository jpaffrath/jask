package jask;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	List<String> parse(List<String> lines) {
		List<String> tokens = new ArrayList<String>();
		boolean isStringParsing = false;
		StringBuilder builder = null;

		for (String line : lines) {
			for (String t : line.split(" ")) {
				if (t.equals("")) continue;

				char tChar[] = t.toCharArray();
				if (tChar[0] == '"') {
					isStringParsing = true;
					builder = new StringBuilder();
				}
				else if (tChar[tChar.length-1] == '"') {
					isStringParsing = false;
					builder.append(" " + t);
					tokens.add(builder.toString());
					continue;
				}

				if (isStringParsing) {
					if (builder.length() > 0) {
						builder.append(" " + t);
					}
					else {
						builder.append(t);
					}
				}
				else {
					tokens.add(t.replace("\t", ""));
				}
			}
		}

		return tokens;
	}
}
