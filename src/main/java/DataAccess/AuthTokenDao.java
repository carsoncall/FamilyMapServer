package DataAccess;

import Model.AuthToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains all methods for interactions with the SQLite database for the AuthToken table.
 */

public class AuthTokenDao {

    /**
     * The connection that the class will use to access the database.
     */
    private final Connection conn;

    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds the authentication token to the database, with the associated username
     * @param authToken the generated authentication token model object
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO authtoken (authtoken, username) VALUES (?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an authtoken into the database");
        }
    }
    /**
     * Returns the username associated with an authentication token. If none exists, return null.
     * @param authToken the authentication token of the user.
     */
    public String find(String authToken) throws DataAccessException {
        ResultSet rs;
        String username = null;
        String sql = "SELECT * FROM authtoken WHERE authtoken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a username in the authtoken table");
        }
        return username;
    }

    /**
     * Clears the database of all authentication tokens.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM authtoken";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the authtoken table");
        }
    }

}
