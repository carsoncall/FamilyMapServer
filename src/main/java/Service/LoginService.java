package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Contains the Login (/user/login) function.
 */
public class LoginService {

    /**
     * Logs the user in and returns a LoginResult container.
     * @param loginRequest a container with the information from the user's request.
     * @return a container with the resulting information that indicates success or failure.
     */
    public LoginResult login(LoginRequest loginRequest) {
        User user = null;
        Database db = new Database();

        //First, we check that the user exists
        try (Connection conn = db.getConnection()){
            UserDao userDao;
            userDao = new UserDao(conn);
            user = userDao.find(loginRequest.getUsername(),loginRequest.getPassword());
            db.closeConnection(true);
        } catch (DataAccessException | SQLException d) {
            System.out.print("LoginService failed to open the database\n");
            db.closeConnection(false);
            d.printStackTrace();
        }

        if (user != null) {
            //This is where we make an authtoken and add it to the database
            String authTokenString = UUID.randomUUID().toString();
            try (Connection conn = db.getConnection()) {
                AuthTokenDao authTokenDao= new AuthTokenDao(conn);
                AuthToken authToken = new AuthToken(authTokenString, user.getUsername());
                authTokenDao.insert(authToken);
                db.closeConnection(true);

            } catch (DataAccessException | SQLException d) {
                //This is what happens if our database query fails
                System.out.print("LoginService failed to create or insert an AuthToken\n");
                db.closeConnection(false);
                d.printStackTrace();
            }

            return new LoginResult(authTokenString,user.getUsername(),user.getPersonID());

        } else {
            //Error where the user is not registered
            LoginResult errorResult = new LoginResult(null,null,null);
            errorResult.setSuccess(false);
            errorResult.setMessage("Error: User not registered (or failed to retrieve user)");
            return errorResult;
        }
    }

}
