package com.annotator.application.algorithm.SPIP;

import com.annotator.infra.CsvHandler;
import com.annotator.infra.RProcessRunner;
import com.annotator.infra.VcfHandler;
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
public class SPIPAlgorithm {
    private static final Path INPUT_DIR = Paths.get(System.getProperty("user.dir"), "input");

    private final RProcessRunner runner = new RProcessRunner();
    private final VcfHandler vcfHandler = new VcfHandler();

    private static Optional<String> getResult(final String path) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); //skip header
            return Optional.of(reader.readLine());
        } catch (final IOException e) {
            log.error("Annotation process finished with error when reading result {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> handle(final List<SPIPInput> input) {
        return prepareInputFile(input)
                .flatMap(this::runAlgorithm)
                .flatMap(SPIPAlgorithm::getResult);

    }

    private Optional<String> runAlgorithm(final Path filePath) {
        final var fileName = filePath.getFileName().toString();
        final var outputFile = fileName.substring(0, fileName.lastIndexOf('.')) + "spip.txt";
        return runner.runSPIPProcess(fileName, outputFile);
    }

    private Optional<Path> prepareInputFile(final List<SPIPInput> input) {
        final var inputPath = Paths.get(INPUT_DIR.toString(), input.hashCode() + ".vcf");
        return vcfHandler.saveBean(inputPath, new ArrayList<>(input));
    }

}
