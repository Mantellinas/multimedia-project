import base64
import io
import numpy as np
from PIL import Image
from skimage.color import rgb2gray
from skimage.filters import sobel
from skimage.segmentation import felzenszwalb, slic, quickshift, watershed
from skimage.segmentation import mark_boundaries
from MongoConnector import *

COLLECTION = os.getenv('BASE_IMAGE')
OUTPUTCOLLECTION = os.getenv('SLIC')

class Slic:
    def __init__(self):
        self.mongo_connector = MongoConnector()

    def computate(self):
        print("Slic started")
        results = self.mongo_connector.getAllDocument(COLLECTION)

        Names = []
        images = []
        for entry in results:
            images.append(base64.b64decode(entry['img']['$binary']['base64']))
            Names.append(entry['_id']['$oid'])

        i=0
        for imgs in images:
            print("slic: "+str(i)+" of "+str(len(images)))
            img = Image.open(io.BytesIO(imgs))
            img = np.array(img)
        
            segments_fz = felzenszwalb(img, scale=100, sigma=0.5, min_size=50)
            segments_slic = slic(img, n_segments=250, compactness=10, sigma=1,start_label=1)
            segments_quick = quickshift(img, kernel_size=3, max_dist=6, ratio=0.5)
            gradient = sobel(rgb2gray(img))
            segments_watershed = watershed(gradient, markers=250, compactness=0.001)

            image = mark_boundaries(img, segments_fz)
            im = Image.fromarray((image * 255).astype(np.uint8))  
            image_bytes_fz = io.BytesIO()
            im.save(image_bytes_fz, format='png')
            

            image = mark_boundaries(img, segments_slic)
            im = Image.fromarray((image * 255).astype(np.uint8))  
            image_bytes_slic = io.BytesIO()
            im.save(image_bytes_slic, format='png')


            image = mark_boundaries(img, segments_quick)
            im = Image.fromarray((image * 255).astype(np.uint8))
            image_bytes_quick = io.BytesIO()
            im.save(image_bytes_quick, format='png')


            image = mark_boundaries(img, segments_watershed)
            im = Image.fromarray((image * 255).astype(np.uint8))
            image_bytes_watershed = io.BytesIO()
            im.save(image_bytes_watershed, format='png')


            res_json = {
                'baseimageid':ObjectId(Names[i]),
                'felzenszwalbSegment':len(np.unique(segments_fz)),
                'slicSegment': len(np.unique(segments_slic)),
                'quickshiftSegment': len(np.unique(segments_quick)),
                'watershedSegment' : len(np.unique(segments_watershed)),
                'felzenszwalbImg': image_bytes_fz.getvalue(),
                'SLICImg' : image_bytes_slic.getvalue(),
                'quickshiftImg' : image_bytes_quick.getvalue(),
                'watershedImg' : image_bytes_watershed.getvalue()
            }
            self.mongo_connector.writeDocument(OUTPUTCOLLECTION,res_json)
            i+=1
        print("Slic completed")
