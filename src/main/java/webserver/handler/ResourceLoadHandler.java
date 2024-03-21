package webserver.handler;

import webserver.exceptions.ResourceNotFoundException;

import java.io.*;

public class ResourceLoadHandler {
    private final String STATIC_DIR_PATH = "/static";

    public byte[] load(String path) throws ResourceNotFoundException {
        String targetPath = STATIC_DIR_PATH + path;
        InputStream inputStream = this.getClass().getResourceAsStream(targetPath);
        if (inputStream == null) {
            throw new ResourceNotFoundException("요청한 리소스를 찾을 수 없습니다", path);
        }
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] bytes = bis.readAllBytes();
            return bytes;
        } catch (IOException e) {
            throw new ResourceNotFoundException("요청한 리소스를 %s에서 읽는 중에 에러가 발생했습니다", path);
        }
    }
}