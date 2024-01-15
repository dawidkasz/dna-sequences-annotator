package com.annotator.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FileHelper {

    public static List<String> readRowsOfHeaderFile(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); //skip header
            return reader.lines().toList();
        } catch (final IOException e) {
            log.error("Error when reading file {}", e.getMessage());
            return List.of();
        }
    }
}
