package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SlicImageService {
    @Autowired
    SlicImageRepository slicRepo;

    public Optional<Slic> getSlicImageByBaseId(String id){
        return slicRepo.findSlicByBaseImageId(id);
    }
}
