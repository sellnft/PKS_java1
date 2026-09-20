package util;

import java.io.InputStream;
import java.util.logging.LogManager;

public final class LoggingConfig {

    private LoggingConfig() {};

    public static void setup() {
        try(InputStream is = LoggingConfig.class.getResourceAsStream("/logging.properties")) {
            if (is == null) {
                System.err.println("logging.properties не найден в папке resources");
                return;
            }
            LogManager.getLogManager().readConfiguration(is);
        } catch (Exception e) {
            System.err.println("Не удалось загрузить logging.proprties " + e.getMessage());
        }
    }
}
