package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.htttpRequest.requestParser.RequestBodyFactory;
import webserver.httpMessage.htttpRequest.requestParser.RequestHeadersFactory;
import webserver.httpMessage.htttpRequest.requestParser.RequestLineFactory;

import java.io.*;

import static webserver.httpMessage.HttpConstants.*;

public class RequestFactory {

    public HttpRequest createHttpRequestFrom(InputStream in) {
        BufferedInputStream bis = new BufferedInputStream(in);
        RequestLine requestLine = createRequestLine(bis);
        RequestHeaders headers = createHeaders(bis);
        RequestBody body = createBody(bis, headers);
        return new HttpRequest(requestLine, headers, body);
    }

    private RequestLine createRequestLine(BufferedInputStream bis) {
        return new RequestLineFactory().createRequestLineFrom(bis);
    }

    private RequestHeaders createHeaders(BufferedInputStream bis) {
        return new RequestHeadersFactory().createRequestHeadersFrom(bis);
    }

    private RequestBody createBody(BufferedInputStream bis, RequestHeaders requestHeaders) {
        return new RequestBodyFactory().createRequestBodyFrom(bis, requestHeaders);
    }
}
