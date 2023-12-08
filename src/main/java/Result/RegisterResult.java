package Result;

/**
 * A container for the resulting data from RegisterService to return to RegisterHandler. Indicates success or failure.
 */

public class RegisterResult extends Result {

    /**
     * A string containing the authentication token.
     */
    String authtoken;
    /**
     * username of the newly registered user.
     */
    String username;
    /**
     * The unique (Person) ID of the person who is registering.
     */
    String personID;

    public RegisterResult() {
        authtoken = null;
        username = null;
        personID = null;
    }
    public RegisterResult(String authtoken, String username, String personID) {
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
