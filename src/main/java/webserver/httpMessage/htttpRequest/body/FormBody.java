package webserver.httpMessage.htttpRequest.body;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.constants.Delimiter.*;

public class FormBody implements RequestBody {
    private final byte[] rawBytes;
    private final Map<String, String> formParsedBytes;

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
}
