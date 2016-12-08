package jask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer class
 *
 * @author Julius Paffrath
 *
 */
public class Tokenizer {

	List<String> parse(String filename) throws IOException {
		if (!Helpers.checkFile(filename)) return null;

		List<String> tokens = new ArrayList<String>();
		InputStream inputStream = new FileInputStream(new File(filename));
		Reader buffer = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

		StringBuilder tokenBuffer = new StringBuilder();
		boolean insideString = false;

		int val;
		char curChar;
		while ((val = buffer.read()) != -1) {
			curChar = (char)val;

			if (curChar == '\n') {
				curChar = ' ';
			}

			if (curChar == '"') {
				insideString = !insideString;
			}

			if (curChar == ' ' && !insideString) {
				tokens.add(tokenBuffer.toString());
				tokenBuffer = new StringBuilder();
				continue;
			}

			tokenBuffer.append(curChar);
		}

		tokens.add(tokenBuffer.toString());
		buffer.close();

		return tokens;
	}

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
			line = line.trim();
			if (line.equals("")) continue;
			if (line.startsWith("//")) continue;

			for (String t : line.split(" ")) {
				// toggle comment parsing
				if (t.contentEquals("/*") && !commentParsing) {
					commentParsing = true;
					continue;
				}

				if (t.contentEquals("*/") && commentParsing) {
					commentParsing = false;
					continue;
				}

				if (commentParsing) continue;

				// toggle string parsing
				if (t.contains("\"") && (t.length() - t.replace("\"", "").length()) % 2 != 0) {
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