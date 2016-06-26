package jask;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	List<String> parse(List<String> lines) {
		List<String> tokens = new ArrayList<String>();
		String strBuffer = "";
		int lastIndex = 0;
		boolean strParsing = false;

		for (String line : lines) {
			if (line.equals("")) continue;

			if (line.contains("\"")) {
				lastIndex = line.lastIndexOf('"');
				strBuffer = line.substring(line.indexOf('"'), lastIndex+1);
			}

			for (String t : line.split(" ")) {
				if (t.equals("")) continue;
				if (t.charAt(0) == '"' && !strParsing) {
					if (t.charAt(t.length()-1) == '"' && t.length() > 1) {
						tokens.add(t);
						continue;
					}
					strParsing = true;
					tokens.add(strBuffer);
					continue;
				}
				if (t.charAt(t.length()-1) == '"') {
					strParsing = false;
					continue;
				}

				if (strParsing) continue;

				tokens.add(t.replace("\t", ""));
			}
		}

		return tokens;
	}
}
