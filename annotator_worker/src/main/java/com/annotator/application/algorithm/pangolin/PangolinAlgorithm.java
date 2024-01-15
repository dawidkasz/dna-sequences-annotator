package com.annotator.application.algorithm.pangolin;

import com.annotator.helper.FileHelper;
import com.annotator.infra.CsvHandler;
import com.annotator.infra.PythonProcessRunner;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class PangolinAlgorithm {
    private static final Path INPUT_DIR = Paths.get(System.getProperty("user.dir"), "input");

    private final PythonProcessRunner runner = new PythonProcessRunner();
    private final CsvHandler csvHandler = new CsvHandler();

    public List<String> handle(final List<PangolinInput> input) {
        return prepareInputFile(input)
                .flatMap(this::runAlgorithm)
                .map(FileHelper::readRowsOfHeaderFile)
                .orElseGet(List::of);
    }

    private Optional<String> runAlgorithm(final Path filePath) {
        final var fileName = filePath.getFileName().toString();
        final var outputFile = fileName.substring(0, fileName.lastIndexOf('.')) + ".pangolin";
        return runner.runPangolinProcess(fileName, outputFile);
    }

    private Optional<Path> prepareInputFile(final List<PangolinInput> input) {
        final var inputPath = Paths.get(INPUT_DIR.toString(), UUID.randomUUID() + ".csv");
        return csvHandler.saveBean(inputPath, new ArrayList<>(input));
    }

}
