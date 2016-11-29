package jask;

public class History {
	private int historyMax;
	private String history[];
	private int historyIndex;

	public History(int historyMax) {
		this.historyMax = historyMax;
		this.history = new String[this.historyMax];
		this.historyIndex = 0;
	}

	/**
	 * Adds a line to the history
	 *
	 * @param line line of jask code
	 */
	public void addToHistory(String line) {
		if (line.isEmpty()) return;

		// if the history is full, clear it
		if (this.historyIndex + 1 > this.historyMax) {
			this.history = new String[this.historyMax];
			this.historyIndex = 0;
		}

		this.history[this.historyIndex++] = line;
	}

	/**
	 * Prints the history to the standard out
	 */
	public void printHistory() {
		for (int i = 0; i < this.historyIndex; i++) {
			System.out.println("[" + (i + 1) +"]" + " " + this.history[i]);
		}
	}

	public void printHistoryDESC() {
		for (int i = this.historyIndex-1; i >= 0; i--) {
			System.out.println("[" + (i + 1) +"]" + " " + this.history[i]);
		}
	}
}