package com.example.spring;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("base_image")
public class Image {

    @Id
    public String id;
    public Binary img;
    public String camera_name;
    public String rover;

    Image(){}

}
