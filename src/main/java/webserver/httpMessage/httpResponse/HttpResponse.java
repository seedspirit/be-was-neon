package webserver.httpMessage.httpResponse;

import webserver.httpMessage.ContentType;

import java.util.HashMap;
import java.util.Map;

import static webserver.httpMessage.HttpConstants.*;

public class HttpResponse {
    private final ResponseStatusLine statusLine;
    private final ResponseHeaders headers;
    private final ContentType contentType;
    private ResponseBody body;

    public static class Builder {
        private final String HTTP_VERSION_NUMBER = "1.1";
        // 필수 매개변수
        private final ResponseStatusLine statusLine;
        private final ResponseHeaders headers;

        // 선택 매개변수 - 기본값으로 초기화
        private ContentType contentType = ContentType.NONE;
        private ResponseBody body = new ResponseBody();

        public Builder(int statusCode, String reasonPhrase) {
            Map<String, String> statusLine = new HashMap<>();
            statusLine.put(HTTP_VERSION_KEY, HTTP_VERSION_NUMBER);
            statusLine.put(STATUS_CODE_KEY, String.valueOf(statusCode));
            statusLine.put(REASON_PHRASE, reasonPhrase);
            this.statusLine = new ResponseStatusLine(statusLine);


            Map<String, String> headers = new HashMap<>();
            headers.put(CONTENT_TYPE, contentType.getMimetype());
            headers.put(CONTENT_LENGTH, String.valueOf(body.getBodyLength()));
            this.headers = new ResponseHeaders(headers);
        }

        public Builder contentType(ContentType val) {
            contentType = val;
            return this;
        }

        public Builder body(byte[] val) {
            body = new ResponseBody(val);
            headers.addHeader(CONTENT_LENGTH, String.valueOf(body.getBodyLength()));
            return this;
        }

        public Builder addHeaderComponent(String key, String value) {
            headers.addHeader(key, value);
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    private HttpResponse(Builder builder) {
        this.statusLine = builder.statusLine;
        this.contentType = builder.contentType;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public Map<String, String> getStatusLine () {
        return statusLine.getValues();
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getStatusCode() {
        return Integer.parseInt(statusLine.getValueOf(STATUS_CODE_KEY));
    }

    public String getReasonPhrase() {
        return statusLine.getValueOf(REASON_PHRASE);
    }

    public Map<String, String> getHeader() {
        return headers.getValues();
    }

    public byte[] getBody() {
        return body.getValues();
    }
}
