package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepo;
    public List<BaseImage> getAllImage() {
        return imageRepo.findAll();
    }
    public BaseImage getLastImage(){
        return imageRepo.findFirstByOrderByIdDesc();
    }
    public Optional<BaseImage> getBaseImageById(String id){
        return imageRepo.findById(id);
    }
    public List<BaseImage> getLatest12Images(){
        return imageRepo.findTop12ByOrderByIdDesc();
    }
}