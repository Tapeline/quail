package me.tapeline.quarkj;

import me.tapeline.quarkj.language.types.QType;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("-<=====>- Welcome to Quark interpreter -<=====>-");
        System.out.println("::::            Quark v0.2-alpha            ::::");
        System.out.println("::::           (C) Tapeline, 2021           ::::");
        System.out.println("-<=====>--------------------------------<=====>-");
        System.out.println(":::: Type in path to Quark Source Code> ");
        Scanner sc = new Scanner(System.in);
        String path = sc.next();
        String code = QFileReader.read(path);
        System.out.println(":::: Do you want to see debug messages");
        System.out.println(":::: (Tokens, AST, etc.)? Type true|false>");
        boolean debug = sc.nextBoolean();
        System.out.println("-<=====>--------------------------------<=====>-");
        RuntimeWrapper runtimeWrapper = new RuntimeWrapper(code, debug);
        QType result = runtimeWrapper.run();
        System.out.println("\n-<=====>--------------------------------<=====>-");
        System.out.println("RuntimeWrapper returned " + result.toString());
        System.out.println(":::: QInterpreter ended.");
        System.out.println("-<=====>--------------------------------<=====>-");
    }
}
