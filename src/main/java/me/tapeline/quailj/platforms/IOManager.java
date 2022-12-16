package me.tapeline.quailj.platforms;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IOManager {

    /**
     * Predefined library locations list
     * */
    public static List<String> libFolders = new ArrayList<>(Arrays.asList(
            "",
            "/home/tapeline/", // Only for testing
            "lib/",
            "userlibs/",
            "custom/libs/",
            "addons/libs/"
    ));

    public static PrintStream output = System.out;
    public static InputStream input = System.in;

    /**
     * Console Put Function W/o \n
     * */
    public void consolePut(String s) {
        System.out.print(s);
    }

    /**
     * Console Input Function W/o \n
     * */
    public String consoleInput() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**
     * Input from a file
     */
    public static String fileInput(String path) {
        try {
            return FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (IOException ignored) { }
        return null;
    }

    public static String fileBinInput(String path) {
        try {
            File file = new File(path);
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(Files.newInputStream(file.toPath()));
            dis.readFully(fileData);
            dis.close();
            return new Base64().encodeAsString(fileData);
        } catch (IOException ignored) {}
        return null;
    }

    public static boolean fileBinSet(String path, String hex) {
        try {
            File file = new File(path);
            byte[] fileData = new Base64().decode(hex);

            DataOutputStream dos = new DataOutputStream(Files.newOutputStream(file.toPath()));
            dos.write(fileData);
            dos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String path) {
        File f = new File(path);
        return f.exists();
    }

    /**
     * Create blank file
     */
    public static void fileCreate(String path) {
        fileSet(path, "");
    }

    /**
     * Set file contents to sth.
     */
    public static boolean fileSet(String path, String content) {
        try {
            FileUtils.writeStringToFile(new File(path), content, "UTF-8");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Append sth. to existing file contents
     */
    public static void filePut(String path, String update) {
        String content = fileInput(path);
        fileSet(path, content + update);
    }

}
