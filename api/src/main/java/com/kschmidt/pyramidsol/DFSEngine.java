package com.kschmidt.pyramidsol;

import java.util.List;
import java.util.Stack;

public class DFSEngine {

	private Board board;
	private int depth;
	private MoveGenerator moveGenerator;
	private DFSEngineParams engineParams;

	public DFSEngine(Board board, int depth, DFSEngineParams engineParams) {
		this.board = board;
		this.depth = depth;
		this.moveGenerator = new MoveGenerator(board);
		this.engineParams = engineParams;
	}

	public DFSEngine(Board board, int depth) {
		this(board, depth, new DFSEngineParams());
	}

	public boolean play() {
		Evaluation bestMove = null;
		do {
			bestMove = findBestMove(board, new Stack<Move>(), depth);
			if (!bestMove.getMoves().isEmpty()) {
				bestMove.getMoves().get(0).execute(board);
				logMove(bestMove);
			}
		} while (!bestMove.getMoves().isEmpty());
		logFinalBoard();
		return board.getPyramidCardCount() == 0;
	}

	public Evaluation findBestMove(Board board, Stack<Move> moves, int depth) {
		// System.out.println("RECURSE findBestMove(" + moves + ", " + depth +
		// ")");
		if (depth == 0) {
			return new Evaluation(moves, evaluateBoard(board, moves));
		}
		List<Move> allMoves = moveGenerator.getAllMoves();
		if (allMoves.isEmpty()) {
			return new Evaluation(moves, evaluateBoard(board, moves));
		}
		Evaluation best = new Evaluation(null, Integer.MIN_VALUE);
		if (depth == 0 || allMoves.isEmpty()) {
			best = new Evaluation(moves, evaluateBoard(board, moves));
		} else {
			for (int i = 0; i < allMoves.size(); ++i) {
				Move move = allMoves.get(i);
				moves.push(move);
				Evaluation evaluation;
				if (move instanceof DeckMove) {
					// not allowed to execute DeckMove, would be cheating
					evaluation = findBestMove(board, moves, 0);
				} else {
					move.execute(board);
					evaluation = findBestMove(board, moves, depth - 1);
					move.undo(board);
				}
				if (evaluation.getScore() > best.getScore()) {
					best = evaluation;
				}
				moves.pop();
			}
		}
		return best;
	}

	private float evaluateBoard(Board board, Stack<Move> moves) {
		if (board.getPyramidCardCount() == 0) {
			return Integer.MAX_VALUE;
		}
		float score = 52;
		boolean deckMove = !moves.isEmpty() && moves.peek() instanceof DeckMove;
		// base score for pyramid + talon
		score += engineParams.getPyramidCardScore()
				* board.getPyramidCardCount()
				+ engineParams.getTalonCardScore()
				* board.getTalon().getCardCount();
		// penalty for moving to freecell, a move to the freecell needs to be
		// justified by lower scores later
		// in addition, moving to freecell removes the card from the pyramid or
		// talon, so must be penalized at least -1
		// or it would look like a freebie.
		// TODO greater search depth when freecell move is made?
		if (!board.isFreeCellEmpty()) {
			score -= engineParams.getFreeCellScore(); // 1 since the card is
														// still on the board. 4
														// means 2
			// matches (or maybe 1 more?) need to be made to
			// justify.
			// also a deckmove adds 3 to the board, make free cell move slightly
			// worse than deck move?
		}
		// if choosing to play a DeckMove, we cannot see the specific cards but
		// know there
		// will be 3 more on the board as a result
		if (deckMove) {
			score -= engineParams.getDeckMoveScore();
		}
		
		score += getPyramidCoveragePenalty(board);
		
		return score;
	}

	private float getPyramidCoveragePenalty(Board board) {
		List<Position> pyramidBottom = board.getPyramidBottomPositions();
		int covered = 0;
		for (int i = 0; i < pyramidBottom.size(); ++i) {
			PyramidPosition p = (PyramidPosition)pyramidBottom.get(i);
			covered += board.getCardsCoveredCount(p.getRowNumber(), p.getColumnNumber());
		}
		return (float)covered / (float)77;
	}

	private void logFinalBoard() {
		if (engineParams.isLog()) {
			System.out.println("FINAL BOARD: " + board);
		}
	}

	private void logMove(Evaluation bestMove) {
		if (engineParams.isLog()) {
			System.out.println();
			System.out.println("BEST MOVE: " + bestMove.getMoves().get(0)
					+ "-->" + bestMove.getScore());
			System.out.println(bestMove.getMoves());
			System.out.println(board);
		}
	}

	public static final class Evaluation {
		private Stack<Move> moves;
		private float score;

		public Evaluation(Stack<Move> moves, float score) {
			this.moves = new Stack<Move>();
			if (moves != null) {
				this.moves.addAll(moves);
			}
			this.score = score;
		}

		public Stack<Move> getMoves() {
			return moves;
		}

		public float getScore() {
			return score;
		}

		public String toString() {
			return moves + " -> " + score;
		}
	}

}
