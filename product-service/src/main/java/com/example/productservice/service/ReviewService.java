package com.example.productservice.service;

import com.example.productservice.dto.ReviewRequestDTO;
import com.example.productservice.dto.ReviewResponseGuestDTO;
import com.example.productservice.entity.*;
import com.example.productservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(reviewRequestDTO.getOrderDetailId());
        review.setUser(user);
        review.setOrderDetail(orderDetail);
        review.setComment(reviewRequestDTO.getComment());
        review.setRating(reviewRequestDTO.getRatting());
        review.setCreatedAt(LocalDateTime.now());
        return convertToDTO(repository.save(review));
    }

    private ReviewResponseGuestDTO convertToDTO(Review review){
        ReviewResponseGuestDTO reviewResponseGuestDTO = new ReviewResponseGuestDTO();
        reviewResponseGuestDTO.setReviewId(review.getReviewId());
        reviewResponseGuestDTO.setUsername(review.getUser().getFirstName() + " " + review.getUser().getLastName());
        reviewResponseGuestDTO.setComment(review.getComment());
        reviewResponseGuestDTO.setRating(review.getRating());
        reviewResponseGuestDTO.setCreateAt(review.getCreatedAt());
        return reviewResponseGuestDTO;
    }


    public List<ReviewResponseGuestDTO> getListReviewByProductId(int productId){
        List<Review> listReview = repository.getListReviewByProductId(productId);
        return listReview.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public boolean hasUserReviewed(int orderDetailId) {
        return repository.hasReviewed(orderDetailId);
    }
}
