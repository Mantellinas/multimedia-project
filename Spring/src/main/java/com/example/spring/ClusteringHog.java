package com.example.spring;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document("clustering_HOG")
public class ClusteringHog
{
    @Id
    public ObjectId id;
    public ArrayList<HogImage> feat_imgs;

    public String dendrogram;
}
