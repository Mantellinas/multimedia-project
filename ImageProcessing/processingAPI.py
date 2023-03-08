from apscheduler.schedulers.background import BackgroundScheduler
from Clustering_KMeans_PCA import * 
from flask import Flask

ProcessingAPI = Flask(__name__)

x = Clustering_KMeans_PCA()
sched = BackgroundScheduler(daemon=True)
sched.add_job(x.computate,'interval',minutes=1) #hours
sched.start()

if __name__ == "__main__":
    ProcessingAPI.run(
            host='0.0.0.0',
            port=8100)