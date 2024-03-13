package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestMsg requestMsg = new HttpRequestMsg(in);

            // HTTP 헤더에서 requestTarget 추출, 알맞는 핸들러 호출
            String targetHandler = extractTargetHandler(requestMsg.getRequestTarget());
            Optional<byte[]> body = Optional.empty();
            switch (targetHandler) {
                case "create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler(requestMsg.getRequestTarget());
                    userCreateHandler.addUserInDatabase();
                    break;
                }
                default -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(requestMsg);
                    body = Optional.of(defaultFileHandler.serialize());
                }
            }

            // 처리 결과를 바탕으로 HTTP 응답 메시지를 만들어 클라이언트에 전송
            DataOutputStream dos = new DataOutputStream(out);
            if (body.isPresent()){
                HttpResponseMsg responseMsg = new HttpResponseMsg();
                responseMsg.send(dos, body.get());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String extractTargetHandler(String requestTarget) {
        return requestTarget.split("/")[1];
    }
}
