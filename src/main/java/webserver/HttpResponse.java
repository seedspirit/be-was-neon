package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final String BLANK = " ";
    private static final String SEMICOLON = ";";
    private static final String HTTP_VERSION_HEADER = "HTTP/";
    private final String HTTP_VERSION = "1.1";
    private static final String CONTENT_TYPE_HEADER = "Content-Type:";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length:";
    private static final String CRLF = "\r\n";
    private final int statusCode;
    private final String reasonPhrase;
    private final ContentType contentType;
    private final byte[] body;

    public HttpResponse(int statusCode, String reasonPhrase, ContentType contentType, byte[] body){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contentType = contentType;
        this.body = body;
    }

    public HttpResponse(int statusCode, String reasonPhrase, byte[] body){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contentType = ContentType.NONE;
        this.body = body;
    }

    // 별도의 body 내용 없이 메시지를 보내는 경우 사용
    public HttpResponse(int statusCode, String reasonPhrase){
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contentType = ContentType.NONE;
        this.body = new byte[0];
    }

    public void send(DataOutputStream dos) {
        String header = makeHeader();
        try {
            dos.writeBytes(header);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String makeHeader() {
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(HTTP_VERSION_HEADER + HTTP_VERSION + statusCode + BLANK + reasonPhrase + BLANK + CRLF);
        headerBuilder.append(CONTENT_TYPE_HEADER + BLANK + contentType.getMimetype() + SEMICOLON + "charset=utf-8" + BLANK + CRLF);
        headerBuilder.append(CONTENT_LENGTH_HEADER + BLANK + body.length + CRLF);
        headerBuilder.append(CRLF);
        return headerBuilder.toString();
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
