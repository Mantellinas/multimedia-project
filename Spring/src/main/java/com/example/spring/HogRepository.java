package com.example.spring;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HogRepository extends MongoRepository<ClusteringHog, String> {
    //Optional<ClusteringHog> findAll(Sort.by(Sort.Direction.DESC, "_id")).stream().findFirst().orElse(null);
}
