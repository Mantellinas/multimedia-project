from pymongo import MongoClient
import os

MONGO_USR = os.getenv('MONGO_USER')
MONGO_PSW = os.getenv('MONGO_PASSWORD')
MONGO_DB = os.getenv('MONGO_DATABASE')
class MongoConnector:
    def __init__(self):
        self.mongo_ip   = "mongo_db" 
        self.mongo_port =  27017
        self.mongo_user = MONGO_USR
        self.mongo_pass = MONGO_PSW
        self.mongo_db   = MONGO_DB
        self.connection = None
        
    def connection_opening(self):
        try:
            self.connection = MongoClient(
                host        = self.mongo_ip,
                port        = self.mongo_port,
                authSource  = self.mongo_db,
                username    = self.mongo_user,
                password    = self.mongo_pass,
                authMechanism='SCRAM-SHA-256'
            )
        except Exception as e:
            print(e)
            self.connection = None

    def connection_closing(self):
        print("DATABASE CONNECTION CLOSING")
        if self.connection is None: return
        self.connection.close()

    def database_collection_selection(self, collection_name):
        self.connection_opening()
        try:
            database   = self.connection[self.mongo_db]
            collection = database[collection_name]
            return database, collection
        except Exception as e:
            return None, None
    
    def writeDocument(self, collection_name, document):
        database, collection = self.database_collection_selection(collection_name)
        collection.insert_one(document)
        
