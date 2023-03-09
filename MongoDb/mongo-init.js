db = db.getSiblingDB('image_database');


db.createUser(
    {
        user: "myuser",
        pwd: "mypassword",
        roles: [
            {
                role: "readWrite",
                db: "image_database"
            }
        ]
    }
);

db.createCollection('base_image');
db.createCollection('clustering_KMeans')
db.createCollection('clustering_HOG')
db.createCollection('FAST')
db.createCollection('SLIC')
db.createCollection('Segmentation')




