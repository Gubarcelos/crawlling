package com.crawlling.backend.infra;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfigs {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseURL() {
        return properties.getProperty("BASE_URL");
    }
}
