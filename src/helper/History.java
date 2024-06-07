package helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages input history
 *
 * @author Julius Paffrath
 *
 */
public class History {
	private final List<String> history;
	private final int historyMax;

	/**
	 * Standard constructor
	 *
	 * @param max maximum number of stored history lines
	 */
	public History(int max) {
		this.historyMax = max;
		this.history = new ArrayList<>(max);
	}

	/**
	 * Adds a line to the history
	 *
	 * @param line line of jask code
	 */
	public void addToHistory(String line) {
		if (line.isEmpty()) {
			return;
		}

		if (history.size() >= historyMax) {
			history.remove(0);
		}

		history.add(line);
	}

	/**
	 * Prints the history to the standard out
	 */
	public void printHistory() {
		printHistory(false);
	}

	/**
	 * Prints the history descending to the standard out
	 */
	public void printHistoryDESC() {
		printHistory(true);
	}
	
	private void printHistory(boolean descending) {
		if (descending) {
			for (int i = history.size() -1; i >= 0; i--) {
				System.out.println("[" + (i + 1) + "] " + history.get(i));
			}
		}
		else {
			for (int i = 0; i < history.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + history.get(i));
            }
		}
	}
}