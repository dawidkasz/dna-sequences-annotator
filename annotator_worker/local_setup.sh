#!/bin/sh
mkdir data input output
wget -O ./data/GRCh37.primary_assembly.genome.fa.gz ftp://ftp.ebi.ac.uk/pub/databases/gencode/Gencode_human/release_30/GRCh37_mapping/GRCh37.primary_assembly.genome.fa.gz
wget -O ./data/gencode.v38lift37.annotation.db https://www.dropbox.com/sh/6zo0aegoalvgd9f/AAA9Q90Pi1UqSzX99R_NM803a/gencode.v38lift37.annotation.db
wget -O ./data/transcriptome_hg38.RData https://downloads.sourceforge.net/project/splicing-prediction-pipeline/transcriptome/transcriptome_hg38.RData

docker build -f Dockerfile.base -t pzsp2-worker-base .
