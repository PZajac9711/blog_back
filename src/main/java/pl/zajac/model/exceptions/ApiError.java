package pl.zajac.model.exceptions;

import org.springframework.http.HttpStatus;

public class ApiError {
    private HttpStatus statusCode;
    private String message;
    private String debugMessage;

    public ApiError(HttpStatus statusCode, Throwable exception) {
        this.message = "Unexpected error";
        this.statusCode = statusCode;
        this.debugMessage = exception.getMessage();
    }

    public ApiError(HttpStatus statusCode, Throwable exception, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.debugMessage = exception.getMessage();
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
