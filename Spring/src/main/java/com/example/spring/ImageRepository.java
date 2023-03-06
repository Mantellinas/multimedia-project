package com.example.spring;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    public Optional<Image> findById(String id);
    public List<Image> findAll(); 

}

