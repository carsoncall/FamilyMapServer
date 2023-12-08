package Request;

/**
 * Container for the request data from the EventHandler to give to the EventService.
 */
public class EventRequest {

    /**
     * The authentication token of the requesting user.
     */
    String auth;

    /**
     * The string containing the ID of the event being retrieved
     */
    String eventID;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public EventRequest(String eventID, String auth) {
        this.eventID = eventID;
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
