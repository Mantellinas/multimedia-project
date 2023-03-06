package com.example.spring;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageController {

    @Autowired
     private ImageService Imageservice;

    @RequestMapping("/")
    public ModelAndView welcome() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadPhoto.html");
        return modelAndView;
    }

    @GetMapping("/photos")
    public Binary getPhoto() {
        List<Image> images= new ArrayList<>();
        images = Imageservice.getAllImage();
        System.out.println(images);
        // model.addAttribute("title", photo.getTitle());
        // model.addAttribute("image",
        //         Base64.getEncoder().encodeToString(images));
        // ModelAndView modelAndView = new ModelAndView();
        // modelAndView.setViewName("photos.html");
        return images.get(0).img;
    }


}