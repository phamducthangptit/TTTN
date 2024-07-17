package com.example.productservice.service;

import com.example.productservice.entity.Image;
import com.example.productservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ImageRepository repository;

    public Image addNewImage(Image image){
        return repository.save(image);
    }
}
