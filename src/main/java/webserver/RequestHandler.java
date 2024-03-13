package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

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
            // HTTP 요청을 String으로 변환
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String httpRequestMessage = util.Reader.bufferedReaderToString(br);

            logHttpRequest(httpRequestMessage);

            // HTTP 헤더에서 requestTarget 추출, 알맞는 핸들러 호출
            String targetHandler = extractTargetHandler(httpRequestMessage);
            Optional<byte[]> body = Optional.empty();
            switch (targetHandler) {
                case "create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler(httpRequestMessage);
                    userCreateHandler.addUserInDatabase();
                    break;
                }
                default -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequestMessage);
                    body = Optional.of(defaultFileHandler.serialize());
                }
            }

            // 처리 결과를 바탕으로 HTTP 응답 메시지를 만들어 클라이언트에 전송
            DataOutputStream dos = new DataOutputStream(out);
            if (body.isPresent()){
                byte[] b = body.get();
                response200Header(dos, b.length);
                responseBody(dos, b);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void logHttpRequest(String httpRequestMessage) {
        logger.debug(httpRequestMessage);
    }

    private String extractTargetHandler(String httpRequestMessage) {
        String requestLine = httpRequestMessage.split(System.lineSeparator())[0];
        String requestTarget = requestLine.split("\\?")[0];
        return requestTarget.split("/")[1];
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
