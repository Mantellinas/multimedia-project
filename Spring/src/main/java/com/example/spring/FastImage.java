package com.example.spring;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("FAST")

public class FastImage {
    @Id
    public ObjectId id;
    public Binary img;
    public ObjectId baseimageid;

    FastImage(){}
}
