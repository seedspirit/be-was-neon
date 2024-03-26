package webserver.handler;

import db.UserDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.constants.Delimiter.AMPERSAND;
import static util.constants.Delimiter.EQUAL_SIGN;
import static webserver.URLConstants.DEFAULT_INDEX_PAGE;
import static webserver.URLConstants.REGISTRATION_FAILED_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;

public class UserCreateHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);
    private final String NAME_VALIDATION_REGEX = "^[가-힣a-zA-Z\\s]+$";
    private final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String USERID_PARAM = "username";
    private final String PASSWORD_PARAM = "password";
    private final String NAME_PARAM = "nickname";
    private final String EMAIL_PARAM = "email";


    public HttpResponse handleRequest(HttpRequest httpRequest){
        try{
            addUserInDatabase(new String(httpRequest.getBody()));
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .addHeaderComponent(LOCATION, DEFAULT_INDEX_PAGE)
                    .build();
        } catch (IllegalArgumentException e) {
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase() + e.getMessage())
                    .addHeaderComponent(LOCATION, REGISTRATION_FAILED_PAGE)
                    .build();
        }
    }

    private void addUserInDatabase(String body) throws IllegalArgumentException {
        Map<String, String> userInfo = getUserInfoFromBody(body);
        String userID = userInfo.get(USERID_PARAM);
        String password = userInfo.get(PASSWORD_PARAM);
        String name =  URLDecoder.decode(userInfo.get(NAME_PARAM), StandardCharsets.UTF_8);
        checkNameFormat(name);
        String email = URLDecoder.decode(userInfo.get(EMAIL_PARAM), StandardCharsets.UTF_8);
        checkEmailFormat(email);

        User user = new User(userID, password, name, email);
        UserDatabase.addUser(user);
        logger.debug("회원가입 성공! ID: {}, Name: {}", userID, name);
    }

    private Map<String, String> getUserInfoFromBody(String body) {
        Map<String, String> userInfo = new HashMap<>();
        String[] bodyComponents = body.split(AMPERSAND);
        for (String bodyComponent : bodyComponents) {
            String param = bodyComponent.split(EQUAL_SIGN)[0];
            String value = bodyComponent.split(EQUAL_SIGN)[1];
            userInfo.put(param, value);
        }
        return userInfo;
    }

    private void checkNameFormat(String name) throws IllegalArgumentException {
        if (!isValidNameFormat(name)){
            throw new IllegalArgumentException(": 잘못된 이름 형식입니다");
        }
    }

    private void checkEmailFormat(String email) throws IllegalArgumentException {
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException(": 잘못된 이메일 형식입니다");
        }
    }

    private boolean isValidNameFormat(String name){
        return name.matches(NAME_VALIDATION_REGEX);
    }

    private boolean isValidEmailFormat(String email){
        return email.matches(EMAIL_VALIDATION_REGEX);
    }
}
