package com.kschmidt.pyramidsol.web.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kschmidt.pyramidsol.api.Board;
import com.kschmidt.pyramidsol.api.Deck;
import com.kschmidt.pyramidsol.api.Game;

@Repository
public class GameRepository {

	private Map<String, Game> games = new HashMap<String, Game>();
	private long randomGameId = 0;

	public Game getGame(String id) throws IOException {
		Game game = games.get(id);
		if (game == null) {
			Board board = new Board();
			board.loadBoard("games/" + id + ".pys");
			game = new Game(id, board);
			games.put(id, game);
		}
		return game;
	}

	public Game resetGame(String id) throws IOException {
		games.remove(id);
		return getGame(id);
	}
	
	public Game getRandomGame() {
		randomGameId += 1;
		String id = Long.toString(randomGameId);
		Game game = new Game(id, new Board(new Deck()));
		games.put(id, game);
		return game;
	}

}
