package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KMeansService {
    @Autowired
    KMeansRepository kMeansRepo;

    public Optional<KMeansImage> getCluster(ObjectId id) {
        return kMeansRepo.findFirstByBaseimageid(id);
    }
    public List<KMeansImage> getAll(){
        return kMeansRepo.findAll();
    }
    public List<KMeansImage> getKMeansByCluster(int idCluster){
        return kMeansRepo.findAllByCluster(idCluster);
    }
}
