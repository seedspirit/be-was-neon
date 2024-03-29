package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.ContentType;

import java.util.List;
import java.util.Map;

import static webserver.httpMessage.HttpConstants.CONTENT_LENGTH;
import static webserver.httpMessage.HttpConstants.CONTENT_TYPE;

public class RequestHeaders {

    private final Map<String, List<String>> requestHeaders;

    public RequestHeaders(Map<String, List<String>> requestHeaders){
        this.requestHeaders = requestHeaders;
    }

    public Map<String, List<String>> getValues() {
        return requestHeaders;
    }

    public boolean containsContentType(){
        return requestHeaders.containsKey(CONTENT_TYPE);
    }

    public int getContentLength(){
        return Integer.parseInt(requestHeaders.get(CONTENT_LENGTH).get(0));
    }

    public boolean contentTypeEqualsFormURLEncoded(){
        return requestHeaders.get(CONTENT_TYPE).get(0).equals(ContentType.FORM_URL_ENCODED.getMimetype());
    }

    public List<String> getValueOf(String headerName){
        return requestHeaders.get(headerName);
    }
}
