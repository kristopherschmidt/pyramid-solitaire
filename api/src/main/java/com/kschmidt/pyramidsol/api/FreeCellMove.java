package com.kschmidt.pyramidsol.api;


public class FreeCellMove implements Move {

	private Position from;
	
	public FreeCellMove(Position from) {
		this.from = from;
	}
	
	public Position getFrom() {
		return from;
	}
	
	@Override
	public void execute(Board board) {
		if (board.isFreeCellEmpty()) {
			from.removeCard(board);
			board.setFreeCell(from.getCard());
		} else {
			throw new IllegalStateException("Cannot move: "+from+" to occupied freecell: "+board.getFreeCell());
		}
	}
	
	@Override
	public void undo(Board board) {
		Card freeCell = board.getFreeCell();
		from.addCard(freeCell, board);
		board.removeFreeCell();
	}
	
	public String toString() {
		return "ToFreeCell - "+from;
	}
	
	public String getTypeName() {
		return "FreeCellMove";
	}

}
