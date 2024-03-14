package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestMsg {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String method;
    private String requestTarget;
    private String headers;
    private String body;

    public HttpRequestMsg(String msg){
        parseMsg(msg);
    }

    public String getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    private void parseMsg(String msg) {
        logger.debug(msg);
        splitHeaderBody(msg);
        String requestLine = msg.split(System.lineSeparator())[0];
        parseRequestLine(requestLine);
    }

    private void splitHeaderBody(String str){
        String[] parts = str.split("\n\n", 2);
        this.headers = parts[0];
        if (parts.length > 1) {
            this.body = parts[1];
        } else {
            this.body = "";
        }
    }

    private void parseRequestLine(String requestLine) {
        this.method = requestLine.split(" ")[0];
        this.requestTarget = requestLine.split(" ")[1];
    }
}
