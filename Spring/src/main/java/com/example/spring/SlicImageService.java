package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SlicImageService {
    @Autowired
    SlicImageRepository slicRepo;

    public Optional<Slic> getSlicImageByBaseId(ObjectId id){
        return slicRepo.findFirstByBaseimageid(id);
    }
}
