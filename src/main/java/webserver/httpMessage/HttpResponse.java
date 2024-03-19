package webserver.httpMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public static class Builder {
        // 필수 매개변수
        private int statusCode;
        private String reasonPhrase;

        // 선택 매개변수 - 기본값으로 초기화
        private ContentType contentType = ContentType.NONE;
        private byte[] body = new byte[0];

        public Builder(int statusCode, String reasonPhrase) {
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
        }

        public Builder contentType(ContentType val) {
            contentType = val;
            return this;
        }

        public Builder body(byte[] val) {
            body = val;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    private HttpResponse(Builder builder) {
        this.statusCode = builder.statusCode;
        this.reasonPhrase = builder.reasonPhrase;
        this.contentType = builder.contentType;
        this.body = builder.body;
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
