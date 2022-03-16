package me.tapeline.quailj.platformspecific;

import me.tapeline.quailj.types.RuntimeStriker;
//import me.tapeline.quailj.parser.nodes.FieldReferenceNode;
//import me.tapeline.quailj.parser.nodes.Node;
//import me.tapeline.quailj.parser.nodes.VariableNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QFileMan {

    public static List<String> libFolders = new ArrayList<>(Arrays.asList(
            "",
            "/home/tapeline/",
            "lib/",
            "userlibs/",
            "custom/libs/",
            "addons/libs/"
    ));

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

    /*public static String resolvePathFromDotNotation(Node operand) throws RuntimeStriker {
        String path = "";
        if (operand instanceof VariableNode) {
            path = ((VariableNode) operand).token.c;
        } else if (operand instanceof FieldReferenceNode) {
            path += resolvePathFromDotNotation(((FieldReferenceNode) operand).lnode);
            path += resolvePathFromDotNotation(((FieldReferenceNode) operand).rnode);
        } else {
            throw new RuntimeStriker("run:cannot resolve path " + operand.toString());
        }
        return path;
    }*/

    public static String loadLibrary(String path) {
        for (String folder : libFolders) {
            File f = new File(folder + path);
            if (f.exists()) {
                return read(folder + path);
            }
        }
        return null;
    }
}
