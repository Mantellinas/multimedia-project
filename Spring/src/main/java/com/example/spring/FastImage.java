package com.example.spring;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("FAST")

public class FastImage {
    @Id
    public String id;
    public Binary img;
    public String baseImageId;

    FastImage(){}
}
