package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.constants.Delimiter.*;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String METHOD = "method";
    private final String REQUEST_TARGET = "requestTarget";
    private final String HTTP_VERSION = "httpVersion";
    private final String CONTENT_LENGTH = "Content-Length";
    private final Map<String, String> requestLine;
    private final Map<String, List<String>> headers;
    private ContentType contentType;
    private String body;

    public HttpRequest(BufferedReader br){
        this.requestLine = new HashMap<>();
        this.headers = new HashMap<>();
        parseRequestLine(br);
        parseHeader(br);
        if (headers.containsKey(CONTENT_LENGTH)){
            parseBody(br);
        } else {
            this.body = EMPTY;
        }
    }

    private void parseRequestLine(BufferedReader br)  {
        try{
            String requestLineString = br.readLine();
            requestLine.put(METHOD, requestLineString.split(BLANK)[0]);
            requestLine.put(REQUEST_TARGET, requestLineString.split(BLANK)[1]);
            requestLine.put(HTTP_VERSION, requestLineString.split(BLANK)[2]);
            this.contentType = ContentType.findMatchingContentType(getRequestTarget());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void parseHeader(BufferedReader br) {
        try{
            String line;
            while((line = br.readLine()) != null && !line.isEmpty()) {
                String[] headerParts = line.split(COLON, 2);
                String key = headerParts[0];
                List<String> values = Arrays.stream(headerParts[1].split(COMMA)).map(String::trim).toList();
                headers.put(key, values);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void parseBody(BufferedReader br) {
        try{
            int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH).get(0));
            char[] chars = new char[contentLength];
            br.read(chars, 0, contentLength);
            this.body = String.copyValueOf(chars);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String getMethod() {
        return requestLine.get(METHOD);
    }

    public String getRequestTarget() {
        return requestLine.get(REQUEST_TARGET);
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public Map<String, String> getRequestLine() {
        return requestLine;
    }

    public String getBody() {
        return body;
    }

    public boolean requireStaticFile() {
        return !contentType.equals(ContentType.NONE);
    }
}
