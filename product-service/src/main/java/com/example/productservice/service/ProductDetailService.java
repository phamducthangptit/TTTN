package com.example.productservice.service;

import com.example.productservice.entity.ProductDetail;
import com.example.productservice.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository repository;

    public ProductDetail addNewProductDetail(ProductDetail productDetail){
        return repository.save(productDetail);
    }
}
