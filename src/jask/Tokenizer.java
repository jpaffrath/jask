package jask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
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

	/**
	 * Checks if a given character is a valid split character
	 *
	 * @param character character to check
	 * @return true if the given character is a valid split character, otherwise false
	 */
	private boolean isSplitCharacter(char character) {
		switch (character) {
		case 0x20: // whitespace
		case 0x9:  // tab
			return true;
		}

		return false;
	}

	/**
	 * Parses input from given Reader to list of string tokens
	 *
	 * @param reader input
	 * @return list of string tokens
	 */
	private List<String> parseInput(Reader reader) {
		List<String> tokens = new ArrayList<String>();
		StringBuilder tokenBuffer = new StringBuilder();

		Reader buffer = new BufferedReader(reader);

		boolean insideString = false;
		boolean insideComment = false;
		boolean lastWasSplit =  false;

		int val;
		char curChar;

		try {
			while ((val = buffer.read()) != -1) {
				curChar = (char)val;

				// skip special characters
				switch (curChar) {
				case 0x0D:
					continue;
				}

				// continue if multiple separator characters are detected
				if (this.isSplitCharacter(curChar) && lastWasSplit && !insideString) {
					continue;
				}

				// toggle comment parsing
				if (curChar == ';' && !insideComment && !insideString) {
					insideComment = true;
				}

				// turn newline to space and stop comment parsing
				if (curChar == '\n') {
					curChar = ' ';
					insideComment = false;
				}

				if (insideComment) continue;

				// toggle string parsing
				if (curChar == '"') {
					insideString = !insideString;
				}

				// split sequence to token
				if (this.isSplitCharacter(curChar) && !insideString) {
					tokens.add(tokenBuffer.toString());
					tokenBuffer = new StringBuilder();
					lastWasSplit = true;
					continue;
				}

				tokenBuffer.append(curChar);
				lastWasSplit = false;
			}
		} catch (IOException e) { e.printStackTrace(); }

		tokens.add(tokenBuffer.toString());

		try { buffer.close(); } catch (IOException e) { e.printStackTrace(); }
		return tokens;
	}

	/**
	 * Splits the given input file into tokens
	 *
	 * @param file a jask file as input
	 * @return parsed list of tokens
	 */
	List<String> parse(File file) {
		if (!Helpers.checkFile(file)) return null;

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Error.printErrorFileNotFound(file.getName());
			return null;
		}

		return this.parseInput(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
	}

	/**
	 * Splits the given input line into tokens
	 *
	 * @param input a line of jask code
	 * @return parsed list of tokens
	 */
	List<String> parse(String input) {
		return this.parseInput(new StringReader(input));
	}

	/**
	 * Splits the given input list into tokens
	 *
	 * @param inputList list of jask code
	 * @return parsed list of tokens
	 */
	List<String> parse(List<String> inputList) {
		List<String> tokens = new ArrayList<String>();

		for (String line : inputList) {
			tokens.addAll(this.parse(line));
		}

		return tokens;
	}
}