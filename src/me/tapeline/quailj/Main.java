package me.tapeline.quailj;

import me.tapeline.quailj.language.types.RuntimeStriker;
import me.tapeline.quailj.language.types.RuntimeStrikerTypes;
import me.tapeline.quailj.language.types.QType;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws RuntimeStriker {
        Language.setupAndSelect("en");
        String path;
        Scanner sc = new Scanner(System.in);
        if (args.length == 2) {
            path = args[0];
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
    }
}
