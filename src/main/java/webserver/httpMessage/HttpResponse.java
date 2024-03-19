package webserver.httpMessage;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import static util.constants.Delimiter.*;
import static webserver.httpMessage.HttpConstants.*;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String HTTP_VERSION_NUMBER = "1.1";
    private final String UTF_8 = "utf-8";
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
        headerBuilder.append(HTTP_VERSION_HEADER + HTTP_VERSION_NUMBER + statusCode + BLANK + reasonPhrase + BLANK + CRLF);
        headerBuilder.append(CONTENT_TYPE + COLON + BLANK + contentType.getMimetype() + SEMICOLON + MIME_TYPE_PARAMETER_CHARSET + EQUAL_SIGN + UTF_8 + BLANK + CRLF);
        headerBuilder.append(CONTENT_LENGTH + COLON + BLANK + body.length + CRLF);
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
