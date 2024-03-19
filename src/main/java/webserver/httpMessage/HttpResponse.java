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
    private final Map<String, String> requestLine;
    private final Map<String, String> header;
    private final ContentType contentType;
    private final byte[] body;

    public static class Builder {
        private final String HTTP_VERSION_NUMBER = "1.1";
        // 필수 매개변수
        private final Map<String, String> requestLine;
        private final Map<String, String> header;

        // 선택 매개변수 - 기본값으로 초기화
        private ContentType contentType = ContentType.NONE;
        private byte[] body = new byte[0];

        public Builder(int statusCode, String reasonPhrase) {
            this.requestLine = new HashMap<>();
            initRequestLine(statusCode, reasonPhrase);

            this.header = new HashMap<>();
            initHeader();
        }

        private void initRequestLine(int statusCode, String reasonPhrase){
            requestLine.put(HEADER_HTTP_VERSION, HTTP_VERSION_NUMBER);
            requestLine.put(STATUS_CODE_KEY, String.valueOf(statusCode));
            requestLine.put(REASON_PHRASE, reasonPhrase);
        }

        private void initHeader() {
            header.put(CONTENT_TYPE, contentType.getMimetype());
            header.put(CONTENT_LENGTH, String.valueOf(body.length));
        }

        public Builder contentType(ContentType val) {
            contentType = val;
            header.put(CONTENT_TYPE, contentType.getMimetype());
            return this;
        }

        public Builder body(byte[] val) {
            body = val;
            header.put(CONTENT_LENGTH, String.valueOf(body.length));
            return this;
        }

        public Builder addHeaderComponent(String key, String value) {
            header.put(key, value);
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    private HttpResponse(Builder builder) {
        this.requestLine = builder.requestLine;
        this.contentType = builder.contentType;
        this.header = builder.header;
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
        headerBuilder.append(HEADER_HTTP_VERSION + HTTP_VERSION_NUMBER + getStatusCode() + BLANK + getReasonPhrase() + BLANK + CRLF);
        headerBuilder.append(CONTENT_TYPE + COLON + BLANK + contentType.getMimetype() + SEMICOLON + MIME_TYPE_PARAMETER_CHARSET + EQUAL_SIGN + UTF_8 + BLANK + CRLF);
        headerBuilder.append(CONTENT_LENGTH + COLON + BLANK + body.length + CRLF);
        headerBuilder.append(CRLF);
        return headerBuilder.toString();
    }

    public int getStatusCode() {
        return Integer.parseInt(requestLine.get(STATUS_CODE_KEY));
    }

    public String getReasonPhrase() {
        return requestLine.get(REASON_PHRASE);
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
