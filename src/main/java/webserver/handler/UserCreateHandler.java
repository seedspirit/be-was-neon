package webserver.handler;

import db.UserDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.body.FormBody;
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

    private void addUserInDatabase(RequestBody body) throws IllegalArgumentException {
        FormBody formBody = (FormBody) body;

        formBody.checkUserNameFormatValid();
        formBody.checkUserEmailFormatValid();

        User user = formBody.createUserByFormData();
        UserDatabase.addUser(user);
        logger.debug("회원가입 성공! ID: {}, Name: {}", user.getUserId(), user.getName());
    }
}
