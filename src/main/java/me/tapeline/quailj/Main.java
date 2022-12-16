package me.tapeline.quailj;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.platforms.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.utils.Utilities;
import me.tapeline.quailj.utils.ErrorFormatter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Utilities.init();
        boolean debug = false, timings = false, translate = false;
        String path = null;
        long msStart = System.currentTimeMillis();

        // java -jar quail.jar run|debug|profile|translate path

        IOManager io = new IOManager();

        if (args.length < 2) {
            System.err.println("Usage: java -jar quail.jar run|profile path");
            return;
        }

        if (args[0].equalsIgnoreCase("run")) {
        } else if (args[0].equalsIgnoreCase("debug")) {
            debug = true;
        } else if (args[0].equalsIgnoreCase("profile")) {
            timings = true;
        } else if (args[0].equalsIgnoreCase("translate")) {
            translate = true;
        }

        path = args[1];

        String code = IOManager.fileInput(path);
        Node root = null;
        Runtime runtime = null;
        try {
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.scan();
            Parser parser = new Parser(code, tokens);
            root = parser.parse();
        } catch (ParserException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        } catch (Exception exception) {
            System.err.println("- - !!! AN INTERNAL ERROR HAS OCCURED !!! - -");
            System.err.println("    This is very untypical behaviour");
            System.err.println("    since Quail should be Exception-safe");
            System.err.println("    Although, the exception has occured");
            System.err.println("    and we highly recommend sending info");
            System.err.println("    below this warning message and your ");
            System.err.println("    Quail code to Quail's Github Issues:");
            System.err.println("      https://github.com/Tapeline/quail/issues");
            System.err.println("    Here begins valuable information:\n");
            System.err.println("Quail internal error");
            System.err.println("Message: " + exception.getMessage());
            System.err.println("Stack traceback: ");
            exception.printStackTrace();
            System.err.println("\n    Here ends valuable information.");
            System.err.println("    End of error message.\n");
            System.exit(1);
        }
        QObject result = null;
        try {
            runtime = new Runtime(code, root, io, args, timings);
            result = runtime.run(root, runtime.memory);
        } catch (RuntimeStriker striker) {
            if (striker.type == RuntimeStriker.Type.EXCEPTION) {
                System.err.println(ErrorFormatter.formatError(code, striker.error));
                System.exit(1);
            } else if (striker.type == RuntimeStriker.Type.RETURN) {
                result = striker.returnValue;
            } else if (striker.type == RuntimeStriker.Type.EXIT) {
                System.exit(striker.code);
            }
        }
        System.out.println("\nRuntime returned " + result);
        System.exit(0);
    }
}