package webserver.httpMessage.htttpRequest;

import static util.constants.Delimiter.EMPTY;

public class RequestBody {
    private final String body;

    public RequestBody(String body){
        this.body = body;
    }

    public RequestBody(){
        this.body = EMPTY;
    }

    public String getValues() {
        return body;
    }
}