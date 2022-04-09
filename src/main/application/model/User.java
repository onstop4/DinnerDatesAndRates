package main.application.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
	private int id;
	private String username;
	private String fullName;

	public User(int id, String username, String fullName) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
	}

	public static User get_user(String username, String password) throws SQLException {
		byte[] salt = getSalt(username);

		if (salt == null) {
			return null;
		}

		byte[] passwordBytes = hashPassword(password, salt);

		if (passwordBytes != null) {
			Connection conn = Database.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
					"select User.user_id, User.username, User.full_name from User where lower(username) = lower(?) and password = ?");

			stmt.setString(1, username);
			stmt.setBytes(2, passwordBytes);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("full_name"));
			}
		}

		return null;
	}

	public static User create_user(String username, String password, String fullName) throws SQLException {
		byte[] salt = generateRandomSalt();
		byte[] passwordBytes = hashPassword(password, salt);

		if (passwordBytes != null) {
			Connection conn = Database.getConnection();
			String statement = "insert into User (username, password, salt, full_name) values (?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(statement);

			stmt.setString(1, username);
			stmt.setBytes(2, passwordBytes);
			stmt.setBytes(3, salt);
			stmt.setString(4, fullName);

			stmt.executeUpdate();

			return get_user(username, password);
		}

		return null;
	}

	private static byte[] generateRandomSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private static byte[] getSalt(String username) throws SQLException {
		Connection conn = Database.getConnection();
		String statement = "select salt from User where username = ?";

		PreparedStatement stmt = conn.prepareStatement(statement);
		stmt.setString(1, username);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return rs.getBytes("salt");
		}
		return null;
	}

	private static byte[] hashPassword(String password, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return factory.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.err.println("Cannot encrypt password");
			return null;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean addAsFriend(User currentUser) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into Following (from_id, to_id) values (?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, id);

			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
