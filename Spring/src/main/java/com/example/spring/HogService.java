package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class HogService {

    @Autowired
    HogRepository hogRepo;
    public ClusteringHog getLastHog()
    {
        return hogRepo.findAll(Sort.by(Sort.Direction.DESC, "_id")).stream().findFirst().orElse(null);
    }
}
