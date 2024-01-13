# Annotation worker

## Local development
Download dependencies:
```shell
# while in annotation_worker directory
python -m venv ../venv
pip install "setuptools<58" --upgrade
pip install gffutils biopython pandas pyfastx pyvcf
pip install torch --index-url https://download.pytorch.org/whl/cpu
```
Download [Pangolin](https://github.com/tkzeng/Pangolin):
```shell
cd ..
git clone https://github.com/tkzeng/Pangolin.git && cd Pangolin
pip install .
```

Also, to download gtf files:
```shell
chmod +x local_setup.sh
./local_setup.sh
```

```shell
 ./gradlew build && docker build -t annotation_worker_dev:latest .
```

Change:
- `PythonProcessRunner.java` `pangolin` for local development