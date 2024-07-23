package com.example.productservice.service;


import com.example.productservice.dto.*;
import com.example.productservice.entity.*;
import com.example.productservice.id.ProductDetailId;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private OrderDetailService orderDetailService;

    public List<ProductResponseDTO> getAllProductByCategoryId(int categoryId) {
        return repository.getAllProductByCategoryId(categoryId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAllProduct(){
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDetailResponseDTO getProductById(int productId) {
        Optional<Product> object = repository.findByproductId(productId);
        if (object.isPresent()) {
            Product product = object.get();
            return convertProductToDTO(product);
        }
        return null;
    }

    public ProductDetailResponseGuestDTO getProductGuest(int productId){
        Optional<Product> object = repository.findByproductId(productId);
        if (object.isPresent()) {
            Product product = object.get();
            return convertProductToGuestDTO(product);
        }
        return null;
    }

    private ProductDetailResponseGuestDTO convertProductToGuestDTO(Product product) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        ProductDetailResponseGuestDTO productDetailResponseGuestDTO = new ProductDetailResponseGuestDTO();
        productDetailResponseGuestDTO.setProductId(product.getProductId());
        productDetailResponseGuestDTO.setName(product.getName());
        productDetailResponseGuestDTO.setDescription(product.getDescription());
        productDetailResponseGuestDTO.setManufacturerName(product.getManufacturer().getName());
        productDetailResponseGuestDTO.setCategoryName(product.getCategory().getName());
        productDetailResponseGuestDTO.setPrice(decimalFormat.format(product.getPrice()));
        productDetailResponseGuestDTO.setStock(product.getStock());
        productDetailResponseGuestDTO.setListDetail(product.getProductDetails().stream().map(this::convertDetailToDTO).collect(Collectors.toList()));
        productDetailResponseGuestDTO.setImage(product.getImages().stream().map(this::convertImageToDTO).collect(Collectors.toList()));
        productDetailResponseGuestDTO.setListReview(product.getReviews().stream().map(this::convertReviewToGuestDTO).collect(Collectors.toList()));
        productDetailResponseGuestDTO.setCountSales(countSales(product.getOrderDetails(), product.getProductId()));
        return productDetailResponseGuestDTO;
    }

    private int countSales(List<OrderDetail> listOrderDetail, int productId){
        int count = 0;
        for(OrderDetail orderDetail : listOrderDetail){
            if(orderDetail.getProduct().getProductId() == productId) count += orderDetail.getQuantity();
        }
        return count;
    }

    private ReviewResponseGuestDTO convertReviewToGuestDTO(Review review) {
        ReviewResponseGuestDTO reviewResponseGuestDTO = new ReviewResponseGuestDTO();
        reviewResponseGuestDTO.setReviewId(review.getReviewId());
        reviewResponseGuestDTO.setUsername(review.getUser().getFirstName() + " " + review.getUser().getLastName());
        reviewResponseGuestDTO.setComment(review.getComment());
        reviewResponseGuestDTO.setRating(review.getRating());
        reviewResponseGuestDTO.setCreateAt(review.getCreatedAt());
        return reviewResponseGuestDTO;
    }

    private ProductDetailResponseDTO convertProductToDTO(Product product) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        ProductDetailResponseDTO productResponseDTO = new ProductDetailResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setManufacturerId(product.getManufacturer().getManufacturerId());
        productResponseDTO.setCategoryId(product.getCategory().getCategoryId());
        productResponseDTO.setPrice(decimalFormat.format(product.getPrice()));
        productResponseDTO.setStock(product.getStock());
        productResponseDTO.setCreateAt(product.getCreateAt());
        productResponseDTO.setUpdateAt(product.getUpdateAt());
        productResponseDTO.setImage(product.getImages().stream().map(this::convertImageToDTO).collect(Collectors.toList()));
        productResponseDTO.setListDetail(product.getProductDetails().stream().map(this::convertDetailToDTO).collect(Collectors.toList()));
        return productResponseDTO;
    }

    private DetailProductResponseDTO convertDetailToDTO(ProductDetail productDetail) {
        DetailProductResponseDTO detailProductResponseDTO = new DetailProductResponseDTO();
        detailProductResponseDTO.setDetailId(productDetail.getDetail().getDetailId());
        detailProductResponseDTO.setName(productDetail.getDetail().getName());
        detailProductResponseDTO.setValue(productDetail.getValue());
        return detailProductResponseDTO;
    }

    private String convertImageToDTO(Image image) {
        return image.getUrl();
    }

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setManufacturerName(product.getManufacturer().getName());
        productResponseDTO.setCategoryName(product.getCategory().getName());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setStock(product.getStock());
        String image = product.getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null);
        List<String> avatarProduct = new ArrayList<>();
        avatarProduct.add(image);
        productResponseDTO.setImage(avatarProduct);
        return productResponseDTO;
    }

    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {
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
        for (String s : productRequestDTO.getImages()) {
            Image image = new Image();
            image.setProduct(savedProduct);
            image.setUrl(s);
            if (++check == 1) image.setAvatar(true);
            imageService.addNewImage(image);
        }
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(savedProduct.getProductId());
        return productResponseDTO;
    }

    @Transactional
    public void updateProduct(ProductRequestUpdateDTO productRequestUpdateDTO) {
        // đây là sản phẩm chưa cập nhật trong db
        ProductDetailResponseDTO productDetailResponseDTO = convertToProductDetailDTO(repository.findByproductId(productRequestUpdateDTO.getProductId()).get());

        System.out.println(productDetailResponseDTO.getListDetail().toString());
        // update các thuôộc tính khác
        repository.updateProduct(productRequestUpdateDTO.getName(), BigDecimal.valueOf(Integer.parseInt(productRequestUpdateDTO.getPrice())),
                Integer.parseInt(productRequestUpdateDTO.getStock()), productRequestUpdateDTO.getDescription(), productRequestUpdateDTO.getCategoryId(),
                productRequestUpdateDTO.getManufacturerId(), productRequestUpdateDTO.getProductId());
        // nếu khác category thì cập nhật lại casc thuộc tính, xóa các thuộc tính cũ, thêm thuộc tính mới
        if (productRequestUpdateDTO.getCategoryId() != productDetailResponseDTO.getCategoryId()) {
            System.out.println(productDetailService.countProductDetailByProductId(productRequestUpdateDTO.getProductId()));
            // xóa các thuộc tính cũ
            productDetailService.deleteProductDetailByProductId(productDetailResponseDTO.getProductId());
            System.out.println(productDetailService.countProductDetailByProductId(productRequestUpdateDTO.getProductId()));
            repository.flush();
            if (productDetailService.countProductDetailByProductId(productRequestUpdateDTO.getProductId()) == 0) {
                System.out.println(productRequestUpdateDTO.getProductDetails().toString());
                // thêm các thuộc tính mới
                for (ProductDetailRequestDTO productDetailRequestDTO : productRequestUpdateDTO.getProductDetails()) {
                    if (!productDetailRequestDTO.getValue().isEmpty()) { // nếu sản phẩm đó có detail thì lưu
                        ProductDetail productDetail = new ProductDetail();
                        ProductDetailId productDetailId = new ProductDetailId();

                        productDetailId.setProductId(productRequestUpdateDTO.getProductId());
                        System.out.println(productRequestUpdateDTO.getProductId());
                        productDetailId.setDetailId(productDetailRequestDTO.getDetailId());
                        System.out.println(productDetailRequestDTO.getDetailId());


                        productDetail.setProductDetailId(productDetailId);
                        productDetail.setValue(productDetailRequestDTO.getValue());

                        productDetailService.addNewProductDetail(productDetail);
                    }
                }
            }
//            productDetailService.
        } else { // nếu cùng category thì update lại các thuộc tính
            for (ProductDetailRequestDTO productDetailRequestDTO : productRequestUpdateDTO.getProductDetails()) {
                productDetailService.updateProductDetail(productRequestUpdateDTO.getProductId(),
                        productDetailRequestDTO.getDetailId(),
                        productDetailRequestDTO.getValue());
            }
        }

        // update image
        //
        List<String> imageBE = productDetailResponseDTO.getImage();
        List<String> imageFE = productRequestUpdateDTO.getImages();
        for(String s : imageFE){ // có trên fe mà không có dưới be thì thêm
            if(!imageBE.contains(s)){
                Image image = new Image();
                Product product = repository.findByproductId(productDetailResponseDTO.getProductId()).get();
                image.setProduct(product);
                image.setUrl(s);
                if(imageService.countImageProduct(productDetailResponseDTO.getProductId()) == 0){
                    image.setAvatar(true);
                } else image.setAvatar(false);
                imageService.addNewImage(image);
            }

        }
        for(String s : imageBE){ // có dưới be mà không có trên fe thì xóa
            if(!imageFE.contains(s)){
                imageService.deleteImageByProductIdAndUrl(productDetailResponseDTO.getProductId(), s);
            }
        }

    }

    private ProductDetailResponseDTO convertToProductDetailDTO(Product product) {
        ProductDetailResponseDTO productDetailResponseDTO = new ProductDetailResponseDTO();
        productDetailResponseDTO.setProductId(product.getProductId());
        productDetailResponseDTO.setName(product.getName());
        productDetailResponseDTO.setDescription(product.getDescription());
        productDetailResponseDTO.setManufacturerId(product.getManufacturer().getManufacturerId());
        productDetailResponseDTO.setCategoryId(product.getCategory().getCategoryId());
        productDetailResponseDTO.setPrice(product.getPrice().toPlainString());
        productDetailResponseDTO.setStock(product.getStock());
        productDetailResponseDTO.setImage(product.getImages().stream().map(this::convertImageToDTO).collect(Collectors.toList()));
        productDetailResponseDTO.setListDetail(product.getProductDetails().stream().map(this::convertDetailToDTO).collect(Collectors.toList()));
        return productDetailResponseDTO;
    }

    @Transactional
    public int deleteProduct(int productId) {
        Optional<Product> product = repository.findByproductId(productId);
        if (product.isPresent()) { // nếu tồn tại product mới đi xóa
            if (orderDetailService.getQuantityProductInOrder(productId) == 0) { // nếu chưa tồn tại trong order nào thì đi xóa
                // xóa các khóa ngoại trước
                // xóa product detail
                productDetailService.deleteProductDetailByProductId(productId);
                // xóa image
                imageService.deleteImageByProductId(productId);
                // xóa product
                repository.deleteProductById(productId);
                return 1; // xóa thành công
            }
            return -2; // xóa thất bại do đã tồn tại trong order
        }
        return -1; // không tìm thấy product
    }

    public int getStockProduct(int productId){
        return repository.getStockProduct(productId);
    }
}
