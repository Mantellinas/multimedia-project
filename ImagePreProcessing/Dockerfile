FROM python:3.9.10

WORKDIR /app

COPY . . 

RUN apt-get update && apt-get install -y python3-opencv

RUN pip3 --no-cache-dir install -r requirements.txt

ENV PYTHONUNBUFFERED=0

EXPOSE 9000

CMD [ "python3","-u", "preprocessing.py"]