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
    private final Map<String, String> statusLine;
    private final Map<String, String> header;
    private final ContentType contentType;
    private final byte[] body;

    public static class Builder {
        private final String HTTP_VERSION_NUMBER = "1.1";
        // 필수 매개변수
        private final Map<String, String> statusLine;
        private final Map<String, String> header;

        // 선택 매개변수 - 기본값으로 초기화
        private ContentType contentType = ContentType.NONE;
        private byte[] body = new byte[0];

        public Builder(int statusCode, String reasonPhrase) {
            this.statusLine = new HashMap<>();
            initRequestLine(statusCode, reasonPhrase);

            this.header = new HashMap<>();
            initHeader();
        }

        private void initRequestLine(int statusCode, String reasonPhrase){
            statusLine.put(HTTP_VERSION_KEY, HTTP_VERSION_NUMBER);
            statusLine.put(STATUS_CODE_KEY, String.valueOf(statusCode));
            statusLine.put(REASON_PHRASE, reasonPhrase);
        }

        private void initHeader() {
            header.put(CONTENT_TYPE, contentType.getMimetype());
            header.put(CONTENT_LENGTH, String.valueOf(body.length));
        }

        public Builder contentType(ContentType val) {
            contentType = val;
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
        this.statusLine = builder.statusLine;
        this.contentType = builder.contentType;
        this.header = builder.header;
        this.body = builder.body;
    }

    public Map<String, String> getStatusLine () {
        return statusLine;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getStatusCode() {
        return Integer.parseInt(statusLine.get(STATUS_CODE_KEY));
    }

    public String getReasonPhrase() {
        return statusLine.get(REASON_PHRASE);
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
