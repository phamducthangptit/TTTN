package com.example.importgoodsservice.service;


import com.example.importgoodsservice.dto.ProductDTO;
import com.example.importgoodsservice.entity.Product;
import com.example.importgoodsservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProductByManufacturer(int manufacturerId){
        List<Product> productList = productRepository.getListProductByManufacturerId(manufacturerId);
        return productList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getId());
        productDTO.setProductName(product.getName());
        return productDTO;
    }

    public void updateStockProduct(int id, int stock){
        productRepository.updateStock(id, stock);
    }
}
