package com.example.productservice.repository;

import com.example.productservice.entity.ProductDetail;
import com.example.productservice.id.ProductDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, ProductDetailId> {
}
