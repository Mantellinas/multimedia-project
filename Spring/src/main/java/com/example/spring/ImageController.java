package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.micrometer.core.annotation.Timed;
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

    @Autowired
    private ChunkService chunkService;
    
    @Timed(value="welcome.get.time",description="time to greeting",percentiles={0.5,0.9})
    @RequestMapping("/")
    public ModelAndView welcome(@RequestParam(required = false) String param,@RequestParam(required = false) String rover, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (param == null && rover == null) {
            BaseImage baseImagePrincipal = Imageservice.getLastImage();
            if(baseImagePrincipal == null ){
                modelAndView.setViewName("error.html");
                return modelAndView;
            }
            model.addAttribute("image",
                    Base64.getEncoder().encodeToString(baseImagePrincipal.img.getData()));
            model.addAttribute("baseImageId", baseImagePrincipal.id);
            model.addAttribute("rover", baseImagePrincipal.rover);
            modelAndView.setViewName("index.html");
        } else {
            Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(param));
            if(baseImagePrincipal.isPresent() == false){
                modelAndView.setViewName("error.html");
                return modelAndView;
            }
            model.addAttribute("rover", rover);
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

    @GetMapping("/kmeans")
    public ModelAndView kmeans(@RequestParam("kMeansId") String idKmeans, Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<KMeansImage> kCluster = kMeansService.getKMeansByCluster(Integer.parseInt(idKmeans));
        Optional <BaseImage> baseImage;
        List<GalleryImage> decodedImages = new ArrayList<>();
        for(KMeansImage kimage : kCluster){
            baseImage = Imageservice.getBaseImageById(kimage.baseimageid);
            decodedImages.add(new GalleryImage(Base64.getEncoder().encodeToString(baseImage.get().img.getData()), baseImage.get().id.toString(), baseImage.get().rover));
        }
        model.addAttribute("images" , decodedImages);
        modelAndView.setViewName("kmeans.html");
        return modelAndView;
    }
    @Timed(value="clustering.get.time",description="time to clustering",percentiles={0.5,0.9})
    @GetMapping("/clustering")
    public ModelAndView clustering(@RequestParam("baseImageIdClustering") String baseImageIdClustering, Model model) {

        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 images
        Optional<BaseImage> baseImagePrincipal = Imageservice.getBaseImageById(new ObjectId(baseImageIdClustering));
        List<KMeansImage> kClusterZero= kMeansService.getKMeansByCluster(0);
        List<KMeansImage> kClusterUno= kMeansService.getKMeansByCluster(1);
        List<KMeansImage> kClusterDue= kMeansService.getKMeansByCluster(2);
        model.addAttribute("cluster0", Base64.getEncoder().encodeToString(Imageservice.getBaseImageById(kClusterZero.get(0).baseimageid).get().img.getData()));
        model.addAttribute("cluster1", Base64.getEncoder().encodeToString(Imageservice.getBaseImageById(kClusterUno.get(0).baseimageid).get().img.getData()));
        model.addAttribute("cluster2", Base64.getEncoder().encodeToString(Imageservice.getBaseImageById(kClusterDue.get(0).baseimageid).get().img.getData()));
        /*if(baseImagePrincipal.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }*/
        Optional<KMeansImage> kMeansImage = kMeansService.getCluster(new ObjectId(baseImageIdClustering));
        /*if(kMeansImage.isPresent() == false){
            modelAndView.setViewName("error.html");
            return modelAndView;
        }*/
        model.addAttribute("imagebase",
                Base64.getEncoder().encodeToString(baseImagePrincipal.get().img.getData()));
       
        ClusteringHog clusteringHog = hogService.getLastHog();

        ArrayList<HogImage> hogImages = clusteringHog.feat_imgs;

        List<GalleryImage> decodedImages = new ArrayList<>();

        List<Chunk> chunks = new ArrayList<>();

        for(HogImage hogimg : hogImages){
            chunks = chunkService.getChunksById(hogimg.featimgid);

            String img="";
            for(Chunk c : chunks){
                img = img + Base64.getEncoder().encodeToString(c.data.getData());
            }

            decodedImages.add(new GalleryImage(img, hogimg.imageid));
        }
       
        model.addAttribute("images" , decodedImages);
        model.addAttribute("dendrogram", clusteringHog.dendrogram);

        if(kMeansImage.isPresent()){
            model.addAttribute("cluster", kMeansImage.get().cluster);
        }else{
            model.addAttribute("cluster", null);
        }
       
        modelAndView.setViewName("clustering.html");
        return modelAndView;
    }


    @GetMapping("/gallery")
    public ModelAndView gallery(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        // get latest image and latest 12 image
        List<GalleryImage> decodedImages = new ArrayList<>();
        List<BaseImage> latestImages = Imageservice.getLatest12Images();
        for (BaseImage image : latestImages){
            decodedImages.add(new GalleryImage(Base64.getEncoder().encodeToString(image.img.getData()), image.id.toString(), image.rover));
        }
        model.addAttribute("images" , decodedImages);
        modelAndView.setViewName("gallery.html");
        return modelAndView;
    }

}