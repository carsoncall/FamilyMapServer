package Request;

/**
 * Container for the request data from the PersonHandler to give to the PersonService.
 */
public class PersonRequest {

    /**
     * The ID of the person being searched for.
     */
    private String personID;
    /**
     * The authentication token of the user making the request. Used to ensure that requested person belongs to the
     * user who is making the request.
     */
    private String auth;

    public PersonRequest(String personID, String auth) {
        this.personID = personID;
        this.auth = auth;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuth() {
        return auth;
    }
}
