package webserver.httpMessage.htttpRequest;

import static util.constants.Delimiter.EMPTY;

public class RequestBody {
    private byte[] body;

    public RequestBody(byte[] body){
        this.body = body;
    }

    public RequestBody(){
        this.body = EMPTY.getBytes();
    }

    public String getValuesInString() {
        return new String(body);
    }

    public byte[] getValues() {
        return body;
    }
}