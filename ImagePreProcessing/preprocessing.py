import numpy as np
import PIL
import urllib.request
import cv2
import pika
from PIL import Image
import json
import os 
from MongoConnector import *
import io

RABBIT_USR = os.getenv('RABBITMQUSER') 
RABBIT_PSW = os.getenv('RABBITMQPASSWORD')
RABBIT_QUEUE_NAME = os.getenv('RABBITQUEUENAME')

def preprocessing(imageJson, mongo_connector):
    Photos_JS = json.loads(imageJson)
    print(Photos_JS)
    dim = len(Photos_JS['latest_photos'])
    
    Marts_photos = []
    Names = []
    
    for i in range(dim):
        Marts_photos.append(Photos_JS['latest_photos'][i]['img_src'])

        name = Marts_photos[i]
        buffer = name.split("/")
        buffer = buffer[len(buffer)-1]
        
        if buffer.split(".")[1] == "JPG":
            buffer = buffer.replace("JPG", "jpg")
            
        Names.append(buffer)
        
        img = np.array(PIL.Image.open(urllib.request.urlopen(Marts_photos[i])))
        if len(img.shape) == 2:
            print(img.shape)
            img = cv2.cvtColor(img, cv2.COLOR_GRAY2RGB) 
            print(img.shape)
            
            

        im = Image.fromarray(img)    
        image_bytes = io.BytesIO()
        im.save(image_bytes, format='JPEG')
        image = {
            'img': image_bytes.getvalue(),
            'camera-name' : Photos_JS['latest_photos'][i]['camera']['full_name'],
            'rover' : Photos_JS['latest_photos'][i]['rover']['name']
        }
        
        mongo_connector.writeDocument("base_image",image)  
        print("writed image in DB")
        
def rabbit_reader(mongo_connector):
    credentials = pika.PlainCredentials(RABBIT_USR, RABBIT_PSW)
    parameters = pika.ConnectionParameters('rabbitmq',
                                   5672,
                                   '/',
                                   credentials)

    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()


    def callback(ch, method, properties, body):
        preprocessing(body.decode(), mongo_connector)

    channel.basic_consume(queue=RABBIT_QUEUE_NAME, on_message_callback=callback, auto_ack=True)
    channel.start_consuming()


if __name__ == "__main__":
    print("Started pre-processing")
    mongo_connector = MongoConnector()
    rabbit_reader(mongo_connector)
