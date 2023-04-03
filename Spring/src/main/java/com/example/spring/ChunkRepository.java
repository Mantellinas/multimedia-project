package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkRepository extends MongoRepository<Chunk, String> {
    public List<Chunk>  findAllByFilesId(ObjectId filesId);

}
