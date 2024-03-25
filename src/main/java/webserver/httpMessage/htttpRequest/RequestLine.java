package webserver.httpMessage.htttpRequest;

import java.util.Map;

public class RequestLine {
    private final Map<String, String> requestLine;

    public RequestLine(Map<String, String> requestLine){
        this.requestLine = requestLine;
    }

    public Map<String, String> getValues() {
        return requestLine;
    }

    public String getValueOf(String key){
        return requestLine.get(key);
    }
}
