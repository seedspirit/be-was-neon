package webserver;

public class PathRedirector {
    private final String BASE_URL = "./src/main/resources/static";

    public String makeRedirection(String originPath) {
        if (originPath.contains("register")){
            return BASE_URL + "/registration" + originPath;
        }
        return BASE_URL + originPath;
    }

    // TODO: getResource 쓰기
}
