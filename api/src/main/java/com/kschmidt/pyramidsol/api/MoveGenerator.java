package com.kschmidt.pyramidsol.api;

import java.util.ArrayList;
import java.util.List;


public class MoveGenerator {

	private Board board;
	
	public MoveGenerator(Board board) {
		this.board = board;
	}
	
	public List<Move> getRemovalMoves() {
		List<Move> moves = new ArrayList<Move>();
		List<Position> pyramidBottom = board.getPyramidBottomPositions();
		List<Position> talon = board.getTalon().getTopCardPositions();
		List<Position> allPositions = new ArrayList<Position>();
		allPositions.addAll(pyramidBottom);
		allPositions.addAll(talon);
		if (board.getFreeCell() != null) {
			allPositions.add(board.getFreeCellPosition());
		}
		//System.out.println(board);
		//System.out.println(allPositions);
		for (int i = 0; i < allPositions.size(); ++i) {
			if (allPositions.get(i).getCard().getValue() == 13) {
				moves.add(new RemoveCardsMove(allPositions.get(i)));
			}
			for (int j = 0; j < allPositions.size(); ++j) {
				if (i != j) {
					if (allPositions.get(i).getCard().getValue()
							+ allPositions.get(j).getCard().getValue() == 13
							&& allPositions.get(i).getCard().getValue() < allPositions.get(
									j).getCard().getValue()) {
						moves.add(new RemoveCardsMove(allPositions.get(i), allPositions
								.get(j)));
					}
				}
			}
		}
		return moves;
	}

	public List<Move> getDeckMoves() {
		List<Move> deckMoves = new ArrayList<Move>();
		if (!(board.getDeck().size() == 0)) {
			deckMoves.add(new DeckMove());
		}
		return deckMoves;
	}

	public List<Move> getFreeCellMoves() {
		List<Move> freeCellMoves = new ArrayList<Move>();
		if (board.isFreeCellEmpty()) {
			List<Position> pyramidBottom = board.getPyramidBottomPositions();
			List<Position> talon = board.getTalon().getTopCardPositions();
			List<Position> allPositions = new ArrayList<Position>();
			allPositions.addAll(pyramidBottom);
			allPositions.addAll(talon);
			for (int i = 0; i < allPositions.size(); ++i) {
				freeCellMoves.add(new FreeCellMove(allPositions.get(i)));
			}
		}
		return freeCellMoves;
	}

	public List<Move> getAllMoves() {
		List<Move> allMoves = new ArrayList<Move>();
		allMoves.addAll(getRemovalMoves());
		allMoves.addAll(getDeckMoves());
		allMoves.addAll(getFreeCellMoves());
		return allMoves;
	}

}
