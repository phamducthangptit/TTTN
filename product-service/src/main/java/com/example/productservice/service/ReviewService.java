package com.example.productservice.service;

import com.example.productservice.dto.ReviewRequestDTO;
import com.example.productservice.dto.ReviewResponseGuestDTO;
import com.example.productservice.entity.Order;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Review;
import com.example.productservice.entity.User;
import com.example.productservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    public ReviewResponseGuestDTO addNewReview(ReviewRequestDTO reviewRequestDTO){
        Review review = new Review();

        int userId = accountService.getUserId(reviewRequestDTO.getUserName());
        User user = userService.getUserById(userId);
        Product product = new Product();
        product.setProductId(reviewRequestDTO.getProductId());
        Order order = new Order();
        order.setOrderId(reviewRequestDTO.getOrderId());
        review.setOrder(order);
        review.setUser(user);
        review.setProduct(product);
        review.setComment(reviewRequestDTO.getComment());
        review.setRating(reviewRequestDTO.getRatting());
        review.setCreatedAt(LocalDateTime.now());
        return convertToDTO(repository.save(review));
    }

    private ReviewResponseGuestDTO convertToDTO(Review review){
        ReviewResponseGuestDTO reviewResponseGuestDTO = new ReviewResponseGuestDTO();
        reviewResponseGuestDTO.setReviewId(review.getReviewId());
        reviewResponseGuestDTO.setUsername(review.getUser().getFirstName() + " " + review.getUser().getLastName());
        System.out.println(review.getUser().toString());
        reviewResponseGuestDTO.setComment(review.getComment());
        reviewResponseGuestDTO.setRating(review.getRating());
        reviewResponseGuestDTO.setCreateAt(review.getCreatedAt());
        return reviewResponseGuestDTO;
    }

   public List<Review> gettAllReviewUser(int userId){
        return repository.findByUser_userId(userId);
   }

    public boolean hasUserReviewedProductInOrder(int userId, int productId, int orderId) {
        return repository.existsByUserIdAndProductIdAndOrderId(userId, productId, orderId);
    }
}
