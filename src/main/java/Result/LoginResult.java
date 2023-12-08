package Result;

/**
 * A container for the resulting data from LoginService to return to LoginHandler. Indicates success or failure.
 */
public class LoginResult extends Result {

    /**
     * the authtoken associated with this login.
     */
    String authtoken;
    /**
     * The username of the user who is logging in.
     */
    String username;
    /**
     * The personID of the person logging in.
     */
    String personID;

    public LoginResult(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
