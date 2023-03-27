from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask
from Clustering_KMeans_PCA import * 
from Corner_Detector_FAST import *
from Segmentation import *
from Hog import *
from Slic import *
import os

INTERVAL = os.getenv('INTERVAL')

ProcessingAPI = Flask(__name__)

clustering_K_means = Clustering_KMeans_PCA()
cd_FAST = Corner_Detector_FAST()
slic = Slic()
segmentation = Segmentation()
hog = Hog()


#sched = BackgroundScheduler(daemon=True)
#sched.add_job(clustering_K_means.computate,'interval',minutes=1) #hours
#sched.add_job(cd_FAST.computate,'interval',minutes=1)
#sched.add_job(slic.computate,'interval',minutes=1)
#sched.add_job(segmentation.computate,'interval',minutes=1)
#sched.add_job(hog.computate,'interval',minutes=1)

#sched.start()

@ProcessingAPI.route('/processing', methods=['GET'])
def processing():
    clustering_K_means.computate()
    cd_FAST.computate()
    segmentation.computate()
    slic.computate()
    # hog.computate()

if __name__ == "__main__":
    ProcessingAPI.run(
            host='0.0.0.0',
            port=8100)