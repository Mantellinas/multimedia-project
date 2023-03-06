from flask import Flask
from MongoConnector import *
import os

# KMEANSCOLLECTION = os.getenv('CLUSTERING_KMEANS')
# HOGSCOLLECTION = os.getenv('CLUSTERING_HOG')
# FASTCOLLECTION = os.getenv('FAST')
# SLICCOLLECTION = os.getenv('SLIC')
# BASECOLLECTION = os.getenv('BASE_IMAGE')





ProcessingAPI = Flask(__name__)

mongo_connector = MongoConnector()

@ProcessingAPI.route("/getAllImages")
def getAllImages():
    images = mongo_connector.getAllDocument("base_image")
    return images

# @ProcessingAPI.route("/runKmeans")
# def runKmeans():
#     images = mongo_connector.ge(KMEANSCOLLECTION)
#     return images
    
   
# @ProcessingAPI.route("/runHog")
# def runHog():
#     images = mongo_connector.getAllDocument(HOGSCOLLECTION)
#     return images
       

# @ProcessingAPI.route("/runFAST")
# def runFAST():
#     images = mongo_connector.getAllDocument(FASTCOLLECTION)
#     return images


# @ProcessingAPI.route("/runSLIC")
# def runSLIC():
#     images = mongo_connector.getAllDocument(SLICCOLLECTION)
#     return images
    
if __name__ == "__main__":
    ProcessingAPI.run(
            host='0.0.0.0',
            port=9000)
