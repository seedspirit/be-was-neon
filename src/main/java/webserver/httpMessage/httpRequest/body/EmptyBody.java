package webserver.httpMessage.httpRequest.body;

public class EmptyBody implements RequestBody {
    private byte[] body;

    public EmptyBody(){
        this.body = new byte[0];
    }

    @Override
    public byte[] toBytes() {
        return body;
    }
}