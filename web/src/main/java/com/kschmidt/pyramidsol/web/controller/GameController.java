package com.kschmidt.pyramidsol.web.controller;

import java.io.IOException;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kschmidt.pyramidsol.api.Board;
import com.kschmidt.pyramidsol.api.DFSEngine;
import com.kschmidt.pyramidsol.api.DeckMove;
import com.kschmidt.pyramidsol.api.FreeCellMove;
import com.kschmidt.pyramidsol.api.Game;
import com.kschmidt.pyramidsol.api.Move;
import com.kschmidt.pyramidsol.api.Position;
import com.kschmidt.pyramidsol.api.RemoveCardsMove;
import com.kschmidt.pyramidsol.api.DFSEngine.Evaluation;
import com.kschmidt.pyramidsol.web.model.GameRepository;

@Controller
public class GameController {
	
	private static final Logger LOG = Logger.getLogger(GameController.class);

	@Autowired
	private GameRepository gameRepository;

	@RequestMapping(value = "/games/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Game getBoard(@PathVariable String id) throws IOException {
		return gameRepository.getGame(id);
	}
	
	@RequestMapping(value = "/games/randomGame", method = RequestMethod.POST)
	@ResponseBody
	public Game getRandomGame() {
		return gameRepository.getRandomGame();
	}

	/**
	 * @RequestMapping(value = "/games/{id}/moves", method = RequestMethod.GET)
	 * @ResponseBody public List<Move> getMoves(@PathVariable String id) throws
	 *               IOException { Board board = gameRepository.createGame(id);
	 *               MoveGenerator moveGenerator = new MoveGenerator(board);
	 *               return moveGenerator.getAllMoves(); }
	 */

	@RequestMapping(value = "/games/{id}/moves/deckMove", method = RequestMethod.POST)
	@ResponseBody
	public Game deckMove(@PathVariable String id) throws IOException {
		Game game = gameRepository.getGame(id);
		DeckMove deckMove = new DeckMove();
		game.execute(deckMove);
		return game;
	}

	@RequestMapping(value = "/games/{id}/moves/resetMove", method = RequestMethod.POST)
	@ResponseBody
	public Game resetMove(@PathVariable String id) throws IOException {
		return gameRepository.resetGame(id);
	}

	@RequestMapping(value = "/games/{id}/moves/engineMove", method = RequestMethod.POST)
	@ResponseBody
	public Game engineMove(@PathVariable String id) throws IOException {
		int depth = 10;
		Game game = gameRepository.getGame(id);
		Board board = game.getBoard();
		DFSEngine engine = new DFSEngine(board, depth);
		Evaluation bestMove = engine.findBestMove(board, new Stack<Move>(),
				depth);
		if (!bestMove.getMoves().isEmpty()) {
			game.execute(bestMove.getMoves().get(0));
		}
		return game;
	}
	
	@RequestMapping(value = "/games/{id}/moves/undo", method = RequestMethod.POST)
	@ResponseBody
	public Game undoMove(@PathVariable String id) throws IOException {
		Game game = gameRepository.getGame(id);
		game.undo();
		return game;
	}
	
	@RequestMapping(value = "/games/{id}/moves/undoAll", method = RequestMethod.POST)
	@ResponseBody
	public Game undoAllMoves(@PathVariable String id) throws IOException {
		Game game = gameRepository.getGame(id);
		game.undoAll();
		return game;
	}
	
	@RequestMapping(value = "/games/{id}/moves/removeCards", method = RequestMethod.POST)
	@ResponseBody
	public Game removeCards(@PathVariable String id, @RequestBody String cardsToRemove) throws IOException, JSONException {
		Game game = gameRepository.getGame(id);
		JSONObject o = new JSONObject(cardsToRemove);
		String card1Name = o.getJSONObject("card1").getString("name");
		Position position1 = game.getBoard().getPositionForCard(card1Name);
		Position position2 = null;
		if (o.has("card2")) {
			String card2Name = o.getJSONObject("card2").getString("name");
			position2 = game.getBoard().getPositionForCard(card2Name);
		}
		game.execute(new RemoveCardsMove(position1, position2));
		LOG.debug("Trying to remove: "+ position1 + ", " + position2);
		return game;
	}
	
	@RequestMapping(value = "/games/{id}/moves/freeCell", method = RequestMethod.POST)
	@ResponseBody
	public Game freeCell(@PathVariable String id, @RequestBody String cardToMove) throws IOException, JSONException {
		Game game = gameRepository.getGame(id);
		JSONObject card = new JSONObject(cardToMove);
		Position fromPosition = game.getBoard().getPositionForCard(card.getString("name"));
		game.execute(new FreeCellMove(fromPosition));
		return game;
	}

}
