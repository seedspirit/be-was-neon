package webserver.httpMessage.httpRequest.body;

import db.UserDatabase;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.constants.Delimiter.*;

public class FormBody implements RequestBody {
    private final byte[] rawBytes;
    private final Map<String, String> formParsedBytes;
    private final String NAME_VALIDATION_REGEX = "^[가-힣a-zA-Z\\s]+$";
    private final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String USERID_PARAM = "username";
    private final String PASSWORD_PARAM = "password";
    private final String NAME_PARAM = "nickname";
    private final String EMAIL_PARAM = "email";


    public FormBody(byte[] bytes){
        this.rawBytes = bytes;
        this.formParsedBytes = new HashMap<>();
        parseForm(bytes);
    }

    private void parseForm(byte[] bytes){
        if (bytes.length == 0){
            return;
        }
        String bodyStr = new String(bytes);
        String[] bodyComponents = bodyStr.split(AMPERSAND);
        for (String bodyComponent : bodyComponents) {
            String param = bodyComponent.split(EQUAL_SIGN)[0];
            String value = URLDecoder.decode(bodyComponent.split(EQUAL_SIGN)[1], StandardCharsets.UTF_8);
            formParsedBytes.put(param, value);
        }
    }

    @Override
    public byte[] toBytes() {
        return rawBytes;
    }

    public Map<String, String> getFormParsedBytes(){
        return formParsedBytes;
    }

    public User createUserByFormData() throws IllegalArgumentException {
        String userID = formParsedBytes.get(USERID_PARAM);
        String password = formParsedBytes.get(PASSWORD_PARAM);
        String name =  URLDecoder.decode(formParsedBytes.get(NAME_PARAM), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(formParsedBytes.get(EMAIL_PARAM), StandardCharsets.UTF_8);
        return new User(userID, password, name, email);
    }

    public void checkUserIdDuplicated() throws IllegalArgumentException {
        if (userIdExistsInDB()){
            throw new IllegalArgumentException();
        }
    }

    public void checkUserNameFormatValid() throws IllegalArgumentException {
        if (!URLDecoder.decode(formParsedBytes.get(NAME_PARAM), StandardCharsets.UTF_8).matches(NAME_VALIDATION_REGEX)){
            throw new IllegalArgumentException(": 잘못된 이름 형식입니다");
        }
    }

    public void checkUserEmailFormatValid() throws IllegalArgumentException {
        if (!URLDecoder.decode(formParsedBytes.get(EMAIL_PARAM), StandardCharsets.UTF_8).matches(EMAIL_VALIDATION_REGEX)) {
            throw new IllegalArgumentException(": 잘못된 이메일 형식입니다");
        }
    }
    public boolean userIdExistsInDB() {
        return UserDatabase.isUserExists(getUserId());
    }

    public boolean passwordInputCorrespondPasswordOf(User user) {
        return user.getPassword().equals(getPassword());
    }

    public String getUserId(){
        return formParsedBytes.get(USERID_PARAM);
    }

    public String getPassword(){
        return formParsedBytes.get(PASSWORD_PARAM);
    }

    public String getUserName(){
        return URLDecoder.decode(formParsedBytes.get(NAME_PARAM), StandardCharsets.UTF_8);
    }

    public String getUserEmail(){
        return URLDecoder.decode(formParsedBytes.get(EMAIL_PARAM), StandardCharsets.UTF_8);
    }
}
