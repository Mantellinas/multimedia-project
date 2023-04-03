package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("fs.chunks")
public class Chunk
{
    @Id
    public ObjectId id;

    public ObjectId filesId;

    public Integer n;

    public String data;
}
