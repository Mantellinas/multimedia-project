package com.example.spring;

import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
     private ImageService Imageservice;
    @Autowired
    private FastService fastservice;

    @RequestMapping("/")
    public ModelAndView welcome(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        BaseImage baseImagePrincipal = Imageservice.getLastImage();
        model.addAttribute("image",
                 Base64.getEncoder().encodeToString(baseImagePrincipal.img.getData()));
        model.addAttribute("baseImageId", baseImagePrincipal.id);
        List<String> decodedImages = new ArrayList<>();
        List<BaseImage> latestImages = Imageservice.getLatest12Images();
        for (BaseImage image : latestImages){
            decodedImages.add(Base64.getEncoder().encodeToString(image.img.getData()));
        }
        model.addAttribute("images" , decodedImages);
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping(value = "fast", method = {RequestMethod.GET})
    public ModelAndView fast(Model model, HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        String baseImageId = req.getParameter("baseImageId");
        Optional<FastImage> fastImage = fastservice.getFastImage(baseImageId);
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(baseImageId);

        model.addAttribute("fastImage",
                Base64.getEncoder().encodeToString(fastImage.get().img.getData()));
        model.addAttribute("baseImage",
                Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
        modelAndView.setViewName("fast.html");
        return modelAndView;
    }


}