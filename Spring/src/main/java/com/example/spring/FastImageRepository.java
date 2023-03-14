package com.example.spring;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FastImageRepository extends MongoRepository<FastImage, String> {
    public Optional<FastImage> findByBase_image_id(String id);

    //@Query("{base_image_id:'?0'}")
    //Optional<FastImage>  findByBaseImageId(String base_image_id);

    public Optional<FastImage> findById(String id);
}
