package Service;

import DataAccess.*;
import Result.ClearResult;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contains the ClearDatabase (/clear) function.
 */
public class ClearService {

    /**
     * Deletes ALL data from the database, including user, authtoken, person, and event data
     */
    public ClearResult clear() {

        Database db = new Database();

        try (Connection conn = db.getConnection()) {
            //Clears user info
            UserDao userDao = new UserDao(conn);
            userDao.clear();

            //Clears authtoken info
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            authTokenDao.clear();

            //Clears event info
            EventDao eventDao = new EventDao(conn);
            eventDao.clear();

            //Clears person info
            PersonDao personDao = new PersonDao(conn);
            personDao.clear();

            db.closeConnection(true);

            //Makes the successful result
            ClearResult result = new ClearResult();
            result.setMessage("Clear succeeded.");
            result.setSuccess(true);
            System.out.print("Cleared the database");
            return result;

        } catch (DataAccessException | SQLException d) {
            System.out.print("There was an issue with the ClearHandler accessing the database.");
            db.closeConnection(false);
            d.printStackTrace();
        }

        ClearResult result = new ClearResult();
        result.setMessage("Error: The ClearService failed to clear the database.");
        result.setSuccess(false);
        return result;
    }

}
