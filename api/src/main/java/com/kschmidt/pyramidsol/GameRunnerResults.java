package com.kschmidt.pyramidsol;

public class GameRunnerResults {

	private int wonCount = 0;
	private int streak = 0;
	private int longestStreak = 0;
	private int numGames = 0;
	private long totalGameTime;
	private DFSEngineParams engineParams;

	public GameRunnerResults(DFSEngineParams engineParams) {
		this.engineParams = engineParams;
	}

	public void add(boolean won) {
		numGames++;
		if (won) {
			wonCount++;
			streak++;
			if (streak > longestStreak) {
				longestStreak = streak;
			}
		} else {
			streak = 0;
		}
	}

	public int getWonCount() {
		return wonCount;
	}

	public String toString() {
		return "Results: " + wonCount + " / " + (numGames - wonCount) + " / "
				+ longestStreak + " / " + totalGameTime + " - " + engineParams;
	}

	public void setTotalGameTime(long totalGameTime) {
		this.totalGameTime = totalGameTime;
	}

}
