package webserver.httpMessage.httpRequest.body;

public class BinaryBody implements RequestBody {
    private final byte[] body;

    public BinaryBody(byte[] bytes){
        this.body = bytes;
    }

    @Override
    public byte[] toBytes(){
        return body;
    }
}
