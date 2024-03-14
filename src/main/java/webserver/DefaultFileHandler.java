package webserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exceptions.ResourceNotFoundException;

public class DefaultFileHandler {
    private final HttpRequestMsg httpRequestMessage;
    private final String BASE_URL = "./src/main/resources/static";

    public DefaultFileHandler(HttpRequestMsg httpRequestMessage) {
        this.httpRequestMessage = httpRequestMessage;
    }

    public byte[] serialize() throws ResourceNotFoundException {
        String path = extractPath(httpRequestMessage.getRequestTarget());
        String data = loadResource(path);
        return data.getBytes();
    }

    private String loadResource(String path) throws ResourceNotFoundException{
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader);
            return util.Reader.bufferedReaderToString(br);
        } catch (IOException e) {
            throw new ResourceNotFoundException("요청한 %s 리소스가 존재하지 않습니다", path);
        }
    }

    private String extractPath(String requestTarget) {
        if (requestTarget.contains("register")){
            return BASE_URL + "/registration" + requestTarget;
        }
        return BASE_URL + requestTarget;
    }
}
