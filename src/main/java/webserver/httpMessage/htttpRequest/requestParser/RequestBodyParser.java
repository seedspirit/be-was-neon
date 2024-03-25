package webserver.httpMessage.htttpRequest.requestParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestBodyParser {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public String getParseResultFrom(BufferedReader br, int contentLength) {
        char[] chars = new char[contentLength];
        try{
            br.read(chars, 0, contentLength);
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
        return String.copyValueOf(chars);
    }
}
