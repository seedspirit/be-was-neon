package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {

    public static BufferedReader inputStreamToBufferedReader(InputStream in) {
        InputStreamReader isr = new InputStreamReader(in);
        return new BufferedReader(isr);
    }

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