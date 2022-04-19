package me.tapeline.quailj;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.RuntimeStrikerType;
import me.tapeline.quailj.types.VoidType;
import me.tapeline.quailj.utils.Utilities;

public class Main {

    public static void main(String[] args) throws RuntimeStriker {
        boolean debug;
        String path;

        IOManager io = new IOManager();

	    if (args.length < 2) {
            debug = false;
            path = "/home/tapeline/test.q";
        } else {
            debug = args[0].equalsIgnoreCase("true");
            path = args[1];
        }

        String code = IOManager.fileInput(path);
        RuntimeWrapper wrapper = new RuntimeWrapper(code, debug, io, path);
        try {
            QType result = wrapper.run();
            if (result != null && !(result instanceof VoidType)) {
                io.consolePut("Runtime returned " + result + "\n");
            }
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerType.EXCEPTION)) {
                assert code != null;
                int[] pos = Utilities.getLine(code, striker.posChar);
                System.err.println("In file: " + path + ", line " + pos[0] + ", column " +
                        pos[1] + ":\n");
                System.err.println("An error occured during the execution of program! Details:");
                String[] strings = striker.val.toString().split(":");
                for (int i = 0; i < strings.length - 1; i++) {
                    for (int j = 0; j < i; j++)
                        System.err.print(" ");
                    System.err.println("At " + strings[i]);
                }
                System.err.println(strings[strings.length - 1]);
            } else throw striker;
        }
    }
}
