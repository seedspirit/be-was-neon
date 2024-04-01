package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exceptions.ParsingException;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.factory.RequestFactory;
import webserver.httpMessage.httpResponse.util.ResponseTransmitter;
import webserver.router.FrontRouter;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.INTERNAL_SERVER_ERROR;

public class MainRequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    private Socket connection;
    private ResponseTransmitter responseTransmitter;

    public MainRequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // inputStream을 읽어 HttpRequest 객체 팩토리 생성
            RequestFactory requestFactory = new RequestFactory(in);

            // outputStream을 받아 HttpResponse를 반환하는 객체 생성
            responseTransmitter = new ResponseTransmitter(out);

            // 생성된 request 객체를 바탕으로 서버 동작
            runServer(requestFactory);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void runServer(RequestFactory requestFactory){
        try {
            // HTTP Request 객체 생성
            HttpRequest httpRequest = requestFactory.createHttpRequest();

            // 비인가 상황에서 인가가 필요한 페이지에 접근할 때 login 페이지로 리다이렉션
            Authenticator authenticator = new Authenticator();
            if(!authenticator.isAuthenticated(httpRequest)){
                responseTransmitter.transmit(generateRedirectionResponse());
                return;
            }

            // HTTP 헤더에서 requestTarget 추출, 알맞는 핸들러 호출 후 결과 반환
            FrontRouter frontRouter = new FrontRouter();
            HttpResponse httpResponse = frontRouter.route(httpRequest);

            // 처리 결과를 바탕으로 HTTP 응답 메시지를 만들어 클라이언트에 전송
            responseTransmitter.transmit(httpResponse);
        } catch (ParsingException e){
            // RequestFactory에서 파싱 에러 발생할 경우 500 응답을 전송
            responseTransmitter.transmit(generateInternalErrorResponse());
        }
    }

    private HttpResponse generateRedirectionResponse(){
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .addHeaderComponent(LOCATION, URLConstants.LOGIN_INDEX_PAGE)
                .build();
    }

    private HttpResponse generateInternalErrorResponse(){
        return new HttpResponse.Builder(INTERNAL_SERVER_ERROR.getStatusCode(), INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }
}
