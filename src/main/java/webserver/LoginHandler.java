package webserver;

import db.Database;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static util.constants.Delimiter.EQUAL_SIGN;

public class LoginHandler {
    Map<String, String> params;

    public LoginHandler(String body){
        this.params = new HashMap<>();
        parseBody(body);
    }
    private void parseBody(String body) {
        String[] parameters = body.split("&");
        for (String parameter : parameters) {
            String[] tmp = parameter.split(EQUAL_SIGN);
            String key = tmp[0];
            String value = URLDecoder.decode(tmp[1], StandardCharsets.UTF_8);
            params.put(key, value);
        }
    }
}
