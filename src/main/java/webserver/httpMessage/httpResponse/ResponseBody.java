package webserver.httpMessage.httpResponse;

public class ResponseBody {
    private final byte[] body;

    public ResponseBody(byte[] body){
        this.body = body;
    }

    public ResponseBody(){
        this.body = new byte[0];
    }

    public byte[] getValues() {
        return body;
    }

    public int getBodyLength(){
        return body.length;
    }
}
