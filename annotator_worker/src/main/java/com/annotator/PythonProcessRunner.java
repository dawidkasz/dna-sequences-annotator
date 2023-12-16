package com.annotator;

import java.io.IOException;

public class PythonProcessRunner {
    public Process runPangolinProcess(String inputFile, String outputFile) throws IOException {
        String pwd = System.getProperty("user.dir");

        String[] command = {
//                For local development
                pwd + "/../venv/bin/pangolin",
                "pangolin",
                pwd + "/input/" + inputFile,
                pwd + "/data/GRCh37.primary_assembly.genome.fa.gz",
                pwd + "/data/gencode.v38lift37.annotation.db",
                pwd + "/output/" + outputFile
        };

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        return pb.start();
    }
}