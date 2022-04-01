package me.tapeline.quailj;

import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.VoidType;

public class Main {

    public static void main(String[] args) throws RuntimeStriker {
        boolean debug;
        String path;
	    if (args.length < 2) {
            debug = false;
            path = "/home/tapeline/test.q";
        } else {
            debug = args[0].equalsIgnoreCase("true");
            path = args[1];
        }

        String code = IOManager.fileInput(path);
        RuntimeWrapper wrapper = new RuntimeWrapper(code, debug);
        QType result = wrapper.run();

        if (result != null && !(result instanceof VoidType)) {
            System.out.println("Runtime returned " + result);
        }
    }
}
