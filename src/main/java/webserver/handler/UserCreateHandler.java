package webserver.handler;

import db.UserDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.body.URLEncodedFormBody;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.body.RequestBody;

import static webserver.URLConstants.DEFAULT_INDEX_PAGE;
import static webserver.URLConstants.REGISTRATION_FAILED_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;

public class UserCreateHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public HttpResponse handleRequest(HttpRequest httpRequest){
        try{
            addUserInDatabase(httpRequest.getBody());
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

    // Body에서 아이디 중복, 아이디와 비밀번호 형식 검증 후 User 객체를 생성, DB에 저장하는 회원가입 절차 진행
    private void addUserInDatabase(RequestBody body) throws IllegalArgumentException {
        URLEncodedFormBody URLEncodedFormBody = (URLEncodedFormBody) body;

        URLEncodedFormBody.checkUserIdDuplicated();
        URLEncodedFormBody.checkUserNameFormatValid();
        URLEncodedFormBody.checkUserEmailFormatValid();

        User user = URLEncodedFormBody.createUserByFormData();
        UserDatabase.addUser(user);
        logger.debug("회원가입 성공! ID: {}, Name: {}", user.getUserId(), user.getName());
    }
}
