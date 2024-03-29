package com.example.spring;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ImageRepository extends MongoRepository<BaseImage, String> {
    public Optional<BaseImage> findById(ObjectId id);

    public List<BaseImage> findAll();

    public BaseImage findFirstByOrderByIdDesc();

    public List<BaseImage> findTop12ByOrderByIdDesc();



}

