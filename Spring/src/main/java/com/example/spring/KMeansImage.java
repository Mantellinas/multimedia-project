package com.example.spring;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("clustering_KMeans")
public class KMeansImage {
    @Id
    public ObjectId id;
    public ObjectId baseimageid;
    public int cluster;
}
