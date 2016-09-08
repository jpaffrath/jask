package jask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static List<String> readFile(String name) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lines;
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("This is the jask interpreter. No input files!");
			return;
		}

		for (int i = 0; i < args.length; i++) {
			Tokenizer tokenizer = new Tokenizer();
			Interpreter interpreter = new Interpreter();
			interpreter.interpret(tokenizer.parse(readFile(args[i])));
		}
	}
}
