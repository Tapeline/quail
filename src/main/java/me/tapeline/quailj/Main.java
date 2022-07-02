package me.tapeline.quailj;
// V0.9
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.VariableTable;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.utils.Utilities;

public class Main {

    public static boolean recordMemory = true;

    public static void main(String[] args) throws RuntimeStriker {
        boolean debug = false, timings = true;
        String path = null;
        long msStart = System.currentTimeMillis();

        IOManager io = new IOManager();

	    if (args.length == 2) {
            debug = args[0].equalsIgnoreCase("true");
            path = args[1];
        } else if (args.length == 3) {
            debug = args[0].equalsIgnoreCase("true");
            path = args[1];
            timings = args[2].equalsIgnoreCase("true");
        } else if (args.length == 4) {
            debug = args[0].equalsIgnoreCase("true");
            path = args[1];
            timings = args[2].equalsIgnoreCase("true");
            recordMemory = args[3].equalsIgnoreCase("true");
        } else {
            debug = false;
            // path = "/home/tapeline/QuailProjects/Expressive/parser.q";
            path = "/home/tapeline/test.q";
        }

        String code = IOManager.fileInput(path);
        RuntimeWrapper wrapper = new RuntimeWrapper(code, debug, io, path, timings);
        Pair<QType, Runtime> resultPair = null;
        try {
            resultPair = wrapper.run();
            QType result = resultPair.a;
            if (result != null && !(result instanceof VoidType)) {
                io.consolePut("Runtime returned " + result + "\n");
            }
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerType.EXCEPTION)) {
                assert code != null;
                int[] pos = Utilities.getLine(code, striker.posChar);
                System.err.println("In file: " + path + ", line " + pos[0] + ":\n");
                System.err.println("An error occured during the execution of program! Details:");
                String[] strings = striker.val.toString().split(":");
                for (int i = 0; i < strings.length - 1; i++) {
                    for (int j = 0; j < i; j++)
                        System.err.print(" ");
                    System.err.println("At " + strings[i]);
                }
                System.err.println(strings[strings.length - 1]);
                System.err.println("\nTraced Actions:\n" + Runtime.mainRecord.toString(0));
            } else throw striker;
        }

        if (timings) {
            System.out.println("\n[Timings] Script done in " + (System.currentTimeMillis() - msStart) + "ms");
        }
    }
}
