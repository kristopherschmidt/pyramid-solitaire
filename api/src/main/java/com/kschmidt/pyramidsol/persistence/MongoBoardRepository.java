package com.kschmidt.pyramidsol.persistence;

import static org.junit.Assert.assertNotNull;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.kschmidt.pyramidsol.Board;
import com.kschmidt.pyramidsol.Card;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class MongoBoardRepository {

	public void save(Board board) throws UnknownHostException {
		MongoClient m = new MongoClient("localhost");
		DB db = m.getDB("pyramidsol");
		// db.createCollection("boards", null);
		DBCollection collection = db.getCollection("boards");
		assertNotNull(collection);
		System.out.println("coll: " + db.getCollectionNames());
		DBObject mongoBoard = new BasicDBObject();
		List<DBObject> pyramidCards = new ArrayList<DBObject>();
		for (int row = 1; row <= 7; ++row) {
			for (int column = 1; column <= row; ++column) {
				Card card = board.getPyramid(row, column);
				if (card != null) {
					DBObject cardPosition = new BasicDBObject();
					cardPosition.put("row", row);
					cardPosition.put("column", column);
					cardPosition.put("card", card.toString());
					pyramidCards.add(cardPosition);
				}
			}
		}
		mongoBoard.put("pyramid", pyramidCards);
		WriteResult result = collection.insert(mongoBoard);
		System.out.println(result);

	}

	public Board loadBoard(String string) {
		throw new RuntimeException("not implemented");
	}

	public void load() throws UnknownHostException {
		MongoClient m = new MongoClient("localhost");
		DB db = m.getDB("pyramidsol");
		System.out.println(db.getCollectionNames());
		DBCollection collection = db.getCollection("boards");
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		System.out.println(cursor);
		System.out.println(collection);
	}

}
