package jask;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	List<String> parse(List<String> lines) {
		List<String> tokens = new ArrayList<String>();
		String strBuffer = null;
		boolean strParsing = false;

		for (String line : lines) {
			if (line.equals("")) continue;

			for (String t : line.split(" ")) {
				if (t.contains("\"") && t.indexOf('"') == t.lastIndexOf('"')) {
					if (!strParsing) {
						strParsing = true;
						strBuffer = new String(t + " ");
						continue;
					}
					else {
						strParsing = false;
						strBuffer += t;
						tokens.add(strBuffer.replace("\t", ""));
						strBuffer = null;
						continue;
					}
				}

				if (strParsing) {
					strBuffer += (t + " ");
					continue;
				}

				tokens.add(t.replace("\t", ""));
			}
		}

		return tokens;
	}
}