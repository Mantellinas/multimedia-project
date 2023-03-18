package com.example.spring;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SegImageRepository extends MongoRepository<SegImage, String> {
    public Optional<SegImage> findFirstSegImageByBaseImageId(String id);

    public Optional<SegImage> findFirstById(String id);
}
