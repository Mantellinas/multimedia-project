FROM python:3.9.10

ENV TF_CPP_MIN_LOG_LEVEL="2"

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update -y

RUN apt-get install -y python3-opencv

RUN apt-get install -y tzdata

WORKDIR /app

COPY . . 

#RUN pip3 --no-cache-dir install -r requirements.txt

RUN pip3 install -r requirements.txt

ENV PYTHONUNBUFFERED=0

CMD [ "python3","-u", "processingAPI.py"]