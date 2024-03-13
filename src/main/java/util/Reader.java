package util;

import java.io.BufferedReader;
import java.io.IOException;

public class Reader {
    public static String bufferedReaderToString(BufferedReader br) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            if (line.isEmpty()){
                break;
            }
            builder.append(line).append(System.lineSeparator());
        }
        return builder.toString();
    }
}