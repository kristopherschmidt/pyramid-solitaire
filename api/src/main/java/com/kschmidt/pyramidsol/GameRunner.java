package com.kschmidt.pyramidsol;

public class GameRunner {

	public GameRunnerResults runGames(int numGames, int depth, boolean log) {
		return this.runGames(numGames, depth, log, new DFSEngineParams());
	}

	public GameRunnerResults runGames(int numGames, int depth, boolean log,
			DFSEngineParams engineParams) {
		GameRunnerResults results = new GameRunnerResults(engineParams);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < numGames; ++i) {
			Board b = new Board(new Deck());
			DFSEngine e = new DFSEngine(b, depth, engineParams);
			if (log) {
				System.out.println("Game: " + (i+1));
				System.out.println(b);
			}
			boolean won = e.play();
			if (log) {
				System.out.println("Game: " + (i+1) + ": "
						+ (won ? "W" : "L"));
			}
			results.add(won);
			if (i % 100 == 0) {
				System.out.println(i);
			}
		}
		long stopTime = System.currentTimeMillis();
		results.setTotalGameTime(stopTime - startTime);
		if (log) {
			System.out.println(results);
		}
		return results;
	}

}
