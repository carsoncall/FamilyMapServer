package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Request.FillRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Contains the Register function (/user/register).
 */

public class RegisterService {

    /**
     * Creates a new user account (user row in the database) and generates 4 generations of ancestor data for the
     * new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username
     * as parameters). Logs the user in. Returns an authtoken
     * @param registerRequest a container with the data from the user's request.
     * @return a container with the resulting data, indicating success or failure.
     */
    public RegisterResult register(RegisterRequest registerRequest) {
        boolean success = false;
        Database db = new Database();

        try (Connection conn = db.getConnection()) {
            //Makes a new user
            UserDao userDao;
            userDao = new UserDao(conn);
            String personID = UUID.randomUUID().toString();
            User user = new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(),
                    registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getGender(), personID);
            userDao.insert(user);
            db.closeConnection(true);
            success = true;

        } catch (DataAccessException | SQLException d) {
            System.out.print("RegisterService failed to register the user\n");
            success = false;
            d.printStackTrace();
        }

        if (success) {
            //Calls the FillService
            FillRequest fillRequest = new FillRequest(registerRequest.getUsername());
            FillService fillService = new FillService();
            fillService.fill(fillRequest);

            //Logs the user in
            LoginRequest loginRequest = new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword());
            LoginService loginService = new LoginService();
            LoginResult loginResult = loginService.login(loginRequest);

            //Takes loginResult and makes registerResult
            if (loginResult.isSuccess()) {
                return new RegisterResult(loginResult.getAuthtoken(), loginResult.getUsername(),
                        loginResult.getPersonID());
            } else {
                // Unlikely error, but worth including
                RegisterResult registerResult = new RegisterResult(null, null, null);
                registerResult.setMessage("Error: Failed to log in user despite having recently been registered");
                registerResult.setSuccess(false);
                return registerResult;
            }
        } else {
            RegisterResult registerResult = new RegisterResult(null, null, null);
            registerResult.setMessage("Error: User already exists");
            registerResult.setSuccess(false);
            return registerResult;
        }



    }

}