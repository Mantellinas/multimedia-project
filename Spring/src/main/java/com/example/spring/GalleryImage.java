package com.example.spring;

public class GalleryImage {
    public String img;
    public String id;
    public String rover;

    public GalleryImage(String img, String id) {
        this.img = img;
        this.id = id;
    }

    public GalleryImage(String img, String id, String rover){
        this.img = img;
        this.id = id;
        this.rover = rover;
    }
}
