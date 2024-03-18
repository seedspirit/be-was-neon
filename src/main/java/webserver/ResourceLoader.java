package webserver;

import webserver.exceptions.ResourceNotFoundException;

import java.io.*;

public class ResourceLoader {
    public byte[] load(String path) throws ResourceNotFoundException {
        File targetFile = new File(path);
        byte[] byteArray = new byte[(int) targetFile.length()];
        try (FileInputStream inputStream = new FileInputStream(targetFile)) {
            inputStream.read(byteArray);
            return byteArray;
        } catch (IOException e) {
            throw new ResourceNotFoundException("요청한 %s 리소스가 존재하지 않습니다", path);
        }
    }
}
