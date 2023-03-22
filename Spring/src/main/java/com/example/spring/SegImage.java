package com.example.spring;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Segmentation")

public class SegImage {
    @Id
    public ObjectId id;
    public ObjectId baseimageid;
    public Binary imgOriginale;
    public Binary imgGrey;
    public Binary imgThresh;
    public Binary imgOpening;
    public Binary imgSureBg;
    public Binary imgSureFg;
    public Binary imgMarkers;
    public Binary imgMarkersWatershed;
    public Binary imgMarkersImgBorder;

    SegImage(){}
}
