package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SlicImageRepository extends MongoRepository<Slic, String> {

    public Optional<Slic> findSlicByBaseImageId(ObjectId id);

}
