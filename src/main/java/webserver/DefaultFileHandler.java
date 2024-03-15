package webserver;

import webserver.exceptions.ResourceNotFoundException;

public class DefaultFileHandler {
    private final HttpRequestMsg httpRequestMessage;

    public DefaultFileHandler(HttpRequestMsg httpRequestMessage) {
        this.httpRequestMessage = httpRequestMessage;
    }

    // HTTP 요청 메시지에서 타깃 경로를 추출 후, 해당 경로에서 리소스를 얻어 byte 형태로 반환한다
    public byte[] serialize() throws ResourceNotFoundException {
        PathRedirector redirector = new PathRedirector();
        ResourceLoader loader = new ResourceLoader();
        String requestTarget = httpRequestMessage.getRequestTarget();

        String path = redirector.makeRedirection(requestTarget);
        String data = loader.load(path);
        return data.getBytes();
    }
}
