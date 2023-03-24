package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SegImageRepository extends MongoRepository<SegImage, String> {
    public Optional<SegImage> findFirstByBaseimageid(ObjectId id);
//findFirstSegImageByBaseImageId
    public Optional<SegImage> findFirstById(ObjectId id);
}
