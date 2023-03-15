import matplotlib.pyplot as plt
import numpy as np
import base64
import io
from MongoConnector import *
from PIL import Image
from skimage.feature import hog
from scipy.spatial import distance
from scipy.cluster.hierarchy import dendrogram, linkage

COLLECTION = os.getenv('BASE_IMAGE')
OUTPUTCOLLECTION = os.getenv('CLUSTERING_HOG')

class Hog:
    def __init__(self):     
        self.mongo_connector = MongoConnector()

    def computate(self):
        Names = []
        final_img = []
        images = []

        results = self.mongo_connector.getAllDocument(COLLECTION)

        for entry in results:
            images.append(base64.b64decode(entry['img']['$binary']['base64']))
            Names.append(entry['_id']['$oid'])

        for image in images:
            im = Image.open(io.BytesIO(image))
            final_img.append(np.array(im))
            

        # creating a list to store HOG feature vectors
        fd_list = []

        feat_ims = []
        n = len(final_img)

        for i in range(n):
            fd, hog_image = hog(final_img[i], orientations=9, pixels_per_cell=(
                64, 64), cells_per_block=(2, 2), visualize=True, channel_axis=-1)

            fd_list.append(fd)          

            im = Image.fromarray(((hog_image * 255).astype(np.uint8)))  
            image_hog= io.BytesIO()

            im.convert('RGB').save(image_hog, format='png') 
            feat_ims.append({'baseimageid': Names[i],
                            'featimg': image_hog.getvalue(),
                            'imageid' : i
                            })


        distance_matrix = np.zeros((n, n))

        for i in range(n):
            fd_i = fd_list[i]
            for k in range(i):
                fd_k = fd_list[k]
               
                distance_matrix[i, k] = distance.jensenshannon(fd_i, fd_k)
        distance_matrix = np.maximum(distance_matrix, distance_matrix.transpose())

        cond_distance_matrix = distance.squareform(distance_matrix)

        Z = linkage(cond_distance_matrix, method='ward')    
        print(Z)
        base64_dendrogram = io.BytesIO()

        plt.figure(figsize=(12, 6))
        dendrogram(Z, color_threshold=0.2, show_leaf_counts=True)
        plt.savefig(base64_dendrogram, format='png', bbox_inches='tight')
        base64_dendrogram.seek(0)

        my_base64_dendrogram= base64.b64encode(base64_dendrogram.read())  

        document = {
            'feat_imgs' : feat_ims,
            'dendrogram' : my_base64_dendrogram.decode()
        }

        self.mongo_connector.writeDocument(OUTPUTCOLLECTION, document)

