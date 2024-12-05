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

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private PriceDetailService priceDetailService;

    public List<ProductResponseDTO> getAllProductByCategoryId(int categoryId) {
        return repository.getAllProductByCategoryId(categoryId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAllProduct() {
        return repository.getAllProductByIsPresent().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAllProductByQuery(String query) {
        return repository.getProductByQuery(query).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDetailResponseDTO getProductById(int productId) {
        Optional<Product> object = repository.findByproductId(productId);
        if (object.isPresent()) {
            Product product = object.get();
            return convertProductToDTO(product);
        }
        return null;
    }

    public ProductDetailResponseGuestDTO getProductGuest(int productId) {
        Optional<Product> object = repository.findByproductId(productId);
        if (object.isPresent()) {
            Product product = object.get();
            return convertProductToGuestDTO(product);
        }
        return null;
    }

    private ProductDetailResponseGuestDTO convertProductToGuestDTO(Product product) {
        LocalDateTime currentDate = LocalDateTime.now();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        ProductDetailResponseGuestDTO productDetailResponseGuestDTO = new ProductDetailResponseGuestDTO();
        productDetailResponseGuestDTO.setProductId(product.getProductId());
        productDetailResponseGuestDTO.setName(product.getName());
        productDetailResponseGuestDTO.setDescription(product.getDescription());
        productDetailResponseGuestDTO.setManufacturerName(product.getManufacturer().getName());
        productDetailResponseGuestDTO.setCategoryName(product.getCategory().getName());
        productDetailResponseGuestDTO.setStock(product.getStock());
        productDetailResponseGuestDTO.setListDetail(product.getProductDetails().stream().map(this::convertDetailToDTO).collect(Collectors.toList()));
        productDetailResponseGuestDTO.setImage(product.getImages().stream().map(this::convertImageToDTO).collect(Collectors.toList()));
        productDetailResponseGuestDTO.setListReview(reviewService.getListReviewByProductId(product.getProductId()));
        productDetailResponseGuestDTO.setCountSales(countSales(product.getOrderDetails(), product.getProductId()));
        // Lọc danh sách PriceDetail để lấy giá hiện tại
        Optional<BigDecimal> priceNow = product.getPriceDetails().stream()
                .filter(pd -> pd.getStartAt().isBefore(currentDate) || pd.getStartAt().isEqual(currentDate))
                .filter(pd -> pd.getEndAt() == null || pd.getEndAt().isAfter(currentDate) || pd.getEndAt().isEqual(currentDate))
                .map(PriceDetail::getPrice1) // Lấy giá trị price1 từ PriceDetail
                .findFirst(); // Lấy giá đầu tiên thỏa mãn điều kiện (nếu có)
        priceNow.ifPresent(productDetailResponseGuestDTO::setPrice);
        return productDetailResponseGuestDTO;
    }

    private int countSales(List<OrderDetail> listOrderDetail, int productId) {
        int count = 0;
        for (OrderDetail orderDetail : listOrderDetail) {
            if (orderDetail.getProduct().getProductId() == productId) count += orderDetail.getQuantity();
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
        LocalDateTime currentDate = LocalDateTime.now();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        ProductDetailResponseDTO productResponseDTO = new ProductDetailResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setManufacturerId(product.getManufacturer().getManufacturerId());
        productResponseDTO.setCategoryId(product.getCategory().getCategoryId());
        productResponseDTO.setStock(product.getStock());
        productResponseDTO.setCreateAt(product.getCreateAt());
        productResponseDTO.setUpdateAt(product.getUpdateAt());
        productResponseDTO.setImage(product.getImages().stream().map(this::convertImageToDTO).collect(Collectors.toList()));
        productResponseDTO.setListDetail(product.getProductDetails().stream().map(this::convertDetailToDTO).collect(Collectors.toList()));
        productResponseDTO.setLength(product.getLength());
        productResponseDTO.setWeight(product.getWeight());
        productResponseDTO.setHeight(product.getHeight());
        productResponseDTO.setWidth(product.getWidth());

        // Lọc danh sách PriceDetail để lấy giá hiện tại
        Optional<PriceResponseDTO> priceNow = product.getPriceDetails().stream()
                .filter(pd -> pd.getStartAt().isBefore(currentDate) || pd.getStartAt().isEqual(currentDate))
                .filter(pd -> pd.getEndAt() == null || pd.getEndAt().isAfter(currentDate) || pd.getEndAt().isEqual(currentDate))
                .map(pd -> new PriceResponseDTO(
                        pd.getPrice().getId(),
                        pd.getPrice().getName(),
                        pd.getPrice1(),
                        pd.getStartAt(),
                        pd.getEndAt()
                )) // Lấy giá trị price1 từ PriceDetail
                .findFirst(); // Lấy giá đầu tiên thỏa mãn điều kiện (nếu có)
        if (priceNow.isPresent()) {
            PriceResponseDTO priceResponseDTO = priceNow.get();
            productResponseDTO.setPriceId(priceResponseDTO.getPriceId());
            productResponseDTO.setPriceNameNow(priceResponseDTO.getPriceName());
            productResponseDTO.setPriceNow(priceResponseDTO.getPrice());
            productResponseDTO.setPriceStartAtNow(priceResponseDTO.getStartAt());
            productResponseDTO.setPriceEndAtNow(priceResponseDTO.getEndAt());
        }
        productResponseDTO.setListPrice(product.getPriceDetails().stream().map(this::convertPriceToDTO).collect(Collectors.toList()));
        return productResponseDTO;
    }

    private PriceResponseDTO convertPriceToDTO(PriceDetail priceDetail) {
        PriceResponseDTO priceResponseDTO = new PriceResponseDTO();
        priceResponseDTO.setPriceId(priceDetail.getPrice().getId());
        priceResponseDTO.setPriceName(priceDetail.getPrice().getName());
        priceResponseDTO.setPrice(priceDetail.getPrice1());
        priceResponseDTO.setStartAt(priceDetail.getStartAt());
        priceResponseDTO.setEndAt(priceDetail.getEndAt());
        return priceResponseDTO;
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
        LocalDateTime currentDate = LocalDateTime.now();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setManufacturerName(product.getManufacturer().getName());
        productResponseDTO.setCategoryName(product.getCategory().getName());
        productResponseDTO.setStock(product.getStock());
        productResponseDTO.setCountSales(countSales(product.getOrderDetails(), product.getProductId()));
        productResponseDTO.setIsPresent(product.getIsPresent());
        String image = product.getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null);
        List<String> avatarProduct = new ArrayList<>();
        avatarProduct.add(image);
        productResponseDTO.setImage(avatarProduct);
        // Lọc danh sách PriceDetail để lấy giá hiện tại
        Optional<BigDecimal> priceNow = product.getPriceDetails().stream()
                .filter(pd -> pd.getStartAt().isBefore(currentDate) || pd.getStartAt().isEqual(currentDate))
                .filter(pd -> pd.getEndAt() == null || pd.getEndAt().isAfter(currentDate) || pd.getEndAt().isEqual(currentDate))
                .map(PriceDetail::getPrice1) // Lấy giá trị price1 từ PriceDetail
                .findFirst(); // Lấy giá đầu tiên thỏa mãn điều kiện (nếu có)

        priceNow.ifPresent(productResponseDTO::setPrice);
        priceNow.ifPresent(productResponseDTO::setPriceSale);

        return productResponseDTO;
    }

    @Transactional
    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setStock(0);
        Category category = categoryService.getCategoryById(productRequestDTO.getCategoryId());
        product.setCategory(category);
        Manufacturer manufacturer = manufacturerService.getManufacturerById(productRequestDTO.getManufacturerId());
        product.setManufacturer(manufacturer);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());
        product.setDescription(productRequestDTO.getDescription());
        product.setWeight(productRequestDTO.getWeight());
        product.setHeight(productRequestDTO.getHeight());
        product.setWidth(productRequestDTO.getWidth());
        product.setLength(productRequestDTO.getLength());
        product.setIsPresent(productRequestDTO.getIsPresent());
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
        // insert price
        int priceId = priceService.getPriceIdByPriceName(productRequestDTO.getNamePrice());
        // insert price detail
        PriceDetailDTO priceDetailDTO = new PriceDetailDTO();
        priceDetailDTO.setProductId(savedProduct.getProductId());
        priceDetailDTO.setPriceId(priceId);
        priceDetailDTO.setPrice(BigDecimal.valueOf(Integer.parseInt(productRequestDTO.getPrice())));
        priceDetailDTO.setStartAt(productRequestDTO.getStartDatePrice());
        priceDetailDTO.setEndAt(productRequestDTO.getEndDatePrice());
        priceDetailService.savePriceDetail(priceDetailDTO);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(savedProduct.getProductId());

        return productResponseDTO;
    }

    public void updateIsPresent(int productId, int isPresent) {
        repository.updateIsPresent(isPresent, productId);
    }

    public void updateStockProduct(int quantity, int productId) {
        repository.updateStockProduct(quantity, productId);
    }

    @Transactional
    public void updateProduct(ProductRequestUpdateDTO productRequestUpdateDTO) {
        // đây là sản phẩm chưa cập nhật trong db
        ProductDetailResponseDTO productDetailResponseDTO = convertToProductDetailDTO(repository.findByproductId(productRequestUpdateDTO.getProductId()).get());

        System.out.println(productDetailResponseDTO.getListDetail().toString());
        System.out.println(productRequestUpdateDTO.getProductDetails().toString());
        // update các thuôộc tính khác
        repository.updateProduct(productRequestUpdateDTO.getName(),
                productRequestUpdateDTO.getDescription(), productRequestUpdateDTO.getCategoryId(),
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
            // nếu có thuoc tinh moi thi them
            for (ProductDetailRequestDTO productDetailRequestDTO : productRequestUpdateDTO.getProductDetails()) {
                if (!productDetailResponseDTO.getListDetail().contains(productDetailRequestDTO)) {

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

        // update image
        //
        List<String> imageBE = productDetailResponseDTO.getImage();
        List<String> imageFE = productRequestUpdateDTO.getImages();
        for (String s : imageFE) { // có trên fe mà không có dưới be thì thêm
            if (!imageBE.contains(s)) {
                Image image = new Image();
                Product product = repository.findByproductId(productDetailResponseDTO.getProductId()).get();
                image.setProduct(product);
                image.setUrl(s);
                if (imageService.countImageProduct(productDetailResponseDTO.getProductId()) == 0) {
                    image.setAvatar(true);
                } else image.setAvatar(false);
                imageService.addNewImage(image);
            }

        }
        for (String s : imageBE) { // có dưới be mà không có trên fe thì xóa
            if (!imageFE.contains(s)) {
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
                // xóa price detail
                priceDetailService.deletePriceDetail(productId);
                // xóa product
                repository.deleteProductById(productId);
                return 1; // xóa thành công
            }
            return -2; // xóa thất bại do đã tồn tại trong order
        }
        return -1; // không tìm thấy product
    }

    public int getStockProduct(int productId) {
        return repository.getStockProduct(productId);
    }

}
