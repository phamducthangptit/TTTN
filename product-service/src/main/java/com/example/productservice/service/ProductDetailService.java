package com.example.productservice.service;

import com.example.productservice.entity.ProductDetail;
import com.example.productservice.repository.ProductDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository repository;

    @Transactional
    public ProductDetail addNewProductDetail(ProductDetail productDetail){
        return repository.save(productDetail);
    }

    public int countProductDetailByProductId(int productId){
        return repository.countProductDetailByProductId(productId);
    }


    @Transactional
    public void deleteProductDetailByProductId(int productId){
        repository.deleteProductDetailByProductId(productId);
    }

    public void updateProductDetail(int productId, int detailId, String value){
        repository.updateProductDetailByProductIdAndDetailId(value, productId, detailId);
    }
}
