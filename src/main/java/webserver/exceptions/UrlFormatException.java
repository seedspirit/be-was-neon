package webserver.exceptions;

import static util.constants.Delimiter.*;

public class UrlFormatException extends Exception {
    public UrlFormatException(String message) {
        super(COLON + BLANK + message);
    }
    public UrlFormatException(){
    }
}
