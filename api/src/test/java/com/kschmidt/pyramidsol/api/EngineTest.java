package com.kschmidt.pyramidsol.api;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import com.kschmidt.pyramidsol.api.Board;
import com.kschmidt.pyramidsol.api.DFSEngine;
import com.kschmidt.pyramidsol.api.DFSEngineParams;
import com.kschmidt.pyramidsol.api.Deck;
import com.kschmidt.pyramidsol.api.GameRunner;
import com.kschmidt.pyramidsol.api.GameRunnerResults;

public class EngineTest {

	@Test
	@Ignore
	public void testDFSGetBestMove() throws IOException {
		Board b = new Board();
		b.loadBoard("kswin1.pys");
		DFSEngine e = new DFSEngine(b, 9);
		boolean won = e.play();
		assertTrue(won);
	}

	@Test
	@Ignore
	public void testPlayRandomGames() {
		new GameRunner().runGames(1000, 6, true);
	}

	@Ignore
	@Test
	public void testEffectOfDepthOnGameSpeed() {
		for (int i = 1; i <= 8; ++i) {
			System.out.println("Depth " + i + ": "
					+ new GameRunner().runGames(1000, i, false));
		}
	}

	@Test
	@Ignore
	public void testRandomEngineParams() {
		List<GameRunnerResults> results = new ArrayList<GameRunnerResults>();
		Random random = new Random();
		for (int i = 1; i <= 200; ++i) {
			float pyramidCardScore = random.nextFloat() * -3;
			float talonCardScore = random.nextFloat() * -3;
			float deckMoveScore = random.nextFloat() * 4 + 1;
			float freeCellScore = random.nextFloat() * 4 + 1;
			DFSEngineParams engineParams = new DFSEngineParams(
					pyramidCardScore, talonCardScore, deckMoveScore,
					freeCellScore, false);
			GameRunnerResults result = new GameRunner().runGames(500, 7, false,
					engineParams);
			results.add(result);
			System.out.println("" + i + result);
		}
		Collections.sort(results, new Comparator<GameRunnerResults>() {
			public int compare(GameRunnerResults o1, GameRunnerResults o2) {
				return -1
						* new Integer(o1.getWonCount()).compareTo(new Integer(
								o2.getWonCount()));
			}
		});
		System.out.println("BEST RESULTS");
		for (int i = 0; i < 5; ++i) {
			System.out.println(results.get(i));
		}
	}

	@Test
	@Ignore
	public void testTweakedEngineParams() {
		DFSEngineParams[] params = new DFSEngineParams[] {
			new DFSEngineParams(),
			new DFSEngineParams(-2.4f, -0.85f, 3.6f, 5f),
			new DFSEngineParams(-1.48f, -0.32f, 2.30f, 2.73f),
			new DFSEngineParams(-2.97f, -0.20f, 4.63f, 4.62f),
		};		
		for (int i = 0; i < params.length; ++i) {
			GameRunnerResults results = new GameRunner().runGames(2000, 7,
					false, params[i]);
			System.out.println(i + ": " + results);
		}
	}

	@Test
	@Ignore
	public void testIndividualEngineParams() {
		DFSEngineParams engineParams = new DFSEngineParams(-1.65f, -0.93f,
				2.34f, 3.97f);
		GameRunnerResults results = new GameRunner().runGames(1000, 7, false,
				engineParams);
		System.out.println(results);

	}
	
	@Test
	@Ignore
	public void blah() {
		Board b = new Board(new Deck());
		int count = 0;
		for (int i = 1; i <= 7; ++i) {
			count += b.getCardsCoveredCount(7, i);
		}
		System.out.println(count);
	}

}
