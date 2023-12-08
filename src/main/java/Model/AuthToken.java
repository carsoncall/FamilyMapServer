package Model;

import java.util.Objects;

/**
 * This class stores the data from a database row in a Java object.
 */
public class AuthToken {
    /**
     * Stores the authentication token. Cannot be null.
     */
    String authToken;

    /**
     * Stores the username of the associated user's username. Cannot be null.
     */
    String username;

    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return authToken.equals(authToken1.authToken) && username.equals(authToken1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
