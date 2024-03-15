package webserver;

import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

public class Router {


    public HttpResponseMsg route(HttpRequestMsg httpRequestMsg) {
        // HTTP 요청 메시지 정보를 바탕으로 적절한 핸들러 객체를 생성 호출하고, 해당 객체의 결과를 바탕으로 응답 메시지를 작성한다
        try{
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequestMsg);
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequestMsg.getRequestTarget());
                    return new HttpResponseMsg(200, "OK");
                }
                case "/index.html", "/register.html", "/registration" -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequestMsg);
                    return new HttpResponseMsg(200, "OK", defaultFileHandler.serialize());
                }
                default -> {
                    return new HttpResponseMsg(404, "Not Found: 요청한 리소스를 찾을 수 없습니다");
                }
            }
            // 핸들러 객체 등에서 에러가 발생하는 경우, 에러를 잡고 그 에러를 바탕으로 응답 메시지를 반환한다
        } catch (UrlFormatException e) {
            return new HttpResponseMsg(400, "Bad Request: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponseMsg(404, "Not Found: " + e.getMessage());
        }
    }
}
