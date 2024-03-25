package webserver.httpMessage.httpResponse;

import java.util.Map;

public class ResponseHeaders {
    private final Map<String, String> responseHeaders;

    public ResponseHeaders(Map<String, String> responseHeaders){
        this.responseHeaders = responseHeaders;
    }

    public Map<String, String> getValues() {
        return responseHeaders;
    }

    public boolean containsKey(String key){
        return responseHeaders.containsKey(key);
    }

    public String getValueOf(String headerName){
        return responseHeaders.get(headerName);
    }

    public void addHeader(String headerName, String value){
        responseHeaders.put(headerName, value);
    }
}
