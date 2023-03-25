package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KMeansRepository extends MongoRepository<KMeansImage, String> {
    public Optional<KMeansImage> findFirstByBaseimageid(ObjectId id);
}
