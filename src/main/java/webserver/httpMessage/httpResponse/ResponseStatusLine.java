package webserver.httpMessage.httpResponse;

import java.util.Map;

public class ResponseStatusLine {
    private final Map<String, String> responseStatusLine;

    public ResponseStatusLine(Map<String, String> responseStatusLine){
        this.responseStatusLine = responseStatusLine;
    }

    public Map<String, String> getValues() {
        return responseStatusLine;
    }

    public String getValueOf(String key){
        return responseStatusLine.get(key);
    }
}