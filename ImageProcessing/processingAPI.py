from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask
from Clustering_KMeans_PCA import * 
from Corner_Detector_FAST import *
from Segmentation import *
import os

INTERVAL = os.getenv('INTERVAL')

ProcessingAPI = Flask(__name__)

clustering_K_means = Clustering_KMeans_PCA()
cd_FAST = Corner_Detector_FAST()
segmentation = Segmentation()

sched = BackgroundScheduler(daemon=True)
sched.add_job(clustering_K_means.computate,'interval',minutes=1) #hours
sched.add_job(cd_FAST.computate,'interval',minutes=1)
sched.add_job(segmentation.computate,'interval',minutes=1)
sched.start()

if __name__ == "__main__":
    ProcessingAPI.run(
            host='0.0.0.0',
            port=8100)