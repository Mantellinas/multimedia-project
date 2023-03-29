import matplotlib.pyplot as plt
import numpy as np
import base64
import io
from MongoConnector import *
from HogSupport import *
from PIL import Image
from scipy.spatial import distance
from skimage import color
from scipy.cluster.hierarchy import dendrogram, linkage

COLLECTION = os.getenv('BASE_IMAGE')
OUTPUTCOLLECTION = os.getenv('CLUSTERING_HOG')

class Hog:
    def __init__(self):     
        self.mongo_connector = MongoConnector()

    def computate(self):
        images = []
        Names = []
        final_img = []
        feat_ims = []
        fd_list = []
        
        results = self.mongo_connector.getAllDocument(COLLECTION)

        for entry in results:
            images.append(base64.b64decode(entry['img']['$binary']['base64']))
            Names.append(entry['_id']['$oid'])

        for image in images:
            im = Image.open(io.BytesIO(image))
            im = color.rgb2gray(im)
            final_img.append(np.array(im))

        i=0
        for img in final_img:
            # if i > 20:
            #     break

            hog = Hog_descriptor(img, cell_size=16, bin_size=16)
            vector, image = hog.extract()
            print("processed "+ str(i)+" of "+str(len(final_img)))
            
            fd_list.append(np.mean(np.array(vector),axis=1))

            im = Image.fromarray(((image * 255).astype(np.uint8)))  
            image_hog= io.BytesIO()
            im.save(image_hog, format='JPEG') 

            feat_ims.append({'baseimageid': ObjectId(Names[i]),
                            'featimg': image_hog.getvalue(),
                            'imageid' : i
                            })
            i+=1

        n = len(fd_list)
        
        distance_matrix = np.zeros((n, n))  

        for j in range(n):
            fd_i = fd_list[j]
            for k in range(n): #j
                fd_k = fd_list[k]
                
                if len(fd_i) != len(fd_k):
                    fd_i = fd_i[: min(len(fd_i), len(fd_k))]
                    fd_k = fd_k[: min(len(fd_i), len(fd_k))]
                    distance_matrix[j, k] = distance.jensenshannon(fd_i, fd_k) 
                else:
                    distance_matrix[j, k] = distance.jensenshannon(fd_i, fd_k)
        

        distance_matrix = np.maximum(distance_matrix, distance_matrix.transpose())
        distance_matrix = np.array(distance_matrix)
        #distance_matrix[~np.isnan(distance_matrix)]=0
        distance_matrix[~np.isfinite(distance_matrix)]=0

        cond_distance_matrix = distance.squareform(distance_matrix)
        
        Z = linkage(distance_matrix, method='average')    
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
        print("HOG completed")

