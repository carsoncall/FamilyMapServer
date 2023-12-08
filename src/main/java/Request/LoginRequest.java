package Request;

/**
 * Container for the request data from the LoginHandler to give to the LoginService.
 */
public class LoginRequest {

    /**
     * Username of the user who is logging in.
     */
    String username;
    /**
     * Password of the user who is logging in.
     */
    String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
