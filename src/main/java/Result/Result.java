package Result;

/**
 * Used as a base class to all other Result classes; allows for all result classes to throw similar errors.
 */
public class Result {

    /**
     * The error response. If there was no error, the message will be null, to avoid being serialized.
     */
    String message = null;

    /**
     * The success indicator. Assumed to be true.
     */
    boolean success = true;

    /**
     * This function is used to set the error message and change "success" to false in one go.
     * @param errorMessage the message to be displayed as an error.
     */
    public void setMessage(String errorMessage) {
        success = false;
        this.message = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
