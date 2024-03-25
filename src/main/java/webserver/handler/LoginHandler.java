package webserver.handler;

import db.UserDatabase;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.session.Cookie;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static util.constants.Delimiter.AMPERSAND;
import static util.constants.Delimiter.EQUAL_SIGN;
import static webserver.URLConstants.LOGIN_FAILED_PAGE;
import static webserver.URLConstants.LOGIN_USER_DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpConstants.SET_COOKIE;
import static webserver.httpMessage.HttpStatus.FOUND;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);
    private final String USERID_PARAM = "username";
    private final String PASSWORD_PARAM = "password";
    private Cookie cookie;

    public HttpResponse handleRequest(HttpRequest httpRequest){
        if(isLoginSucceed(httpRequest.getBody())){
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .addHeaderComponent(SET_COOKIE, cookie.toString())
                    .addHeaderComponent(LOCATION, LOGIN_USER_DEFAULT_INDEX_PAGE)
                    .build();
        }
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .addHeaderComponent(LOCATION, LOGIN_FAILED_PAGE)
                .build();
    }

    private boolean isLoginSucceed(String body) {
        Map<String, String> params = parseBodyToMap(body);
        String userId = params.get(USERID_PARAM);
        if(!isUserExistsInDB(userId)){
            return false;
        }

        User user = UserDatabase.findUserById(userId);
        String passwordInput = params.get(PASSWORD_PARAM);
        if (!isPasswordInputCorrect(user, passwordInput)){
            return false;
        }

        this.cookie = new Cookie();
        registerCookieUserPair(cookie, user);
        logger.debug("로그인 성공! Name: {}, SessionId: {}", user.getName(), cookie.getSessionId());
        return true;
    }

    private Map<String, String> parseBodyToMap(String body) {
        Map<String, String> params = new HashMap<>();
        String[] parameters = body.split(AMPERSAND);
        for (String parameter : parameters) {
            String[] tmp = parameter.split(EQUAL_SIGN);
            String key = tmp[0];
            String value = URLDecoder.decode(tmp[1], StandardCharsets.UTF_8);
            params.put(key, value);
        }
        return params;
    }

    private boolean isPasswordInputCorrect(User user, String passwordInput) {
        return user.getPassword().equals(passwordInput);
    }

    private boolean isUserExistsInDB(String userId) {
        return UserDatabase.isUserExists(userId);
    }

    private void registerCookieUserPair(Cookie cookie, User user){
        SessionDatabase.addRecord(cookie, user);
    }
}
