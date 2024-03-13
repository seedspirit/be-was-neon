package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestMsg {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String method;
    private String requestTarget;
    private String headers;
    private String body;

    public HttpRequestMsg(InputStream in){
        parseMsg(in);
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

    private void parseMsg(InputStream in) {
        try {
            String msg = inputStreamStringConversion(in);
            logger.debug(msg);
            splitHeaderBody(msg);
            String requestLine = msg.split(System.lineSeparator())[0];
            parseRequestLine(requestLine);
        } catch (IOException e){
            logger.error(e.getMessage());
        }
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

    private String inputStreamStringConversion(InputStream in) throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        return util.Reader.bufferedReaderToString(br);
    }
}
