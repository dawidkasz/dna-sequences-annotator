FROM gradle:jdk17-alpine
WORKDIR /app
ARG JAR_FILE=build/libs/annotator_worker-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

RUN apt-get update \
 && command -v python3 >/dev/null 2>&1 || { echo >&2 "Installing Python..."; apt-get install -y python3; } \
 && command -v pip3 >/dev/null 2>&1 || { echo >&2 "Installing pip..."; apt-get install -y python3-pip; } \
 && command -v git >/dev/null 2>&1 || { echo >&2 "Installing git..."; apt-get install -y git; } \
 && rm -rf /var/lib/apt/lists/*

RUN ln -s /usr/bin/python3 /usr/local/bin/python \
 && ln -s /usr/bin/pip3 /usr/local/bin/pip \

RUN pip install "setuptools<58" --upgrade && pip install gffutils biopython pandas pyfastx
RUN pip install torch --index-url https://download.pytorch.org/whl/cpu

RUN git clone https://github.com/tkzeng/Pangolin.git \
 && cd Pangolin \
 && pip install .

RUN mkdir data
RUN wget -O ./data/GRCh38.primary_assembly.genome.fa.gz https://ftp.ebi.ac.uk/pub/databases/gencode/Gencode_human/release_38/GRCh38.primary_assembly.genome.fa.gz
RUN wget -O ./data/gencode.v38.annotation.db https://www.dropbox.com/sh/6zo0aegoalvgd9f/AADOhGYJo8tbUhpscp3wSFj6a/gencode.v38.annotation.db

RUN gradle build

CMD ["java", "-jar", "app.jar"]