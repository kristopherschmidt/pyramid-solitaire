package com.kschmidt.pyramidsol.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game {

	private Board board;
	private String id;
	private List<Move> legalMoves = new ArrayList<Move>();
	private MoveGenerator moveGenerator;
	private Stack<Move> moves = new Stack<Move>();

	public Game(String id, Board board) {
		this.id = id;
		this.board = board;
		this.moveGenerator = new MoveGenerator(board);
		this.legalMoves = moveGenerator.getAllMoves();
	}

	public Board getBoard() {
		return this.board;
	}

	public String getId() {
		return id;
	}
	
	public List<Move> getLegalMoves() {
		return legalMoves;
	}

	public Stack<Move> getMoves() {
		return moves;
	}

	public void execute(Move move) {
		if (!getIsOver()) {
			move.execute(board);
			moves.add(move);
			legalMoves = moveGenerator.getAllMoves();
		}
	}

	public void undo() {
		if (!moves.isEmpty()) {
			Move lastMove = moves.pop();
			lastMove.undo(board);
			legalMoves = moveGenerator.getAllMoves();
		}
	}

	public void undoAll() {
		while (!moves.isEmpty()) {
			undo();
		}
	}

	public boolean getIsOver() {
		return legalMoves.isEmpty() || board.getPyramidCardCount() == 0;
	}

}
