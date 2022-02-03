package me.tapeline.quailj;

import me.tapeline.quailj.language.types.RuntimeStriker;
import me.tapeline.quailj.language.types.RuntimeStrikerTypes;
import me.tapeline.quailj.language.types.QType;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws RuntimeStriker {
        Language.setupAndSelect("en");
        String path;
        boolean useFancyFrame = false;
        Scanner sc = new Scanner(System.in);
        if (args.length == 3) {
            path = args[0];
            useFancyFrame = Boolean.parseBoolean(args[2]);
        } else {
            System.out.println("-<=====>- Welcome to Quail interpreter -<=====>-");
            System.out.println("::::            Quail  v0.5-beta            ::::");
            System.out.println("::::           (C) Tapeline, 2021           ::::");
            System.out.println("-<=====>--------------------------------<=====>-");
            System.out.println(":::: Type in path to Quail Source Code> ");
            path = "/home/tapeline/test.q";//sc.next(); //"/home/tapeline/test.q";//
        }
        String code = QFileReader.read(path);
        boolean debug;
        if (args.length == 3) {
            debug = Boolean.parseBoolean(args[1]);
        } else {
            System.out.println(":::: Do you want to see debug messages");
            System.out.println(":::: (Tokens, AST, etc.)? Type true|false>");
            debug = sc.nextBoolean();
        }
        if (useFancyFrame) System.out.println("-<=====>--------------------------------<=====>-");
        RuntimeWrapper runtimeWrapper = new RuntimeWrapper(code, debug, path);
        try {
            QType result = runtimeWrapper.run();
            if (debug) System.out.println("Runtime returned " + result.toString());
        } catch (RuntimeStriker striker) {
            if (striker.type.equals(RuntimeStrikerTypes.EXCEPTION)) {
                System.err.println("In file: " + path + ", line " + striker.posline + ", column " +
                        striker.poschar + ":\n");
                System.err.println("An error occured during the execution of program! Details:");
                String[] strings = striker.msg.split(":");
                for (int i = 0; i < strings.length - 1; i++) {
                    for (int j = 0; j < i; j++)
                        System.err.print(" ");
                    System.err.println("At " + strings[i]);
                }
                System.err.println(strings[strings.length - 1]);
            } else throw striker;
        }
        if (useFancyFrame) System.out.println("\n-<=====>--------------------------------<=====>-");
    }
}
