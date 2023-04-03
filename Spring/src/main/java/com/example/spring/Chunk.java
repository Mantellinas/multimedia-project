package com.example.spring;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document("fs.chunks")
public class Chunk
{
    @Id
    public ObjectId id;

    @Field("files_id")
    public ObjectId filesId;

    public Integer n;

    public Binary data;
}
