package me.tapeline.quailj.platformspecific;

import me.tapeline.quailj.parser.nodes.FieldReferenceNode;
import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.types.RuntimeStriker;

import java.io.*;
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

    /**
     * Console Put Function W/o \n
     * */
    public void consolePut(String s) {
        System.out.print(s);
    }

    /**
     * Console Input Function W/o \n
     * */
    public String consoleInput(String s) {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**
     * Input from a file
     */
    public static String fileInput(String path) {
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
    public static void fileSet(String path, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(content);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append sth. to existing file contents
     */
    public static void filePut(String path, String update) {
        String content = fileInput(path);
        fileSet(path, content + update);
    }

    /**
     * Convert path.file to path/file
     */
    public static String resolvePathFromDotNotation(Node operand) throws RuntimeStriker {
        String path = "";
        if (operand instanceof VariableNode) {
            path = ((VariableNode) operand).token.c;
        } else if (operand instanceof FieldReferenceNode) {
            path += resolvePathFromDotNotation(((FieldReferenceNode) operand).lnode);
            path += "/";
            path += resolvePathFromDotNotation(((FieldReferenceNode) operand).rnode);
        } else {
            throw new RuntimeStriker("run:cannot resolve path " + operand.toString());
        }
        return path;
    }

    /**
     * Load library contents
     */
    public static String loadLibrary(String path) {
        for (String folder : libFolders) {
            File f = new File(folder + path);
            if (f.exists()) {
                return fileInput(folder + path);
            }
        }
        return null;
    }

}
