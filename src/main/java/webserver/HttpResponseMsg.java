package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseMsg {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final int statusCode;
    private final String reasonPhrase;
    private final ContentType contentType;
    private final byte[] body;

    public HttpResponseMsg(int statusCode, String reasonPhrase, ContentType contentType, byte[] body){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contentType = contentType;
        this.body = body;
    }

    public HttpResponseMsg(int statusCode, String reasonPhrase, byte[] body){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contentType = ContentType.NONE;
        this.body = body;
    }

    // 별도의 body 내용 없이 메시지를 보내는 경우 사용
    public HttpResponseMsg(int statusCode, String reasonPhrase){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contentType = ContentType.NONE;
        this.body = new byte[0];
    }

    public void send(DataOutputStream dos) {
        writeResponseHeader(dos);
        writeResponseBody(dos);
    }

    private void writeResponseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " + reasonPhrase + " \r\n");
            dos.writeBytes("Content-Type:" + contentType.getMimetype() + ";charset=utf-8 \r\n");
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

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public byte[] getBody() {
        return body;
    }
}
