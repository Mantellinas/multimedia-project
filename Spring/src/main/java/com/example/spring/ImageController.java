package com.example.spring;

import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
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
    @Autowired

    private SegImageService segImageService;

    @Autowired

    private SlicImageService slicImageService;
    @RequestMapping("/")
    public ModelAndView welcome(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        BaseImage baseImagePrincipal = Imageservice.getLastImage();
        model.addAttribute("image",
                 Base64.getEncoder().encodeToString(baseImagePrincipal.img.getData()));
        model.addAttribute("baseImageId", baseImagePrincipal.id);
        System.out.println("I'id è il seguente: "+baseImagePrincipal.id);
        List<String> decodedImages = new ArrayList<>();
        List<BaseImage> latestImages = Imageservice.getLatest12Images();
        for (BaseImage image : latestImages){
            decodedImages.add(Base64.getEncoder().encodeToString(image.img.getData()));
        }
        model.addAttribute("images" , decodedImages);
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @GetMapping("/fast")
    public ModelAndView fast(@RequestParam("baseImageId") String baseImageId, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<FastImage> fastImage = fastservice.getFastImage(new ObjectId(baseImageId));
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageId));

        model.addAttribute("fastImage",
                Base64.getEncoder().encodeToString(fastImage.get().img.getData()));
        model.addAttribute("baseImage",
                Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
        modelAndView.setViewName("fast.html");
        return modelAndView;
    }
    @GetMapping("/segmentation")
    public ModelAndView segmentation(@RequestParam("baseImageIdSeg") String baseImageIdSeg, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<SegImage> segImage = segImageService.getSegImageByBaseId(new ObjectId(baseImageIdSeg));
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageIdSeg));

        model.addAttribute("imageOriginale",
                Base64.getEncoder().encodeToString(segImage.get().imgOriginale.getData()));
        model.addAttribute("imgGrey",
                Base64.getEncoder().encodeToString(segImage.get().imgGrey.getData()));
        model.addAttribute("imgThresh",
                Base64.getEncoder().encodeToString(segImage.get().imgThresh.getData()));
        model.addAttribute("imgOpening",
                Base64.getEncoder().encodeToString(segImage.get().imgOpening.getData()));
        model.addAttribute("ImgSureFg",
                Base64.getEncoder().encodeToString(segImage.get().imgSureFg.getData()));
        model.addAttribute("ImgSureBg",
                Base64.getEncoder().encodeToString(segImage.get().imgSureBg.getData()));
        model.addAttribute("ImgMakers",
                Base64.getEncoder().encodeToString(segImage.get().imgMarkers.getData()));
        model.addAttribute("ImgWatershed",
                Base64.getEncoder().encodeToString(segImage.get().imgMarkersWatershed.getData()));
        model.addAttribute("ImgBorder",
                Base64.getEncoder().encodeToString(segImage.get().imgMarkersImgBorder.getData()));
        model.addAttribute("baseImage",
                Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
        modelAndView.setViewName("segmentation.html");
        return modelAndView;
    }

    @GetMapping("/slic")
    public ModelAndView slic(@RequestParam("baseImageIdSlic") String baseImageIdSlic, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<Slic> slicImage = slicImageService.getSlicImageByBaseId(new ObjectId(baseImageIdSlic));
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageIdSlic));

        model.addAttribute("felzenszwalbImg",
                Base64.getEncoder().encodeToString(slicImage.get().felzenszwalbImg.getData()));
        model.addAttribute("SLICImg",
                Base64.getEncoder().encodeToString(slicImage.get().SLICImg.getData()));
        model.addAttribute("quickshiftImg",
                Base64.getEncoder().encodeToString(slicImage.get().quickshiftImg.getData()));
        model.addAttribute("watershedImg",
                Base64.getEncoder().encodeToString(slicImage.get().watershedImg.getData()));
        model.addAttribute("felzenszwalbSegment", slicImage.get().felzenszwalbSegment);
        model.addAttribute("quickshiftSegment", slicImage.get().quickshiftSegment);
        model.addAttribute("slicSegment", slicImage.get().slicSegment);
        model.addAttribute("watershedSegment", slicImage.get().watershedSegment);
        modelAndView.setViewName("slic.html");
        return modelAndView;
    }


}