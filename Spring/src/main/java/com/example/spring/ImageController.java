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

import io.micrometer.core.annotation.Timed;

import javax.management.Query;
import java.util.*;

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

    @Autowired
    private KMeansService kMeansService;

    @Autowired
    private HogService hogService;
    
    @Timed(value="welcome.get.time",description="time to greeting",percentiles={0.5,0.9})
    @RequestMapping("/")
    public ModelAndView welcome(@RequestParam(required = false) String param, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (param == null) {
            BaseImage baseImagePrincipal = Imageservice.getLastImage();
            if(baseImagePrincipal == null ){
                modelAndView.setViewName("error.html");
                return modelAndView;
            }
            model.addAttribute("image",
                    Base64.getEncoder().encodeToString(baseImagePrincipal.img.getData()));
            model.addAttribute("baseImageId", baseImagePrincipal.id);
            modelAndView.setViewName("index.html");
        } else {
            Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(param));
            if(baseImagePrincipal.isPresent() == false){
                modelAndView.setViewName("error.html");
                return modelAndView;
            }
            model.addAttribute("image",
                    Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
            model.addAttribute("baseImageId", baseImagePrincipal.get().id);
            modelAndView.setViewName("index.html");
        }
        return modelAndView;
    }


    @Timed(value="fast.get.time",description="time to fast",percentiles={0.5,0.9})
    @GetMapping("/fast")
    public ModelAndView fast(@RequestParam("baseImageId") String baseImageId, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<FastImage> fastImage = fastservice.getFastImage(new ObjectId(baseImageId));
        if(fastImage.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageId));
        if(baseImagePrincipal.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }
        model.addAttribute("fastImage",
                Base64.getEncoder().encodeToString(fastImage.get().img.getData()));
        model.addAttribute("baseImage",
                Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
        modelAndView.setViewName("fast.html");
        return modelAndView;
    }
    @Timed(value="segmentation.get.time",description="time to segmentation",percentiles={0.5,0.9})
    @GetMapping("/segmentation")
    public ModelAndView segmentation(@RequestParam("baseImageIdSeg") String baseImageIdSeg, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<SegImage> segImage = segImageService.getSegImageByBaseId(new ObjectId(baseImageIdSeg));
        if(!segImage.isPresent()){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageIdSeg));
        if(baseImagePrincipal.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }
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

    @Timed(value="slic.get.time",description="time to slic",percentiles={0.5,0.9})
    @GetMapping("/slic")
    public ModelAndView slic(@RequestParam("baseImageIdSlic") String baseImageIdSlic, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<Slic> slicImage = slicImageService.getSlicImageByBaseId(new ObjectId(baseImageIdSlic));
        if(!slicImage.isPresent()){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageIdSlic));
        if(baseImagePrincipal.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }
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

    @Timed(value="clustering.get.time",description="time to clustering",percentiles={0.5,0.9})
    @GetMapping("/clustering")
    public ModelAndView clustering(@RequestParam("baseImageIdClustering") String baseImageIdClustering, Model model) {
        System.out.print("Prova ");
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageIdClustering));
        System.out.print("Prova 1");
        /*if(baseImagePrincipal.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }*/
        Optional<KMeansImage> kMeansImage = kMeansService.getCluster(new ObjectId(baseImageIdClustering));
        System.out.print("Prova 2");
        /*if(kMeansImage.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }*/
        model.addAttribute("imagebase",
                Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
        ClusteringHog clusteringHog = hogService.getLastHog();
        System.out.print("Prova 3");
        /*if(clusteringHog == null){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }*/
        ArrayList<HogImage> hogImages = clusteringHog.feat_imgs;
        List<GalleryImage> decodedImages = new ArrayList<>();
        /*if(hogImages == null){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }*/
        System.out.print("4");
        for(HogImage hogImage: hogImages){
            if(hogImage.baseimageid.toString().equals(baseImageIdClustering)){
                //ho trovato l'immagine con le stelline e posso stamparla
                model.addAttribute("imageHog",
                        Base64.getEncoder().encodeToString(hogImage.featimg.getData()));
                break;
            }

        }
        for(HogImage hogImage: hogImages){
            decodedImages.add(new GalleryImage(Base64.getEncoder().encodeToString(hogImage.featimg.getData()), hogImage.imageid));
        }
        model.addAttribute("images" , decodedImages);
        model.addAttribute("dendrogram", clusteringHog.dendrogram);
        model.addAttribute("cluster", kMeansImage.get().cluster);
        modelAndView.setViewName("clustering.html");
        return modelAndView;
    }

    @GetMapping("/gallery")
    public ModelAndView gallery(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 image
        List<GalleryImage> decodedImages = new ArrayList<>();
        List<BaseImage> latestImages = Imageservice.getLatest12Images();
        List<String> latestIds = new ArrayList<>();
        for (BaseImage image : latestImages){
            decodedImages.add(new GalleryImage(Base64.getEncoder().encodeToString(image.img.getData()), image.id.toString()));
        }
        model.addAttribute("images" , decodedImages);
        modelAndView.setViewName("gallery.html");
        return modelAndView;
    }

}