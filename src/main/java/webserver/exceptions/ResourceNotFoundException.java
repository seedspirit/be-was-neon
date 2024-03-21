package webserver.exceptions;

import static util.constants.Delimiter.*;

public class ResourceNotFoundException extends Exception {
    private final String resource;
    public ResourceNotFoundException(String message, String resource) {
        super(COLON + BLANK + String.format(message, resource));
        this.resource = resource;
    }
}