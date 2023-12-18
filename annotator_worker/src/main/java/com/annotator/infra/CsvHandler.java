package com.annotator.infra;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CsvHandler {
    public Optional<Path> saveCsvBean(final Path path, final List<CsvBean> sampleData) {

        try (final Writer writer = new FileWriter(path.toString())) {
            final StatefulBeanToCsv<CsvBean> sbc = new StatefulBeanToCsvBuilder<CsvBean>(writer)
                    .withQuotechar('\'')
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            sbc.write(sampleData);
        } catch (final IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            log.error("Error when saving csv file {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(path);
    }
}
