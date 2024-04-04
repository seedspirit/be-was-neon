package webserver.httpMessage.httpRequest.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;
import webserver.exceptions.ParsingException;
import webserver.httpMessage.httpRequest.RequestHeaders;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.constants.Delimiter.*;
import static webserver.httpMessage.HttpConstants.CONTENT_TYPE;
import static webserver.httpMessage.HttpConstants.COOKIE;

public class RequestHeadersFactory {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public RequestHeaders createRequestHeadersFrom(BufferedInputStream bis) throws ParsingException {
        Map<String, List<String>> headers = new HashMap<>();
        try{
            String line;
            while(!(line = Reader.readLineFrom(bis)).isEmpty()) {
                String[] headerParts = line.split(COLON, 2);
                String key = headerParts[0];
                List<String> values;
                if (key.equals(COOKIE)){
                    values = parseCookie(headerParts[1]);
                } else if (key.equals(CONTENT_TYPE)) {
                    values = parseContentType(headerParts[1]);
                } else {
                    values = Arrays.stream(headerParts[1].split(COMMA)).map(String::trim).toList();
                }
                headers.put(key, values);
            }
            return new RequestHeaders(headers);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ParsingException();
        }
    }

    private List<String> parseCookie(String values) {
        return Arrays.stream(values.split(SEMICOLON)).map(String::trim).toList();
    }

    private List<String> parseContentType(String values) {
        if (values.contains(SEMICOLON)){
            return Arrays.stream(values.split(SEMICOLON)).map(String::trim).toList();
        }
        return List.of(values.trim());
    }
}
