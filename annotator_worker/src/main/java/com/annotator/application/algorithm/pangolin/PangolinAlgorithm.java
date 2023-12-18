package com.annotator.application.algorithm.pangolin;

import com.annotator.infra.CsvHandler;
import com.annotator.infra.PythonProcessRunner;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PangolinAlgorithm {
    private final static String OUTPUT_FILE_NAME = "brca.annotated.pangolin";
    private final static String COMPONENT_NAME = "annotator_worker";
    private static final Path INPUT_DIR = Paths.get(System.getProperty("user.dir"), COMPONENT_NAME, "input");


    private final PythonProcessRunner runner = new PythonProcessRunner();
    private final CsvHandler csvHandler = new CsvHandler();

    private static Optional<String> getResult(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); //skip header
            return Optional.of(reader.readLine());
        } catch (final IOException e) {
            log.error("Annotation process finished with error when reading result {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> handle(final List<PangolinInput> input) {
        return prepareInputFile(input)
                .flatMap(this::runAlgorithm)
                .flatMap(PangolinAlgorithm::getResult);

    }

    private Optional<String> runAlgorithm(final Path filePath) {
        final var fileName = filePath.getFileName().toString();
        return runner.runPangolinProcess(fileName, OUTPUT_FILE_NAME);
    }

    private Optional<Path> prepareInputFile(final List<PangolinInput> input) {
        final var inputPath = Paths.get(INPUT_DIR.toString(), String.valueOf(input.hashCode()));
        return csvHandler.saveCsvBean(inputPath, new ArrayList<>(input));
    }

}
