package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class SegImageService {

    @Autowired
    SegImageRepository segRepo;
    public Optional<SegImage> getSegImageByBaseId(String id){
        return segRepo.findFirstSegImageByBaseImageId(id);
    }
    public Optional<SegImage> getSegImageById(String id){
        return segRepo.findFirstById(id);
    }
}