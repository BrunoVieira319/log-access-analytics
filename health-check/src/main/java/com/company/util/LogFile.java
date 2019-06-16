package com.company.util;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public class LogFile {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static Handler logFile;

    public static Handler getLogFile() {
        logger.atInfo().log("Getting a file to record logs");
        if (logFile == null) {
            try {
                logFile = new FileHandler("health-check-logs.xml");
            } catch (IOException e) {
                logger.atWarning()
                        .withStackTrace(StackSize.SMALL)
                        .log("Error on creating file for logs");
            }
        }
        return logFile;
    }
}
