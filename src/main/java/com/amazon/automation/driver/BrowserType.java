package com.amazon.automation.driver;

import java.util.Arrays;


public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE,
    SAFARI;

    
    public static BrowserType fromString(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Browser name is null or empty in config");
        }
        return Arrays.stream(values())
                .filter(b -> b.name().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser: " + name + ". Supported: " + Arrays.toString(values())));
    }
}
