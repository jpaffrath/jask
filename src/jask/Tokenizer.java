package jask;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import helper.Error;
import helper.Helpers;

/**
 * Tokenizer class
 *
 * @author Julius Paffrath
 */
public class Tokenizer {

    /**
     * Checks if a given character is a valid split character.
     *
     * @param character character to check
     * @return true if the given character is a valid split character, otherwise false
     */
    private boolean isSplitCharacter(char character) {
        return character == ' ' || character == '\t';
    }

    /**
     * Parses input from given Reader to list of string tokens.
     *
     * @param reader input
     * @return list of string tokens
     */
    private List<String> parseInput(Reader reader) {
        List<String> tokens = new ArrayList<>();
        StringBuilder tokenBuffer = new StringBuilder();
        
        try (BufferedReader buffer = new BufferedReader(reader)) {
            boolean insideString   = false;
            boolean insideComment  = false;
            boolean insideFunction = false;
            boolean lastWasSplit   = false;
            int val;

            while ((val = buffer.read()) != -1) {
            	char curChar = (char)val;

                if (curChar == '\r') {
                	continue;
                }
                
                // continue if multiple separator characters are detected
                if (isSplitCharacter(curChar) && lastWasSplit && !insideString) {
                	continue;
                }
                
                // toggle comment parsing
                if (curChar == ';' && !insideComment && !insideString) {
                	insideComment = true;
                }
                
                // toggle function parsing
                if (curChar == '(' && !insideComment && !insideString) {
                	insideFunction = true;
                }
                
                if (curChar == ')' && !insideComment && !insideString) {
                	insideFunction = false;
                }
                
                if (isSplitCharacter(curChar) && insideFunction && !insideString) {
                	continue;
                }
                
                // turn newline to space and stop comment parsing
                if (curChar == '\n') {
                	curChar = ' ';
                	insideComment = false;
                }
                
                if (insideComment) {
                	continue;
                }
                
                // toggle string parsing
                if (curChar == '"') {
                	insideString = !insideString;
                }
                
                // split sequence to token
                if (isSplitCharacter(curChar) && !insideString) {
                    if (tokenBuffer.length() > 0) {
                        tokens.add(tokenBuffer.toString());
                        tokenBuffer.setLength(0);
                    }
                    lastWasSplit = true;
                    continue;
                }

                tokenBuffer.append(curChar);
                lastWasSplit = false;
            }

            if (tokenBuffer.length() > 0) {
                tokens.add(tokenBuffer.toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;
    }

    /**
     * Splits the given input file into tokens.
     *
     * @param file a jask file as input
     * @return parsed list of tokens
     */
    public List<String> parse(File file) {
        if (Helpers.checkFile(file) == false) {
            return null;
        }
        
        try (InputStream inputStream = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return parseInput(reader);
        }
        catch (FileNotFoundException e) {
        	Error.printErrorFileNotFound(file.getName());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Splits the given input line into tokens.
     *
     * @param input a line of jask code
     * @return parsed list of tokens
     */
    public List<String> parse(String input) {
        return parseInput(new StringReader(input));
    }

    /**
     * Splits the given input list into tokens.
     *
     * @param inputList list of jask code
     * @return parsed list of tokens
     */
    public List<String> parse(List<String> inputList) {
        List<String> tokens = new ArrayList<>();

        for (String line : inputList) {
            tokens.addAll(parse(line));
        }

        return tokens;
    }
}