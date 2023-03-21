package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class SegImageService {

    @Autowired
    SegImageRepository segRepo;
    public Optional<SegImage> getSegImageByBaseId(ObjectId id){
        return segRepo.findFirstSegImageByBaseImageId(id);
    }
    public Optional<SegImage> getSegImageById(ObjectId id){
        return segRepo.findFirstById(id);
    }
}