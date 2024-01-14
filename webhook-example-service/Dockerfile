FROM python:3.8-slim
WORKDIR /usr/src/app
COPY service.py service.py
RUN pip install --no-cache-dir fastapi uvicorn
EXPOSE 8000
CMD ["uvicorn", "service:app", "--host", "0.0.0.0", "--port", "8000"]