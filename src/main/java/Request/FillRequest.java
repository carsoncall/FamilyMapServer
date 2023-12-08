package Request;

/**
 * Container for the request data from the FillHandler to give to the FillService.
 */
public class FillRequest {

    /**
     * The username of the user who makes the fill request.
     */
    String username;
    /**
     * the number of generations that the user would like to populate. Default is 4.
     */
    int generations = 4;

    public FillRequest(String username) {
        this.username = username;
    }
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
