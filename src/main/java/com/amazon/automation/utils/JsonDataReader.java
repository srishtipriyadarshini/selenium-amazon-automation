package com.amazon.automation.utils;

import com.amazon.automation.constants.FrameworkConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;


public final class JsonDataReader {

    private static final Logger LOG = LogManager.getLogger(JsonDataReader.class);
    private static volatile JsonNode rootNode;
    private static final Object LOCK = new Object();

    private JsonDataReader() {
    }

    private static JsonNode root() {
        if (rootNode == null) {
            synchronized (LOCK) {
                if (rootNode == null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        rootNode = mapper.readTree(new File(FrameworkConstants.TESTDATA_JSON_PATH));
                        LOG.info("Loaded JSON test data from {}", FrameworkConstants.TESTDATA_JSON_PATH);
                    } catch (IOException e) {
                        LOG.error("Failed to load JSON test data", e);
                        throw new RuntimeException("Cannot read testdata.json", e);
                    }
                }
            }
        }
        return rootNode;
    }
    public static String getString(String path) {
        JsonNode node = navigate(path);
        return (node == null || node.isMissingNode() || node.isNull()) ? null : node.asText();
    }

    public static JsonNode get(String path) {
        return navigate(path);
    }

    private static JsonNode navigate(String dottedPath) {
        JsonNode current = root();
        for (String segment : dottedPath.split("\\.")) {
            if (current == null) return null;
            current = current.path(segment);
        }
        return current;
    }
}
