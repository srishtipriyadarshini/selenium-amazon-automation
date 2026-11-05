package com.amazon.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ExcelReader {

    private static final Logger LOG = LogManager.getLogger(ExcelReader.class);

    private ExcelReader() {
    }

    
    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LOG.error("Sheet '{}' not found in {}", sheetName, filePath);
                return rows;
            }

            DataFormatter formatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                LOG.warn("Sheet '{}' has no header row", sheetName);
                return rows;
            }

            // Cache header names in column order
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(formatter.formatCellValue(cell).trim());
            }

            // Iterate data rows. lastRowNum can lie if cells were ever touched
            // then cleared, hence the null check on row.
            int lastRow = sheet.getLastRowNum();
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                // LinkedHashMap preserves insertion order - keeps debug output sane.
                Map<String, String> rowMap = new LinkedHashMap<>();
                boolean anyValue = false;
                for (int c = 0; c < headers.size(); c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    String value = (cell == null) ? "" : formatter.formatCellValue(cell).trim();
                    rowMap.put(headers.get(c), value);
                    if (!value.isEmpty()) anyValue = true;
                }

                // Skip completely blank rows - common when users add empty rows
                // for visual spacing.
                if (anyValue) rows.add(rowMap);
            }

            LOG.info("Read {} data rows from sheet '{}'", rows.size(), sheetName);

        } catch (IOException e) {
            LOG.error("Failed to read Excel: {}", filePath, e);
            throw new RuntimeException("Excel read failed: " + filePath, e);
        }

        return rows;
    }
}
