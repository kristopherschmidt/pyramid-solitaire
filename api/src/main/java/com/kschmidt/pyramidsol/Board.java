package com.kschmidt.pyramidsol;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Board {

	private Deck deck = new Deck();
	private Card[][] pyramid = new Card[7][7];
	private Talon talon = new Talon();
	private Card freeCell;

	public Board() {

	}

	// create a new board from cards in the deck
	public Board(Deck deck) {
		this.deck = deck;
		deck.shuffle();
		for (int i = 0; i < pyramid.length; ++i) {
			for (int j = 0; j <= i; ++j) {
				Card card = deck.drawCard();
				pyramid[i][j] = card;
			}
		}
	}

	public Deck getDeck() {
		return deck;
	}

	public Talon getTalon() {
		return talon;
	}

	public void setTalon(Talon talon) {
		this.talon = talon;
	}

	public Card[][] getPyramid() {
		return pyramid;
	}

	public Card getPyramid(int row, int column) {
		return pyramid[row - 1][column - 1];
	}

	public void setPyramid(Card card, int row, int column) {
		pyramid[row - 1][column - 1] = card;
	}

	public void setPyramid(String cardString, int row, int column) {
		this.setPyramid(new Card(cardString), row, column);
	}

	public void loadBoard(String filename) throws IOException {
		File file = new File(this.getClass().getClassLoader()
				.getResource(filename).getFile());
		System.out.println("Loading file: " + file);
		List<String> lines = FileUtils.readLines(file);

		for (int i = 0; i < lines.size(); ++i) {
			String line = lines.get(i);
			System.out.println(line);
			line = line.trim();
			if (i < 7) {
				readPyramidLine(i, line);
			}
			if (i == 8) {
				readDeckLine(line);
			}

		}
	}

	private void readPyramidLine(int i, String line) {
		String[] cardStrings = line.split("--");
		for (int j = 0; j < cardStrings.length; ++j) {
			if (!StringUtils.equals("**", cardStrings[j])) {
				Card card = deck.drawCard(cardStrings[j]);
				setPyramid(card, i + 1, j + 1);
			} else {
				setPyramid((Card) null, i + 1, j + 1);
			}
		}
	}

	private void readDeckLine(String line) {
		String[] cardStrings = line.split("-");
		// sanity check the deck line. note this doesn't check for 24 cards
		// because we might be loading a game in progress
		Set<String> deckCards = new HashSet<String>();
		deckCards.addAll(Arrays.asList(cardStrings));
		if (deckCards.size() != cardStrings.length) {
			throw new IllegalStateException(
					"Deck does not contain distinct cards");
		}
		for (int i = 0; i < cardStrings.length; ++i) {
			deck.setCard(i + 1, cardStrings[i]);
		}
	}

	@Transient
	public List<Card> getPyramidBottom() {
		List<Card> bottomCards = new ArrayList<Card>();
		for (int i = pyramid.length - 1; i >= 0; --i) {
			for (int j = 0; j <= i; ++j) {
				Card c = pyramid[i][j];
				// System.out.println("bottom card "+i+","+j+","+c);
				if (c != null) {
					if (i == 6) {
						bottomCards.add(c);
					} else if (pyramid[i + 1][j] == null
							&& pyramid[i + 1][j + 1] == null) {
						bottomCards.add(c);
					}
				}

			}
		}
		return bottomCards;
	}

	@Transient
	public List<Position> getPyramidBottomPositions() {
		List<Position> bottomPositions = new ArrayList<Position>();
		for (int i = pyramid.length - 1; i >= 0; --i) {
			for (int j = 0; j <= i; ++j) {
				Card c = pyramid[i][j];
				// System.out.println("bottom card "+i+","+j+","+c);
				if (c != null) {
					if (i == 6) {
						bottomPositions
								.add(new PyramidPosition(c, i + 1, j + 1));
					} else if (pyramid[i + 1][j] == null
							&& pyramid[i + 1][j + 1] == null) {
						bottomPositions
								.add(new PyramidPosition(c, i + 1, j + 1));
					}
				}

			}
		}
		return bottomPositions;
	}

	public boolean equals(Object o) {
		if (o instanceof Board) {
			Board rhs = (Board) o;
			return new EqualsBuilder().append(this.pyramid, rhs.pyramid)
					.isEquals();
		}
		return false;

		// TODO loader does not take remaining deck into account for boards that
		// already have moves removed + cards
		// no longer in the deck
		// return EqualsBuilder.reflectionEquals(this, o);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Board: " + this.hashCode());
		b.append(System.getProperty("line.separator"));
		for (int i = 0; i < pyramid.length; ++i) {
			int numspaces = 2 * (pyramid.length - 1 - i);
			b.append(StringUtils.repeat(" ", numspaces));
			for (int j = 0; j <= i; ++j) {
				if (pyramid[i][j] != null) {
					b.append(pyramid[i][j]);
				} else {
					b.append("**");
				}
				if (j < i) {
					b.append("--");
				}
			}
			b.append(System.getProperty("line.separator"));
		}
		b.append(System.getProperty("line.separator"));
		b.append(freeCell != null ? freeCell : "**");
		b.append(System.getProperty("line.separator"));
		b.append(System.getProperty("line.separator"));
		b.append(talon);
		b.append(System.getProperty("line.separator"));
		b.append(System.getProperty("line.separator"));
		b.append(deck);
		return b.toString();
	}

	public void drawCardsToTalon() {
		Card card1 = deck.drawCard();
		Card card2 = deck.drawCard();
		Card card3 = deck.drawCard();
		talon.addCards(card1, card2, card3);
	}

	public boolean isFreeCellEmpty() {
		return freeCell == null;
	}

	public Card getFreeCell() {
		return freeCell;
	}

	public Position getFreeCellPosition() {
		return new FreeCellPosition(getFreeCell());
	}

	public void removeFreeCell() {
		freeCell = null;
	}

	public void setFreeCell(Card freeCell) {
		this.freeCell = freeCell;
	}

	public boolean isDeckEmpty() {
		return getDeck().size() == 0;
	}

	// TODO implement with a running count for efficiency?
	public int getPyramidCardCount() {
		int count = 0;
		for (int i = 0; i < pyramid.length; ++i) {
			for (int j = 0; j <= i; ++j) {
				if (pyramid[i][j] != null) {
					count++;
				}
			}
		}
		return count;
	}

	public int getCardsCoveredCount(int row, int column) {
		return getCardsCovered(row, column).size();
	}

	public Set<String> getCardsCovered(int row, int column) {
		Set<String> covered = new HashSet<String>();
		if (row == 1) {
			return covered;
		}
		// all columns other than the rightmost have an upperright card
		if (column < row) {
			covered.add((row - 1) + "," + column);
			covered.addAll(getCardsCovered(row - 1, column));
		}
		// all columns other than the leftmost have an upperleft card
		if (column > 1) {
			covered.add((row - 1) + "," + (column - 1));
			covered.addAll(getCardsCovered(row - 1, column - 1));
		}
		return covered;
	}
	
	public Position getPositionForCard(String cardName) {
		List<Position> pyramidBottom = getPyramidBottomPositions();
		for (int i = 0; i < pyramidBottom.size(); ++i) {
			Position p = pyramidBottom.get(i);
			if (StringUtils.equals(cardName, p.getCard().getName())) {
				return p;
			}
		}
		List<Position> talonTop = getTalon().getTopCardPositions();
		for (int i = 0; i < talonTop.size(); ++i) {
			Position p = talonTop.get(i);
			if (StringUtils.equals(cardName, p.getCard().getName())) {
				return p;
			}
		}
		Position p = getFreeCellPosition();
		if (p != null && StringUtils.equals(cardName, p.getCard().getName())) {
			return p;
		}
		return null;
	}

}
