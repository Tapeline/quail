package me.tapeline.quailj.utils;

import me.tapeline.quailj.typing.objects.errors.ErrorMessage;

import java.io.File;

public class ErrorFormatter {

    public static String formatError(String code, int line, int character,
                                     int length, String errorType, String message) {
        String[] lines = code.split("\n");
        int index = line != 0? line - 1 : 0;
        StringBuilder error = new StringBuilder();
        error.append("Error on line ").append(line).append(" character ").append(character).append(":\n");
        for (int i = (index >= 2? index - 2 : index); i < line; i++) {
            StringBuilder tabs = new StringBuilder("        ");
            String lineNumStr = Integer.toString(i + 1);
            tabs.replace(tabs.length() - lineNumStr.length(), tabs.length(), lineNumStr);
            if (i + 1 == line) tabs.replace(0, 1, ">");
            error.append(tabs).append(" | ").append(lines[i]).append("\n");
        }
        StringBuilder underline = new StringBuilder("           ");
        for (int i = 0; i < character; i++)
            underline.append(" ");
        for (int i = 0; i < length; i++)
            underline.append("^");
        error.append(underline).append("\n");
        int maxLength = 0;
        for (int i = 0; i < lines.length; i++)
            if (lines[i].length() > maxLength)
                maxLength = lines[i].length();
        for (int i = 0; i < Math.max(underline.length(), maxLength); i++)
            error.append("=");
        error.append("\n").append(errorType).append(":\n").append(message);
        return error.toString();
    }

    public static String formatError(String code, ErrorMessage message) {
        return formatError(code, message.line, message.character,
                message.length, message.errorType, message.message);
    }

}
