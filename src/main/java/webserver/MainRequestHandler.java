package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.htttpRequest.factory.RequestFactory;
import webserver.httpMessage.httpResponse.util.ResponseTransmitter;
import webserver.router.FrontRouter;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;

public class MainRequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    private Socket connection;

    public MainRequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestFactory requestFactory = new RequestFactory();
            ResponseTransmitter responseTransmitter = new ResponseTransmitter();
            FrontRouter frontRouter = new FrontRouter();

            // inputStream을 읽어 HttpRequest 객체 생성
            HttpRequest httpRequest = requestFactory.createHttpRequestFrom(in);

            Authenticator authenticator = new Authenticator();
            // 비인가 상황에서 인가가 필요한 페이지에 접근할 때 login 페이지로 리다이렉션
            if(!authenticator.isAuthenticated(httpRequest)){
                responseTransmitter.transmit(generateRedirectionResponse(), out);
                return;
            }

            // HTTP 헤더에서 requestTarget 추출, 알맞는 핸들러 호출 후 결과 반환
            HttpResponse httpResponse = frontRouter.route(httpRequest);

            // 처리 결과를 바탕으로 HTTP 응답 메시지를 만들어 클라이언트에 전송
            responseTransmitter.transmit(httpResponse, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse generateRedirectionResponse(){
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .addHeaderComponent(LOCATION, URLConstants.LOGIN_INDEX_PAGE)
                .build();
    }
}
