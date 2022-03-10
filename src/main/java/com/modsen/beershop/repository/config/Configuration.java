package com.modsen.beershop.repository.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    public static final String PROPERTIES_FILE = "src/main/resources/config.properties";
    public static final String DB_DRIVER = "db.driver";
    public static final String DB_URL = "db.URL";
    public static final String DB_USER = "db.user";
    public static final String DB_PASSWORD = "db.password";
    public static final String ABV_MIN = "validation.abv.min";
    public static final String ABV_MAX = "validation.abv.max";
    public static final String CONTAINER_MIN = "validation.container.min";
    public static final String CONTAINER_MAX = "validation.container.max";
    public static final String IBU_MIN = "validation.ibu.min";
    public static final String IBU_MAX = "validation.ibu.max";
    public static final String PAGE_SIZE_MIN = "validation.pageSize.min";
    public static final String PAGE_SIZE_MAX = "validation.pageSize.max";

    private final Properties config = new Properties();

    public Configuration() {
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE);
            config.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getURL() {
        return config.getProperty(DB_URL);
    }

    public String getUser() {
        return config.getProperty(DB_USER);
    }

    public String getPassword() {
        return config.getProperty(DB_PASSWORD);
    }

    public String getDriver() {
        return config.getProperty(DB_DRIVER);
    }

    public Double getAbvMin() {
        return Double.valueOf(config.getProperty(ABV_MIN));
    }

    public Double getAbvMax() {
        return Double.valueOf(config.getProperty(ABV_MAX));
    }

    public Double getContainerMin() {
        return Double.valueOf(config.getProperty(CONTAINER_MIN));
    }

    public Double getContainerMax() {
        return Double.valueOf(config.getProperty(CONTAINER_MAX));
    }

    public Integer getIbuMin() {
        return Integer.valueOf(config.getProperty(IBU_MIN));
    }

    public Integer getIbuMax() {
        return Integer.valueOf(config.getProperty(IBU_MAX));
    }

    public Integer getPageSizeMin() {
        return Integer.valueOf(config.getProperty(PAGE_SIZE_MIN));
    }

    public Integer getPageSizeMax() {
        return Integer.valueOf(config.getProperty(PAGE_SIZE_MAX));
    }
}
