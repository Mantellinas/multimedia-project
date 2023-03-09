from keras.applications.vgg16 import VGG16 
from keras.models import Model
from keras.applications.vgg16 import preprocess_input 
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from PIL import Image
from MongoConnector import *
import numpy as np
import base64
import io

COLLECTION = os.getenv('BASE_IMAGE')
OUTPUTCOLLECTION = os.getenv('CLUSTERING_KMEANS')

class Clustering_KMeans_PCA:
    def __init__(self):
        self.mongo_connector = MongoConnector()
        
    def extract_features(self,file, model):
        image = Image.open(io.BytesIO(file))
        image = image.resize((224, 224))
        img = np.array(image) 
        reshaped_img = img.reshape(1,224,224,3) 
        imgx = preprocess_input(reshaped_img)
        features = model.predict(imgx, use_multiprocessing=True)
        return features

    def computate(self):
        Names = []  
        model = VGG16()
        model = Model(inputs = model.inputs, outputs = model.layers[-2].output)

        results = self.mongo_connector.getAllDocument(COLLECTION)

        res = []
        for entry in results:
            res.append(base64.b64decode(entry['img']['$binary']['base64']))
            Names.append(entry['_id'])
            
        data = {}
        for img in res:
            feat = self.extract_features(img,model)
            data[img] = feat

        filenames = np.array(list(data.keys())) #check
        feat = np.array(list(data.values()))
        feat = feat.reshape(-1,4096)
        
        n_comp = feat.shape[0]

        pca = PCA(n_components=n_comp, random_state=22)
        pca.fit(feat)
        x = pca.transform(feat)

        kmeans = KMeans(n_clusters=3)
        kmeans.fit(x)

        for i in range(n_comp):
            cluster = {'base_image_id':Names[i]['$oid'], 'cluster': int(kmeans.labels_[i])}
            print(cluster)
            self.mongo_connector.writeDocument(OUTPUTCOLLECTION,cluster)

