package webserver.httpMessage.htttpRequest.factory;

import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.htttpRequest.body.RequestBody;
import webserver.httpMessage.htttpRequest.RequestHeaders;
import webserver.httpMessage.htttpRequest.RequestLine;

import java.io.*;

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
