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

/**
 * Queries and creates users in the database. Stores user info.
 */
public class User {
	private final int id;
	private final String username;
	private final String fullName;
	private final UserType userType;

	public enum UserType {
		STUDENT, FACULTY;

		/**
		 * Returns user type from ordinal.
		 * 
		 * @param ordinal
		 * @return
		 */
		public static UserType getUserType(int ordinal) {
			return values()[ordinal];
		}
	}

	/**
	 * Constructs new object.
	 * 
	 * @param id       id of user
	 * @param username username of user
	 * @param fullName full name of user
	 * @param userType user type of user
	 */
	public User(int id, String username, String fullName, UserType userType) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
		this.userType = userType;
	}

	/**
	 * Returns User object from provided credentials.
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Returns User object from provided credentials after creating user in
	 * database. Will indicate in database that user is a student.
	 * 
	 * @param username
	 * @param password
	 * @param fullName
	 * @param year
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Returns User object from provided credentials after creating user in
	 * database. Will indicate in database that user is a faculty member.
	 * 
	 * @param username
	 * @param password
	 * @param fullName
	 * @return
	 * @throws SQLException
	 */
	public static User create_faculty(String username, String password, String fullName) throws SQLException {
		User user = create_user(username, password, fullName, UserType.FACULTY);
		if (user != null) {
			Connection conn = Database.getConnection();

			// Creates new Student that references the User created above.
			String statement = "insert into FacultyMember (user_id) values (?)";
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, user.getId());

			stmt.executeUpdate();
		}

		return user;
	}

	/**
	 * Returns User object from provided credentials after creating user in
	 * database.
	 * 
	 * @param username
	 * @param password
	 * @param fullName
	 * @param userType
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Returns random salt.
	 * 
	 * @return
	 */
	private static byte[] generateRandomSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	/**
	 * Returns salt associated with username in database.
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Hashes password using salt.
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
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

	/**
	 * Returns id of user.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns username of user.
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns full name of user.
	 * 
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Returns user type of user.
	 * 
	 * @return
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * Adds user as friend of current user to the database.
	 * 
	 * @param currentUser
	 */
	public void addAsFriend(User currentUser) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into Following (from_id, to_id) values (?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, id);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determines if this object is equal to the object passed as an argument.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id;
	}
}
