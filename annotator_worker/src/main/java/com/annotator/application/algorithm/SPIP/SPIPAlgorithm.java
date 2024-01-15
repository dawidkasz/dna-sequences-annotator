package com.annotator.application.algorithm.SPIP;

import com.annotator.helper.FileHelper;
import com.annotator.infra.RProcessRunner;
import com.annotator.infra.VcfHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SPIPAlgorithm {
    private static final Path INPUT_DIR = Paths.get(System.getProperty("user.dir"), "input");
    private final RProcessRunner runner = new RProcessRunner();
    private final VcfHandler vcfHandler = new VcfHandler();

    public List<String> handle(final List<SPIPInput> input) {
        return prepareInputFile(input)
                .flatMap(this::runAlgorithm)
                .map(FileHelper::readRowsOfHeaderFile)
                .orElseGet(List::of);
    }

    private Optional<String> runAlgorithm(final Path filePath) {
        final var fileName = filePath.getFileName().toString();
        final var outputFile = fileName.substring(0, fileName.lastIndexOf('.')) + "spip.txt";
        return runner.runSPIPProcess(fileName, outputFile);
    }

    private Optional<Path> prepareInputFile(final List<SPIPInput> input) {
        final var inputPath = Paths.get(INPUT_DIR.toString(), UUID.randomUUID() + ".txt");
        return vcfHandler.saveBean(inputPath, new ArrayList<>(input));
    }

}
