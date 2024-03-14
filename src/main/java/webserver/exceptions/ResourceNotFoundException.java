package webserver.exceptions;

public class ResourceNotFoundException extends Exception {
    private final String resource;
    public ResourceNotFoundException(String message, String resource) {
        super(message);
        this.resource = resource;
    }
}