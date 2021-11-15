package me.tapeline.quarkj;

import me.tapeline.quarkj.language.types.DirectInstruction;
import me.tapeline.quarkj.language.types.DirectInstructionType;
import me.tapeline.quarkj.language.types.QType;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String path;
        Scanner sc = new Scanner(System.in);
        if (args.length == 2) {
            path = args[0];
        } else {
            System.out.println("-<=====>- Welcome to Quark interpreter -<=====>-");
            System.out.println("::::           Quark v0.4.1-alpha           ::::");
            System.out.println("::::           (C) Tapeline, 2021           ::::");
            System.out.println("-<=====>--------------------------------<=====>-");
            System.out.println(":::: Type in path to Quark Source Code> ");
            path = sc.next(); //"/home/tapeline/test.q";//
        }
        String code = QFileReader.read(path);
        boolean debug;
        if (args.length == 2) {
            debug = Boolean.parseBoolean(args[1]);
        } else {
            System.out.println(":::: Do you want to see debug messages");
            System.out.println(":::: (Tokens, AST, etc.)? Type true|false>");
            debug = sc.nextBoolean();
        }
        System.out.println("-<=====>--------------------------------<=====>-");
        RuntimeWrapper runtimeWrapper = new RuntimeWrapper(code, debug);
        QType result = runtimeWrapper.run();
        System.out.println("\n-<=====>--------------------------------<=====>-");
        if (result instanceof DirectInstructionType && ((DirectInstructionType) result).i.equals(
                DirectInstruction.EXCEPTION)) {
            System.err.println("Runtime wrapper returned exception instruction!");
            System.err.println("(X) Unhandled exception!");
            System.err.println("Error <" + ((DirectInstructionType) result).data.get("msg") + ">,");
            System.err.println("Additional data: " + ((DirectInstructionType) result).data);
        } else {
            System.out.println("RuntimeWrapper returned " + result.toString());
        }
    }
}
