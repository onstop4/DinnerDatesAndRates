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
	private final int id;
	private final String username;
	private final String fullName;
	private final UserType userType;

	public enum UserType {
		STUDENT, FACULTY;

		public static UserType getUserType(int ordinal) {
			return values()[ordinal];
		}
	}

	public User(int id, String username, String fullName, UserType userType) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
		this.userType = userType;
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
					"select User.user_id, User.username, User.full_name, User.user_type from User where lower(username) = lower(?) and password = ?");

			stmt.setString(1, username);
			stmt.setBytes(2, passwordBytes);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("full_name"),
						UserType.getUserType(rs.getInt("user_type")));
			}
		}

		return null;
	}

	public static User create_student(String username, String password, String fullName, int year) throws SQLException {
		User user = create_user(username, password, fullName, UserType.STUDENT);
		if (user != null) {
			Connection conn = Database.getConnection();

			// Creates new Student that references the User created above.
			String statement = "insert into Student (user_id, year) values (?, ?)";
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, user.getId());
			stmt.setInt(2, year);

			stmt.executeUpdate();
		}

		return user;
	}

	public static User create_faculty(String username, String password, String fullName) throws SQLException {
		User user = create_user(username, password, fullName, UserType.FACULTY);
		if (user != null) {
			Connection conn = Database.getConnection();

			// Creates new Student that references the User created above.
			String statement = "insert into Faculty (user_id) values (?)";
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, user.getId());

			stmt.executeUpdate();
		}

		return user;
	}

	private static User create_user(String username, String password, String fullName, UserType userType)
			throws SQLException {
		byte[] salt = generateRandomSalt();
		byte[] passwordBytes = hashPassword(password, salt);

		if (passwordBytes != null) {
			Connection conn = Database.getConnection();

			// Creates new User.
			String statement = "insert into User (username, password, salt, full_name, user_type) values (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(statement);

			stmt.setString(1, username);
			stmt.setBytes(2, passwordBytes);
			stmt.setBytes(3, salt);
			stmt.setString(4, fullName);
			stmt.setInt(5, userType.ordinal());

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

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	public UserType getUserType() {
		return userType;
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
