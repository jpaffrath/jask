package jask;

import static jask.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import expression.Expression;
import expression.ExpressionType;
import function.Function;
import function.FunctionExecuter;
import function.InternalFunctions;
import helper.Error;
import helper.Helpers;
import helper.Keywords;
import variable.Variable;
import variable.VariableType;

/**
 * Interpreter class
 *
 * @author Julius Paffrath
 *
 */
public class Interpreter {
	private List<String> values;
	private Executer executer;
	
	private char[] invalidChars = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',', '\'', ';', '/', '\\', '!'};

	/**
	 * General constructor
	 */
	public Interpreter() {
		this.values = new ArrayList<String>();
		this.values.add(TRUE);
		this.values.add(FALSE);
		this.values.add(NULL);

		this.executer = new Executer();
	}

	/**
	 * Initializes a new interpreter for function executing
	 *
	 * @param function function to be executed
	 * @param functionExecuter the function executed to be used
	 * @param modules list of modules
	 */
	public Interpreter(Function function, FunctionExecuter functionExecuter, List<Executer> modules, InternalFunctions internalFunctions) {
		this();
		this.executer = new Executer(function.getHeap(), functionExecuter, modules, internalFunctions);
	}

	/**
	 * Initializes a new interpreter for interpreting run statements
	 *
	 * @param executer executer to be used
	 */
	public Interpreter(Executer executer) {
		this();
		this.executer = executer;
	}

	/**
	 * Checks if a given string is a jask value
	 *
	 * @param t string to check
	 * @return true if the given string is a jask value
	 */
	private boolean isValue(String t) {
		return this.values.contains(t);
	}
	
	/**
	 * Checks if a given string is a valid jask variable name
	 * 
	 * @param t string to check
	 * @return true if the given string is a valid jask variable name
	 */
	private boolean isValidVariableName(String t) {
		if (Keywords.isKeyword(t) || Keywords.isOperator(t) || this.isValue(t)) {
			return false;
		}
		
		char toCheck = t.toCharArray()[0];
		
		for (char c : invalidChars) {
			if (toCheck == c) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isValidFunctionCall(String input) {
		Stack<Character> stack = new Stack<>();
        boolean functionNameFound = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (Character.isLetter(currentChar)) {
                functionNameFound = true; // Funktionenamen gefunden
            } else if (currentChar == '(') {
                if (!functionNameFound) {
                    return false; // Öffnende Klammer ohne Funktion
                }
                stack.push(currentChar);
            } else if (currentChar == ')') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return false; // Schließende Klammer ohne öffnende Klammer
                }
                stack.pop();
            } else if (currentChar == ',' || currentChar == ':' || currentChar == '"') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return false; // Trennzeichen außerhalb von Parametern
                }
            }
        }

        return functionNameFound && stack.isEmpty();
    }
	
	/**
	 * Checks if a given string is a jask function
	 *
	 * @param t string to check
	 * @return true if the given string is a jask function
	 */
	public static boolean isFunction(String s) {
		if (s.contains("(") == false) return false;
		if (s.contains(")") == false) return false;

		return isValidFunctionCall(s);
	}

	/**
	 * Returns local executer
	 *
	 * @return executer
	 */
	public Executer getExecuter() {
		return this.executer;
	}
	
	/**
	 * Returns the token at the calculated index or terminates the process if an error occurred
	 * 
	 * @param tokens a list of strings containing tokens
	 * @param curPos the current position in the list
	 * @param nextPos the desired next position
	 * @return token at the calculated index
	 */
	private String getTokenOrDie(List<String> tokens, int curPos, int nextPos) {
		if (tokens.size() <= curPos + nextPos) {
			Error.terminateInterpret("Can't read tokens after '" + tokens.get(curPos) + "'!");
		}
		
		return tokens.get(curPos + nextPos);
	}

	/**
	 * Interprets a given list of tokens
	 *
	 * @param tokens list of tokens
	 * @return result of interpreting
	 */
	public String interpret(List<String> tokens) {
		Expression exp = null;
		String t = null;
		String retVal = "";
		boolean ifRunning = false;

		for (int i = 0; i < tokens.size(); i++) {
			t = this.getTokenOrDie(tokens, i, 0);
			
			if (t.isEmpty()) {
				continue;
			}
			
			if (ifRunning && t.contentEquals("endif")) {
				continue;
			}

			// checks return statement
			if (t.contentEquals("return")) {
				String str = this.getTokenOrDie(tokens, i, 1);
				Variable ret = null;

				// check if the return statement is a function
				if (isFunction(str)) {
					ret = this.executer.executeFunction(str);
				}
				else {
					// check if the return statement is a calculation
					if (tokens.size() > i+2 && Keywords.isCalculation(this.getTokenOrDie(tokens, i, 2))) {
						final String operandName1 = this.getTokenOrDie(tokens, i, 1);
						final String operandName2 = this.getTokenOrDie(tokens, i, 3);

						Variable operand1 = this.executer.hasVariable(operandName1) ? this.executer.getVariableFromHeap(operandName1) : new Variable(operandName1);
						Variable operand2 = this.executer.hasVariable(operandName2) ? this.executer.getVariableFromHeap(operandName2) : new Variable(operandName2);

						ret = this.executer.executeCalculation(operand1, operand2, CalculationType.getType(this.getTokenOrDie(tokens, i, 2)));
					}
					else {
						ret = this.executer.getVariableFromHeap(str);
					}
				}

				// if the return statement is not a function, a calculation or a variable, return the value
				if (ret == null) {
					return str;
				}
				else {
					// wrap string returns
					if (ret.getType() == VariableType.String) {
						return "\"" + ret.toString() + "\"";
					}
					// return string representation of the variable
					return ret.toString();
				}
			}

			// checks store expressions
			else if (t.contentEquals("store") && tokens.size() > i +3 && tokens.get(i+2).contentEquals("in")) {
				// check if the variable name is a keyword
				if (Keywords.isKeyword(this.getTokenOrDie(tokens, i, 1))) {
					Error.terminateInterpret(this.getTokenOrDie(tokens, i, 1) + " is a keyword!");
				}
				
				if (this.isValidVariableName(this.getTokenOrDie(tokens, i, 3)) == false) {
					Error.terminateInterpret(this.getTokenOrDie(tokens, i, 3) + " is not a valid variable name!");
				}

				exp = new Expression(ExpressionType.Store, tokens.subList(i, i+4));
				i += 3;
			}
			// checks assign expressions
			else if (t.contentEquals("assign")) {
				if (Keywords.isOperator(this.getTokenOrDie(tokens, i, 2)) && this.getTokenOrDie(tokens, i, 4).contentEquals("to")) {
					if (Keywords.isKeyword(this.getTokenOrDie(tokens, i, 1))) {
						Error.terminateInterpret(this.getTokenOrDie(tokens, i, 1) + " is a keyword!");
					}

					if (Keywords.isKeyword(this.getTokenOrDie(tokens, i, 3))) {
						Error.terminateInterpret(this.getTokenOrDie(tokens, i, 3) + " is a keyword!");
					}

					if (Keywords.isKeyword(this.getTokenOrDie(tokens, i, 5)) || isValue(this.getTokenOrDie(tokens, i, 5))) {
						Error.terminateInterpret(this.getTokenOrDie(tokens, i, 5) + " is a keyword!");
					}

					exp = new Expression(ExpressionType.Assign, tokens.subList(i, i+6));
					i += 5;
				}
				else {
					if (Keywords.isKeyword(this.getTokenOrDie(tokens, i, 1))) {
						Error.terminateInterpret(this.getTokenOrDie(tokens, i, 1) + " is a keyword!");
					}

					if (Keywords.isKeyword(this.getTokenOrDie(tokens, i, 3)) || isValue(this.getTokenOrDie(tokens, i, 3))) {
						Error.terminateInterpret(this.getTokenOrDie(tokens, i, 3) + " is a keyword!");
					}

					exp = new Expression(ExpressionType.Assign, tokens.subList(i, i+4));
					i += 3;
				}
			}
			// checks function expressions
			else if (isFunction(t)) {
				exp = new Expression(ExpressionType.Function, tokens.subList(i, i+1));
			}
			// checks function define
			else if (t.contentEquals("function") && isFunction(this.getTokenOrDie(tokens, i, 1))) {
				String name = this.getTokenOrDie(tokens, ++i, 0);
				int start = i+1;
				int funcCount = 1;
				int endCount = 0;

				while (true) {
					t = this.getTokenOrDie(tokens, i++, 0);

					if (t.contentEquals("function")) {
						funcCount++;
					}
					else if (t.contentEquals("end")) {
						endCount++;
					}

					if (funcCount == endCount) {
						break;
					}
				}

				this.executer.addFunction(new Function(name, tokens.subList(start, --i)));
				continue;
			}

			// checks if statement
			else if (t.contentEquals("if")) {
				int ifType = 3;
				if (this.getTokenOrDie(tokens, i, 2).contentEquals("mod")) {
					ifType += 2;
				}
				if (this.executer.executeStatement(new Expression(ExpressionType.Statement, tokens.subList(i, i+ifType+1)))) {
					i += ifType;
					ifRunning = true;

				}
				else {
					ifRunning = true;

					int ifCount = 1;
					int elCount = 0;

					while (true) {
						t = this.getTokenOrDie(tokens, ++i, 0);

						if (t.contentEquals("if")) {
							ifCount++;
						}
						else if (t.contentEquals("else")) {
							elCount++;
						}

						if (ifCount == elCount) {
							break;
						}
					}
				}

				continue;
			}

			else if (t.contentEquals("else") && ifRunning) {
				while (!(t = this.getTokenOrDie(tokens, ++i, 0)).equals("endif")) { }
				continue;
			}

			// check run statement
			else if (t.contentEquals("run") || t.contentEquals("while") || t.contentEquals("for")) {
				int runCount = 1;
				int endCount = 0;
				int runner = i;

				while (true) {
					t = this.getTokenOrDie(tokens, ++runner, 0);

					if (t.contentEquals("run") || t.contentEquals("while")) {
						runCount++;
					}
					else if (t.contentEquals("endrun")) {
						endCount++;
					}

					if (runCount == endCount) {
						break;
					}
				}

				exp = new Expression(ExpressionType.Runner, tokens.subList(i, runner+1));
				i = runner;
			}

			// check convert statement
			else if (t.contentEquals("convert")) {
				exp = new Expression(ExpressionType.Convert, tokens.subList(i, i+4));
				i += 3;
			}

			// check increment statement
			else if (t.contentEquals("increment")) {
				exp = new Expression(ExpressionType.Increment, tokens.subList(++i, i+1));
			}

			// check decrement statement
			else if (t.contentEquals("decrement")) {
				exp = new Expression(ExpressionType.Decrement, tokens.subList(++i, i+1));
			}

			// check use statement
			else if (t.startsWith("use")) {
				String moduleName = this.getTokenOrDie(tokens, ++i, 0);
				
				// checks if the use statements tries to load an internal module
				if (InternalFunctions.isInternalModule(moduleName)) {
					this.executer.addInternalModule(moduleName);
					continue;
				}
				
				String module = moduleName + ".jask";

				if (!Helpers.checkFilename(module)) {
					Error.terminateInterpret("Can't find module named '" + module + "'");
				}

				Interpreter moduleInterpreter = new Interpreter();
				moduleInterpreter.interpret(new Tokenizer().parse(new File(module)));
				moduleInterpreter.executer.setName(moduleName);
				this.executer.addModule(moduleInterpreter.executer);
				continue;
			}

			// check module remove statement
			else if (t.startsWith("remove")) {
				String moduleName = this.getTokenOrDie(tokens, ++i, 0);
				this.executer.removeModule(moduleName);
				continue;
			}
			
			// check call statement
			else if (t.contentEquals("call")) {
				exp = new Expression(ExpressionType.Call, tokens.subList(++i, i+2));
				i += 2;
			}
			
			// check struct statement
			else if (t.contentEquals("struct")) {
				int runner = i;

				while (true) {
					t = this.getTokenOrDie(tokens, ++runner, 0);

					if (t.contentEquals("endstruct")) {
						break;
					}
				}

				exp = new Expression(ExpressionType.Struct, tokens.subList(i+1, runner));
				i = runner;
			}

			if (exp != null) {
				retVal = this.executer.execute(exp);
			}
			else {
				Error.terminateInterpret("Token " + t + " cannot be interpreted!");
			}

			exp = null;
			if (retVal.contentEquals("") == false) {
				return retVal;
			}
		}

		return "";
	}
}