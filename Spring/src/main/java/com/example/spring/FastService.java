package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class FastService {

    @Autowired
    FastImageRepository fastRepo;
    public Optional<FastImage> getFastImage(String id){
        return fastRepo.findAllByBaseimageid(id);
    }
    public Optional<FastImage> getFastImageById(String id){
        return fastRepo.findFirstById(id);
    }
}
