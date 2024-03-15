package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    // HTTP 요청 특성상 얼마만큼의 요청이 들어올지 모르기 때문에 유연하게 처리하기 위해 newCachedThreadPool 사용
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // 이후 메서드 체이닝으로 쉽게 작업 추가를 할 수 있어 .runAsync 사용
                CompletableFuture.runAsync(new RequestHandler(connection), executorService);
            }
        }
    }
}