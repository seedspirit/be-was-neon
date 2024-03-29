package webserver.httpMessage.httpRequest.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;
import webserver.httpMessage.httpRequest.RequestHeaders;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.constants.Delimiter.*;
import static webserver.httpMessage.HttpConstants.COOKIE;

public class RequestHeadersFactory {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public RequestHeaders createRequestHeadersFrom(BufferedInputStream bis) {
        Map<String, List<String>> headers = new HashMap<>();
        try{
            String line;
            while(!(line = Reader.readLineFrom(bis)).isEmpty()) {
                String[] headerParts = line.split(COLON, 2);
                String key = headerParts[0];
                List<String> values;
                if (key.equals(COOKIE)){
                    values = parseCookie(headerParts[1]);
                } else {
                    values = Arrays.stream(headerParts[1].split(COMMA)).map(String::trim).toList();
                }
                headers.put(key, values);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return new RequestHeaders(headers);
    }

    private List<String> parseCookie(String values) {
        return Arrays.stream(values.split(SEMICOLON)).map(String::trim).toList();
    }
}
