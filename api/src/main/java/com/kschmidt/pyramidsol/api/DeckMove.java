package com.kschmidt.pyramidsol.api;


public class DeckMove implements Move {

	@Override
	public void execute(Board board) {
		if (board.getDeck().size() != 0) {
			board.drawCardsToTalon();
		}
	}

	@Override
	public void undo(Board board) {
		for (int i = 3; i >= 1; --i) {
			board.getDeck().addCard(board.getTalon().getTopCard(i));
			board.getTalon().removeCard(i);
		}
	}
	
	public boolean equals(Object o) {
		if (o instanceof DeckMove) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "DeckMove";
	}
	
	public String getTypeName() {
		return "DeckMove";
	}

}
