package webserver;

import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    private final String INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN = "^\\/[^\\/\\?]+";

    public HttpResponseMsg route(HttpRequestMsg httpRequestMsg) {
        try{
            String targetHandler = extractTargetHandler(httpRequestMsg);
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler(httpRequestMsg.getRequestTarget());
                    userCreateHandler.addUserInDatabase();
                    return new HttpResponseMsg(200, "OK");
                }
                case "/index.html", "/register.html" -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequestMsg);
                    return new HttpResponseMsg(200, "OK", defaultFileHandler.serialize());
                }
                default -> {
                    return new HttpResponseMsg(404, "Not Found: 요청한 리소스를 찾을 수 없습니다");
                }
            }
        } catch (UrlFormatException e) {
            return new HttpResponseMsg(400, "Bad Request: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponseMsg(404, "Not Found: " + e.getMessage());
        }
    }

    private String extractTargetHandler(HttpRequestMsg httpRequestMsg) throws UrlFormatException {
        String requestTarget = httpRequestMsg.getRequestTarget();
        Pattern pattern = Pattern.compile(INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN);
        Matcher matcher = pattern.matcher(requestTarget);
        if (matcher.find()) {
            return matcher.group();
        }
        else {
            throw new UrlFormatException("잘못된 형식의 URL입니다.");
        }
    }
}
