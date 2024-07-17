package com.example.productservice.service;


import com.example.productservice.dto.ProductDetailRequestDTO;
import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.entity.*;
import com.example.productservice.id.ProductDetailId;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ImageService imageService;

    public List<ProductResponseDTO> getAllProductByCategoryId(int categoryId) {
        return repository.getAllProductByCategoryId(categoryId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setManufacturerName(product.getManufacturer().getName());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setStock(product.getStock());
        productResponseDTO.setCreateAt(product.getCreateAt());
        productResponseDTO.setUpdateAt(product.getUpdateAt());
        productResponseDTO.setImage(product.getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null));
        return productResponseDTO;
    }

    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO){
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(BigDecimal.valueOf(Integer.parseInt(productRequestDTO.getPrice())));
        product.setStock(Integer.parseInt(productRequestDTO.getStock()));
        Category category = categoryService.getCategoryById(productRequestDTO.getCategoryId());
        product.setCategory(category);
        Manufacturer manufacturer = manufacturerService.getManufacturerById(productRequestDTO.getManufacturerId());
        product.setManufacturer(manufacturer);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());
        product.setDescription(productRequestDTO.getDescription());
        Product savedProduct = repository.save(product);

        // insert product detail
        for (ProductDetailRequestDTO proDetail : productRequestDTO.getProductDetails()) {
            ProductDetail productDetail = new ProductDetail();

            // Thiết lập ProductDetailId
            ProductDetailId productDetailId = new ProductDetailId();
            productDetailId.setDetailId(proDetail.getDetailId());
            productDetailId.setProductId(savedProduct.getProductId()); // Lấy productId từ product đã lưu

            productDetail.setProductDetailId(productDetailId); // Thiết lập ID cho ProductDetail
            productDetail.setValue(proDetail.getValue());

            productDetailService.addNewProductDetail(productDetail); // Lưu ProductDetail
        }
        int check = 0;
        //insert image
        for(String s : productRequestDTO.getImages()){
            Image image = new Image();
            image.setProduct(savedProduct);
            image.setUrl(s);
            if(++check == 1) image.setAvatar(true);
            imageService.addNewImage(image);
        }
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(savedProduct.getProductId());
        return productResponseDTO;
    }


}
