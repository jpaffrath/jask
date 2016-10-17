package jask;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer class
 *
 * @author Julius Paffrath
 *
 */
public class Tokenizer {

	/**
	 * Splits the given input lines into tokens
	 *
	 * @param lines content of file as lines
	 * @return parsed list of tokens
	 */
	List<String> parse(List<String> lines) {
		List<String> tokens = new ArrayList<String>();
		String strBuffer = null;
		boolean strParsing = false;
		boolean commentParsing = false;

		for (String line : lines) {
			line = line.replace("\t", "");
			if (line.equals("")) continue;
			if (line.startsWith("//")) continue;

			for (String t : line.split(" ")) {
				// toggle comment parsing
				if (t.contentEquals("*/") && commentParsing) {
					commentParsing = false;
					continue;
				}

				if (commentParsing) continue;

				if (t.contentEquals("/*") && !commentParsing) {
					commentParsing = true;
					continue;
				}

				// toggle string parsing
				if (t.contains("\"") && t.indexOf('"') == t.lastIndexOf('"')) {
					if (!strParsing) {
						strParsing = true;
						strBuffer = new String(t + " ");
						continue;
					}
					else {
						strParsing = false;
						strBuffer += t;
						tokens.add(strBuffer);
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