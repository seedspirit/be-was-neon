package webserver.httpMessage.htttpRequest.requestParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static util.constants.Delimiter.BLANK;
import static webserver.httpMessage.HttpConstants.*;

public class RequestLineParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Map<String, String> getParseResultFrom(BufferedReader br) {
        Map<String, String> requestLine = new HashMap<>();
        try{
            String requestLineString = br.readLine();
            String[] requestLineComponents = requestLineString.split(BLANK);
            requestLine.put(METHOD, requestLineComponents[0]);
            requestLine.put(REQUEST_TARGET, requestLineComponents[1]);
            requestLine.put(HTTP_VERSION_KEY, requestLineComponents[2]);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return requestLine;
    }
}
