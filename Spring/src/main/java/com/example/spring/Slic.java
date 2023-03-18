package com.example.spring;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("SLIC")
public class Slic {
    @Id
    public String id;

    public String baseImageId;
    public int felzenszwalbSegment;
    public int slicSegment;
    public int quickshiftSegment;
    public int watershedSegment;
    public Binary felzenszwalbImg;
    public Binary SLICImg;

    public Binary quickshiftImg;

    public Binary watershedImg;

    Slic(){

    }

}
