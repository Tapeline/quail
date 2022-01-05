package me.tapeline.quailj;

import java.io.*;

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

    public static boolean exists(String path) {
        File f = new File(path);
        return f.exists();
    }

    public static void create(String path) {
        write(path, "");
    }

    public static void write(String path, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(content);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void append(String path, String update) {
        String content = read(path);
        write(path, content + update);
    }
}
