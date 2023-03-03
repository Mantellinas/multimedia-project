#!/usr/bin/env python
import pika

credentials = pika.PlainCredentials('myuser', 'mypassword')
parameters = pika.ConnectionParameters('localhost',
                                   5672,
                                   '/',
                                   credentials)
connection = pika.BlockingConnection(parameters)

channel = connection.channel()

# channel.queue_declare(queue='logstash-sink-queue', durable=True)
i=0
while True:
    channel.basic_publish(exchange='', routing_key='logstash-sink-queue', body="Hello")
    i+=1

#connection.close()