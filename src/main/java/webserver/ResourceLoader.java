package webserver;

import webserver.exceptions.ResourceNotFoundException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResourceLoader {
    public String load(String path) throws ResourceNotFoundException {
        File targetFile = new File(path);
        // TODO: String 말고 byte[] 반환으로 변경
        try (FileReader fileReader = new FileReader(targetFile)){
            char[] chars = new char[(int) targetFile.length()];
            fileReader.read(chars);
            return new String(chars);
        } catch (IOException e) {
            throw new ResourceNotFoundException("요청한 %s 리소스가 존재하지 않습니다", path);
        }
    }
}
