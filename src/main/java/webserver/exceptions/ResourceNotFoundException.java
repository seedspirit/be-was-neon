package webserver.exceptions;

public class ResourceNotFoundException extends Exception {
    private final String resource;
    public ResourceNotFoundException(String message, String resource) {
        super(": " + String.format(message, resource));
        this.resource = resource;
    }
}