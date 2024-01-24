package com.annotator.infra;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class RProcessRunner {
    private static final String PWD = System.getProperty("user.dir");
    private static final Path SPIP_INT = Paths.get(PWD, "SPiP");
    private static final Path DATA_DIR = Paths.get(PWD, "data");
    private static final Path TRANSCRIPT_SEQUENCES= Paths.get(DATA_DIR.toString(), "transcriptome_hg38.RData");
    private static final Path INPUT_DIR = Paths.get(PWD, "input");
    private static final Path OUTPUT_DIR = Paths.get(PWD, "output");

    public Optional<String> runSPIPProcess(final String inputFile, final String outputFile) {
        final var outputPath = Paths.get(OUTPUT_DIR.toString(), outputFile).toString();

        final String[] command = {
                "Rscript",
                Paths.get(SPIP_INT.toString(), "SPiPv2.1_main.r").toString(),
                "-I",
                Paths.get(INPUT_DIR.toString(), inputFile).toString(),
                "-O",
                outputPath,
                "--transcriptome",
                TRANSCRIPT_SEQUENCES.toString()
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
        return Optional.of(outputPath);
    }
}