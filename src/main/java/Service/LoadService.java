package Service;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contains the Load (/load) function.
 */
public class LoadService {

    /**
     * Clears all data from the database (just like the /clear API). Loads the user, person, and event data from the
     * request body into the database.
     * @param loadRequest the object containing the data to be loaded into the database.
     * @return a container with the resulting data indicating success or failure.
     */
    public LoadResult load(LoadRequest loadRequest) {boolean success = false;
        Database db = new Database();

        try (Connection conn = db.getConnection()) {
            //First, clear everything
            ClearService clearService = new ClearService();
            clearService.clear();

            //Next, load users
            UserDao userDao = new UserDao(conn);
            int usersAdded = 0;
            for (User user : loadRequest.getUsers()) {
                userDao.insert(user);
                usersAdded++;
            }

            //Next, load Persons
            PersonDao personDao = new PersonDao(conn);
            int personsAdded = 0;
            for (Person person : loadRequest.getPersons()) {
                personDao.insert(person);
                personsAdded++;
            }

            //Finally, load events
            EventDao eventDao = new EventDao(conn);
            int eventsAdded = 0;
            for (Event event : loadRequest.getEvents()) {
                eventDao.insert(event);
                eventsAdded++;
            }

            //write to the database and make a successful Result object
            db.closeConnection(true);
            LoadResult result = new LoadResult();
            result.setMessage("Successfully added " + usersAdded + " users, " + personsAdded + " persons, and " +
                    eventsAdded + " events to the database.");
            result.setSuccess(true);
            return result;

        } catch (DataAccessException | SQLException d) {
            System.out.print("LoadService failed to load the data \n");
        }
        // If we are here, there was some sort of error that was caught. This returns a fail result.
        LoadResult result = new LoadResult();
        result.setMessage("Error: An error occurred while loading the events into the database");
        result.setSuccess(false);
        return result;
    }
}
