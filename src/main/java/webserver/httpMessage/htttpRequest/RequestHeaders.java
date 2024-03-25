package webserver.httpMessage.htttpRequest;

import java.util.List;
import java.util.Map;

public class RequestHeaders {

    private final Map<String, List<String>> requestHeaders;

    public RequestHeaders(Map<String, List<String>> requestHeaders){
        this.requestHeaders = requestHeaders;
    }

    public Map<String, List<String>> getValues() {
        return requestHeaders;
    }

    public boolean containsKey(String key){
        return requestHeaders.containsKey(key);
    }

    public List<String> getValueOf(String headerName){
        return requestHeaders.get(headerName);
    }
}
