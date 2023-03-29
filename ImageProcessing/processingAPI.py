from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask, make_response
from Clustering_KMeans_PCA import * 
from Corner_Detector_FAST import *
from Segmentation import *
from Hog import *
from Slic import *
import os
import threading

INTERVAL = os.getenv('INTERVAL')

ProcessingAPI = Flask(__name__)

clustering_K_means = Clustering_KMeans_PCA()
cd_FAST = Corner_Detector_FAST()
slic = Slic()
segmentation = Segmentation()
hog = Hog()

@ProcessingAPI.route('/processing', methods=['GET'])
def processing():

    fast_thread = threading.Thread(target=cd_FAST.computate(), name='fast_thread')
    Kmeans_thread = threading.Thread(target=clustering_K_means.computate(), name='Kmeans_thread')
    segmentation_thread = threading.Thread(target=segmentation.computate(), name='segmentation_thread')
    slic_thread = threading.Thread(target=slic.computate(), name='slic_thread')
    hog_thread = threading.Thread(target=hog.computate(), name='hog_thread')
   
    fast_thread.start()
    Kmeans_thread.start()
    segmentation_thread.start()
    slic_thread.start()
    hog_thread.start()

    response = make_response()
    response.status_code = 200
    return response


if __name__ == "__main__":
    ProcessingAPI.run(
            host='0.0.0.0',
            port=8100)