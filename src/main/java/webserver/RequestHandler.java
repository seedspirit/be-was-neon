package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final String BASE_URL = "./src/main/resources/static";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String httpRequestMessage = readTargetFile(br);
            logHttpRequest(httpRequestMessage);
            String url = extractURL(httpRequestMessage);

            DataOutputStream dos = new DataOutputStream(out);
            String bodyString = readTargetFile(new BufferedReader(new FileReader(url)));
            byte[] body = bodyString.getBytes();

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void logHttpRequest(String httpRequestMessage) {
        logger.debug(httpRequestMessage);
    }

    private String extractURL(String httpRequestMessage) {
        String requestLine = httpRequestMessage.split(System.lineSeparator())[0];
        return BASE_URL + requestLine.split(" ")[1];
    }

    private String readTargetFile(BufferedReader br) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            if (line.isEmpty()){
                break;
            }
            builder.append(line).append(System.lineSeparator());
        }
        return builder.toString();
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
