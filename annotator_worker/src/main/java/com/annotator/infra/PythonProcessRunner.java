package com.annotator.infra;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class PythonProcessRunner {
    private static final String PWD = System.getProperty("user.dir");
//    private static final Path PYTHON_INT = Paths.get(PWD, "/venv/bin/pangolin"); //for local development
    private static final Path PYTHON_INT = Paths.get("pangolin"); //for docker
    private static final Path DATA_DIR = Paths.get(PWD, "data");
    private static final Path GENOME = Paths.get(DATA_DIR.toString(), "GRCh37.primary_assembly.genome.fa.gz");
    private static final Path GENCODE = Paths.get(DATA_DIR.toString(), "gencode.v38lift37.annotation.db");
    private static final Path INPUT_DIR = Paths.get(PWD, "input");
    private static final Path OUTPUT_DIR = Paths.get(PWD, "output");

    public Optional<String> runPangolinProcess(final String inputFile, final String outputFile) {
        final var outputPath = Paths.get(OUTPUT_DIR.toString(), outputFile).toString();

        final String[] command = {
                PYTHON_INT.toString(),
                Paths.get(INPUT_DIR.toString(), inputFile).toString(),
                GENOME.toString(),
                GENCODE.toString(),
                outputPath
        };
        log.info("Created a process with command: `{}`", String.join(" ", command));
        final ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        try {
            final var process = pb.start();
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Annotation process finished with error({})", exitCode);
                return Optional.empty();
            }
            log.info("Annotation process finished OK");
        } catch (final IOException | InterruptedException e) {
            log.error("There was error when running pagolin algorithm {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(outputPath + inputFile.substring(inputFile.lastIndexOf('.')));
    }
}