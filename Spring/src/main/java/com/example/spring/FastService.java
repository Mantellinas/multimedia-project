package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class FastService {

    @Autowired
    FastImageRepository fastRepo;
    public Optional<FastImage> getFastImage(ObjectId id){
        return fastRepo.findFirstByBaseimageid(id);
    }
    public Optional<FastImage> getFastImageById(ObjectId id){
        return fastRepo.findFirstById(id);
    }
}
