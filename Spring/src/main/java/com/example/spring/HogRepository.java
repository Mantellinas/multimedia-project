package com.example.spring;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HogRepository extends MongoRepository<ClusteringHog, String> {
}
