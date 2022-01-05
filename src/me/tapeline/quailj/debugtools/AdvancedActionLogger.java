package me.tapeline.quailj.debugtools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdvancedActionLogger {

    public String logText = "";

    public void log(Object... log) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String sDate = "[" + formatter.format(date) + " INFO] ";
        StringBuilder data = new StringBuilder();
        for (Object o : log)
            data.append(o.toString()).append(" ");
        logText += (sDate + data + "\n");
    }

    public void warn(Object... log) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String sDate = "[" + formatter.format(date) + " WARN] ";
        StringBuilder data = new StringBuilder();
        for (Object o : log)
            data.append(o.toString()).append(" ");
        logText += (sDate + data + "\n");
    }

    public void err(Object... log) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String sDate = "[" + formatter.format(date) + " ERROR] ";
        StringBuilder data = new StringBuilder();
        for (Object o : log)
            data.append(o.toString()).append(" ");
        logText += (sDate + data + "\n");
    }

}
