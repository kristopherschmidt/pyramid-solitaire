package com.kschmidt.pyramidsol;


public interface Move {

	public abstract void execute(Board board);

	public abstract void undo(Board board);
	
	//for JSON serialization
	public abstract String getTypeName();

}