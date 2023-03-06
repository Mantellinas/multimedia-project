package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepo;

    public List<Image> getAllImage() {
        return imageRepo.findAll();
    }
}