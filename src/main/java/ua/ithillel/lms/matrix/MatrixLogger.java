package ua.ithillel.lms.matrix;

import ua.ithillel.lms.logger.Logger;
import ua.ithillel.lms.logger.LoggerConfiguration;
import ua.ithillel.lms.logger.LoggerConfigurationLoader;
import ua.ithillel.lms.logger.file.FileLogger;
import ua.ithillel.lms.logger.file.FileLoggerConfiguration;
import ua.ithillel.lms.logger.file.FileLoggerConfigurationLoader;

public class MatrixLogger {

    private static Logger logger;

    public static Logger create(String path) {
        LoggerConfigurationLoader lcl = new FileLoggerConfigurationLoader();
        LoggerConfiguration lc = lcl.load(path);
        logger = new FileLogger((FileLoggerConfiguration) lc);
        return logger;
    }

    public static Logger get() {
        return logger;
    }

}
