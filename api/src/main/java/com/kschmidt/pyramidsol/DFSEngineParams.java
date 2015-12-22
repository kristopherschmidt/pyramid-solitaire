package com.kschmidt.pyramidsol;

public class DFSEngineParams {

	private float pyramidCardScore = -1;
	private float talonCardScore = -1;
	private float deckMoveScore = 3;
	private float freeCellScore = 3;
	private boolean log = false;

	public DFSEngineParams() {

	}

	public DFSEngineParams(float pyramidCardScore, float talonCardScore,
			float deckMoveScore, float freeCellScore, boolean log) {
		super();
		this.pyramidCardScore = pyramidCardScore;
		this.talonCardScore = talonCardScore;
		this.deckMoveScore = deckMoveScore;
		this.freeCellScore = freeCellScore;
		this.log = log;
	}
	
	public DFSEngineParams(float pyramidCardScore, float talonCardScore,
			float deckMoveScore, float freeCellScore) {
		this(pyramidCardScore, talonCardScore, deckMoveScore, freeCellScore, false);
	}

	public float getPyramidCardScore() {
		return pyramidCardScore;
	}

	public void setPyramidCardScore(float pyramidCardScore) {
		this.pyramidCardScore = pyramidCardScore;
	}

	public float getTalonCardScore() {
		return talonCardScore;
	}

	public void setTalonCardScore(float talonCardScore) {
		this.talonCardScore = talonCardScore;
	}

	public float getDeckMoveScore() {
		return deckMoveScore;
	}

	public void setDeckMoveScore(float deckMoveScore) {
		this.deckMoveScore = deckMoveScore;
	}

	public float getFreeCellScore() {
		return freeCellScore;
	}

	public void setFreeCellScore(float freeCellScore) {
		this.freeCellScore = freeCellScore;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	@Override
	public String toString() {
		return "DFSEngineParams [pyramidCardScore=" + pyramidCardScore
				+ ", talonCardScore=" + talonCardScore + ", deckMoveScore="
				+ deckMoveScore + ", freeCellScore=" + freeCellScore + "]";
	}

}
