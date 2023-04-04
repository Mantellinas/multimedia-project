package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FastImageRepository extends MongoRepository<FastImage, String> {

    public Optional<FastImage> findFirstByBaseimageid(ObjectId id);

    public Optional<FastImage> findFirstById(ObjectId id);
}
