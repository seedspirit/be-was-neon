package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseMsg {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final int statusCode;
    private final String reasonPhrase;
    private byte[] body;

    public HttpResponseMsg(int statusCode, String reasonPhrase, byte[] body){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.body = body;
    }

    public HttpResponseMsg(int statusCode, String reasonPhrase){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public void send(DataOutputStream dos) {
        writeResponseHeader(dos);
        writeResponseBody(dos);
    }

    private void writeResponseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + reasonPhrase + " \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeResponseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
