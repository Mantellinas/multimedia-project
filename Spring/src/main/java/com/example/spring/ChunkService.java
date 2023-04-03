package com.example.spring;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChunkService {

    @Autowired
    ChunkRepository chunkReposityory;

    public List<Chunk> getChunksById(ObjectId id){
        return chunkReposityory.findByfilesId(id);
    }
    

}
