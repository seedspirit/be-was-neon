package webserver.exceptions;

public class UrlFormatException extends Exception {
    public UrlFormatException(String message) {
        super(": " + message);
    }
}
