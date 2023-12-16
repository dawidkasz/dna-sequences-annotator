package com.annotator;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class PythonProcessRunner {
    public Process runPangolinProcess(String inputFile, String outputFile) throws IOException {
        String pwd = System.getProperty("user.dir");

        String[] command = {
//                For local development
//                pwd + "/../venv/bin/pangolin",
//                For docker
                "pangolin",
                pwd + "/input/" + inputFile,
                pwd + "/data/GRCh37.primary_assembly.genome.fa.gz",
                pwd + "/data/gencode.v38lift37.annotation.db",
                pwd + "/output/" + outputFile
        };
        log.info("Created a process with command: `{}`", String.join(" ", command));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        return pb.start();
    }
}