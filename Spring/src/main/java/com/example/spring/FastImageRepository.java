package com.example.spring;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FastImageRepository extends MongoRepository<FastImage, String> {
    public FastImage findFirstByBaseImageId(String id);
}
