package com.annotator.core.infrastructure.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CsvHandler {
    public Optional<InputStreamSource> convertToCsvStream(final List<CsvBean> csvData) {

        try (final Writer writer = new StringWriter()) {
            final var sbc = new StatefulBeanToCsvBuilder<CsvBean>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            sbc.write(csvData);
            return Optional.of(new ByteArrayResource(writer.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (final IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            log.warn("Error when converting to csv data", e);
            return Optional.empty();
        }
    }
}
