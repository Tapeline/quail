package me.tapeline.quarkj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class QFileReader {

    public static String read(String path) {
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
