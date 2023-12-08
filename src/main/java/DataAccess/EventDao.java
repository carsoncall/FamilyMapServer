package DataAccess;

import Model.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Contains all methods for interactions with the SQLite database for the Event table.
 */
public class EventDao{

    /**
     * The connection that the class will use to access the database.
     */
    private final Connection conn;

    public EventDao(Connection connection) {
        this.conn = connection;
    }

    /**
     * Inserts an event into the database.
     * @param event the Event object containing the event data.
     */
    public void insert(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }


    /**
     * Returns the single Event object with the specified ID, if the event is associated with the current user, as
     * determined by the authentication token.
     * //@param auth the authentication token of the current user.
     * @param eventID the unique ID of the event being searched for.
     * @return an Event object with the data, or null if it was not found/not associated with the current user.
     */
    public Event find(String eventID, String username) throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM event WHERE eventID = ? INTERSECT SELECT * FROM event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            stmt.setString(2,username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * Returns ALL events associated with the current user as determined by the provided authentication token
     * @param username the username of the current user.
     * @return a list of all the Event objects associated with the user.
     */
    public List<Event> allEvents(String username) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT * FROM event WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            System.out.print(rs.toString());
            while (rs.next()) {
                 Event theEvent =new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                 events.add(theEvent);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while accessing all events of a username in the database");
        }
    }

    /**
     * used as part of the /load webAPI and /fill webAPI. Loads the list of events given into the database.
     * @param events a list of all events to be loaded into the database, as Event objects
     */
    void load(List<Event> events) throws DataAccessException {

    }

    /**
     * Clears the database of all Event objects.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM event";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    /**
     * clears the database of all Event objects associated with a username.
     * @param username the username associated with the events that will be lost forever.
     */
    public void clear(String username) throws DataAccessException {
        String sql = "DELETE FROM event WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

}
