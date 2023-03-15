from PIL import Image
from MongoConnector import *
import base64
import numpy as np
import cv2 as cv
import io

COLLECTION = os.getenv('BASE_IMAGE')
OUTPUTCOLLECTION = os.getenv('FAST')

class Corner_Detector_FAST:
    def __init__(self):
        self.mongo_connector = MongoConnector()

    def computate(self):
        results = self.mongo_connector.getAllDocument(COLLECTION)

        Names = []
        images = []
        for entry in results:
            images.append(base64.b64decode(entry['img']['$binary']['base64']))
            Names.append(entry['_id'])

        i=0
        for image in images:
            img = Image.open(io.BytesIO(image))
            img = np.array(img)

            gray = cv.cvtColor(img,cv.COLOR_BGR2GRAY)
            gray = np.float32(gray)
            dst = cv.cornerHarris(gray,2,3,0.04)
            dst = cv.dilate(dst,None)
            img[dst>0.01*dst.max()]=[0,0,255]
            
            im = Image.fromarray(img)    
            image_bytes = io.BytesIO()
            im.save(image_bytes, format='JPEG')

            image_mod = {
                'img': image_bytes.getvalue(),
                'baseimageid':Names[i]['$oid']
            }

            self.mongo_connector.writeDocument(OUTPUTCOLLECTION,image_mod)
            i+=1
