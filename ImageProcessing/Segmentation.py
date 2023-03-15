import cv2 as cv
import numpy as np
import base64
import io 
import os
from MongoConnector import *
from PIL import Image

COLLECTION = os.getenv('BASE_IMAGE')
OUTPUTCOLLECTION = os.getenv('SEGMENTATION')

class Segmentation:
    def __init__(self):
        self.mongo_connector = MongoConnector()

    def computate(self):
        results = self.mongo_connector.getAllDocument(COLLECTION)

        Names = []
        images = []
        for entry in results:
            images.append(base64.b64decode(entry['img']['$binary']['base64']))
            Names.append(entry['_id']['$oid'])

        for image in images:
            img = Image.open(io.BytesIO(image))
            img = np.array(img)
            gray = cv.cvtColor(img,cv.COLOR_BGR2GRAY)
            ret, thresh = cv.threshold(gray,0,255,cv.THRESH_BINARY_INV+cv.THRESH_OTSU)


            im = Image.fromarray((img))  
            image_bytes_base= io.BytesIO()
            im.save(image_bytes_base, format='png')


            im = Image.fromarray((gray))  
            image_bytes_gray= io.BytesIO()
            im.save(image_bytes_gray, format='png')


            im = Image.fromarray((thresh))  
            image_bytes_thresh= io.BytesIO()
            im.save(image_bytes_thresh, format='png')

            # noise removal
            kernel = np.ones((3,3),np.uint8)
            opening = cv.morphologyEx(thresh,cv.MORPH_OPEN,kernel, iterations = 2)
            # sure background area
            sure_bg = cv.dilate(opening,kernel,iterations=3)
            # Finding sure foreground area
            dist_transform = cv.distanceTransform(opening,cv.DIST_L2,5)
            ret, sure_fg = cv.threshold(dist_transform,0.7*dist_transform.max(),255,0)
            # Finding unknown region
            sure_fg = np.uint8(sure_fg)
            unknown = cv.subtract(sure_bg,sure_fg) 


            im = Image.fromarray((opening))  
            image_bytes_opening= io.BytesIO()
            im.save(image_bytes_opening, format='png')


            im = Image.fromarray((sure_bg))  
            image_bytes_sure_bg= io.BytesIO()
            im.save(image_bytes_sure_bg, format='png')


            im = Image.fromarray((sure_fg))  
            image_bytes_sure_fg= io.BytesIO()
            im.save(image_bytes_sure_fg, format='png')

            ret, markers = cv.connectedComponents(sure_fg)
            markers = markers+1
            markers[unknown==255] = 0

            im =Image.fromarray((markers * 255).astype(np.uint8))
            image_bytes_img_markers = io.BytesIO()
            im.save(image_bytes_img_markers, format='png')

            markers = cv.watershed(img,markers)
            img[markers == -1] = [255,0,0]
            im =Image.fromarray((markers * 255).astype(np.uint8))
            image_bytes_img_markers_ws = io.BytesIO()
            im.save(image_bytes_img_markers_ws, format='png')


            im =Image.fromarray((img * 255).astype(np.uint8))
            image_bytes_img_markers_img = io.BytesIO()
            im.save(image_bytes_img_markers_img, format='png')


            i=1
            res_json = {
                'baseimageid': Names[i],
                'imgoriginale' : image_bytes_base.getvalue(),
                'imggrey' : image_bytes_gray.getvalue(),
                'imgthresh' : image_bytes_thresh.getvalue(),
                'imgopening' : image_bytes_opening.getvalue(),
                'imgsure_bg' : image_bytes_sure_bg.getvalue(),
                'imgsure_fg': image_bytes_sure_fg.getvalue(),
                'imgmarkers' : image_bytes_img_markers.getvalue(),
                'imgmarkers_watershed' : image_bytes_img_markers_ws.getvalue(),
                'imgmarkers_img_border' : image_bytes_img_markers_img.getvalue()
            }
            i+=1
            self.mongo_connector.writeDocument(OUTPUTCOLLECTION,res_json)

