package Service;

import DataAccess.*;
import Model.Event;
import Request.EventRequest;
import Result.EventDataResult;
import Result.EventResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Contains the Event search (/event/[eventID]) function
 */
public class EventService {

    /**
     * Returns the single Event object with the specified ID (if the event is associated with the current user).
     * The current user is determined by the provided authtoken.
     * @param eventRequest the container with the request information, including the authentication token.
     * @return a container with the resulting information that indicates success or failure.
     */
    public EventDataResult retrieve(EventRequest eventRequest) {

        Database db = new Database();

        try (Connection conn = db.getConnection()) {
            //Checking that the authtoken exists and saves the username in a variable for later use
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String username = authTokenDao.find(eventRequest.getAuth());

            if (username != null) {
                EventDao eventDao = new EventDao(conn);

                if (eventRequest.getEventID() == null) {
                    //Retrieving all data from the database. the null EventID means that there is not a specific
                    // event that was requested.
                    EventDataResult eventResult = new EventDataResult();
                    List<Event> result = eventDao.allEvents(username);
                    Event[] resultArray = result.toArray(new Event[0]);
                    eventResult.setData(resultArray);
                    return eventResult;

                } else {
                    //This is if there is a specific event requested.
                    Event event = eventDao.find(eventRequest.getEventID(),username);

                    if (event != null) {
                        return new EventResult(event.getAssociatedUsername(),event.getEventID(),event.getPersonID()
                                , event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity()
                                , event.getEventType(), event.getYear());

                    } else {
                        //This is the error if the eventID does not work (including if not associated with the username)
                        EventResult eventResult = new EventResult();
                        eventResult.setMessage("Error: The provided Event ID does not exist");
                        eventResult.setSuccess(false);
                        return eventResult;
                    }
                }

            } else {
                //The error that the authentication token is not valid
                EventResult eventResult = new EventResult();
                eventResult.setMessage("Error: The provided authentication is invalid");
                eventResult.setSuccess(false);
                return eventResult;
            }

        } catch (DataAccessException | SQLException d) {
            System.out.print("Error: EventService failed to access the database");
            d.printStackTrace();
        }

        //If we got here, there was an exception that was caught
        EventResult eventResult = new EventResult();
        eventResult.setMessage("Error: Unable to retrieve the event");
        eventResult.setSuccess(false);
        return eventResult;

    }
}
