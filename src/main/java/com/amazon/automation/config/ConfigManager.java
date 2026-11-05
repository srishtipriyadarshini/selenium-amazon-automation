package com.amazon.automation.config;

import com.amazon.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public final class ConfigManager {

    private static final Logger LOG = LogManager.getLogger(ConfigManager.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ConfigManager() {
        // Utility class
    }

    private static void loadProperties() {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            PROPERTIES.load(fis);
            LOG.info("Configuration loaded from: {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            // Config is foundational - if we can't read it, fail fast and loud.
            LOG.error("Failed to load config.properties from {}", FrameworkConstants.CONFIG_FILE_PATH, e);
            throw new RuntimeException("Cannot start framework without config.properties", e);
        }
    }

    
    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isEmpty() && !"null".equalsIgnoreCase(systemValue)) {
            // The "null" string check exists because maven-surefire passes
            // unset -D properties as the literal string "null". Took an hour to find that.
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            LOG.warn("Property {} value '{}' is not an int; using default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return (value == null || value.isEmpty()) ? defaultValue : Boolean.parseBoolean(value.trim());
    }
}
