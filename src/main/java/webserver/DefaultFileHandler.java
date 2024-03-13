package webserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFileHandler {
    private final HttpRequestMsg httpRequestMessage;
    private final String BASE_URL = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public DefaultFileHandler(HttpRequestMsg httpRequestMessage) {
        this.httpRequestMessage = httpRequestMessage;
    }

    public byte[] serialize() throws IOException {
        String path = extractPath(httpRequestMessage.getRequestTarget());
        String data = loadResource(path);
        return data.getBytes();
    }

    private String loadResource(String path) throws IOException {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader);
            return util.Reader.bufferedReaderToString(br);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        throw new IOException("요청하는 파일이 존재하지 않습니다");
    }

    private String extractPath(String requestTarget) {
        if (requestTarget.contains("register")){
            return BASE_URL + "/registration" + requestTarget;
        }
        return BASE_URL + requestTarget;
    }
}
