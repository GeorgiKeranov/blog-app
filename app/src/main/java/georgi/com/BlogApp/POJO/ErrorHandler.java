package georgi.com.BlogApp.POJO;

// This class is used to handle JSON objects from the server.
public class ErrorHandler {

    private Boolean error;

    private String error_msg;

    public ErrorHandler(Boolean error) {
        this.error = error;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
