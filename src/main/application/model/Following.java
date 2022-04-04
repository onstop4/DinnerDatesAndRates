package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Following {
private final int fromID;
private final int toID;

public Following(int fromID, int toID){
	this.fromID = fromID;
	this.toID = toID;
	}

public int getFromID() {
	return fromID;
}

public int getToID() {
	return toID;
}

public void addFollowing(UserModel userModel) {
	try (Connection conn = Database.getConnection()) {
		String statement = "insert into Following (from_id, to_id) values (?, ?)";

		PreparedStatement stmt = conn.prepareStatement(statement);
		stmt.setInt(1, userModel.getId());
		stmt.setInt(2, toID);

		stmt.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
	}
}

}
