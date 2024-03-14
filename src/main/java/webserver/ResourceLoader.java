package webserver;

import webserver.exceptions.ResourceNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ResourceLoader {
    public String load(String path) throws ResourceNotFoundException {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader);
            return util.Reader.bufferedReaderToString(br);
        } catch (IOException e) {
            throw new ResourceNotFoundException("요청한 %s 리소스가 존재하지 않습니다", path);
        }
    }
}
