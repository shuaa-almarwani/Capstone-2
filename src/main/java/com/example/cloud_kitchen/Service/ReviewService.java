package com.example.cloud_kitchen.Service;


import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Order;
import com.example.cloud_kitchen.Model.Review;
import com.example.cloud_kitchen.Repository.OrderRepository;
import com.example.cloud_kitchen.Repository.ReviewRepository;
import com.example.cloud_kitchen.Repository.UserRepository;
import com.example.cloud_kitchen.Repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final OrderRepository orderRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // public void addReview(Review review) {
    //     if (userRepository.findUserById(review.getUserId()) == null) {
    //         throw new ApiException("User not found");
    //     }
    //     if (vendorRepository.findVendorById(review.getVendorId()) == null) {
    //         throw new ApiException("Vendor not found");
    //     }

    //     Order completedOrder = orderRepository.findFirstByUserIdAndVendorIdAndStatus(
    //             review.getUserId(),
    //             review.getVendorId(),
    //             "DELIVERED"
    //     );

    //     if (completedOrder == null) {
    //         throw new ApiException("You cannot review this vendor because you haven't completed any orders from them yet");
    //     }

    //     reviewRepository.save(review);
    // }
    public void addReview(Integer userId, Integer vendorId, Review review) {

    if (userRepository.findUserById(userId) == null) {
        throw new ApiException("User not found");
    }

    if (vendorRepository.findVendorById(vendorId) == null) {
        throw new ApiException("Vendor not found");
    }

    Order completedOrder = orderRepository.findFirstByUserIdAndVendorIdAndStatus(
            userId,
            vendorId,
            "DELIVERED"
    );

    if (completedOrder == null) {
        throw new ApiException("You cannot review this vendor because you haven't completed any orders from them yet");
    }

    review.setUserId(userId);
    review.setVendorId(vendorId);
    review.setOrderId(completedOrder.getId());

    reviewRepository.save(review);
}
    public void updateReview(Integer id, Review review) {

        Review oldReview = reviewRepository.findReviewById(id);

        if (oldReview == null) {
            throw new ApiException("review not found");
        }

        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());

        reviewRepository.save(oldReview);
    }
    public void deleteReview(Integer id) {

        Review review = reviewRepository.findReviewById(id);

        if (review == null) {
            throw new ApiException("review not found");
        }

        reviewRepository.delete(review);
    }



    public List<Review> getByVendor(Integer vendorId) {

        List<Review> reviews = reviewRepository.findReviewByVendorId(vendorId);

        if (reviews.isEmpty()) {
            throw new ApiException("no reviews found for this vendor");
        }
        return reviews;
    }



     public List<Review> getReviewsByUserId(Integer userId) {
        List<Review> reviews = reviewRepository.findReviewByUserId(userId);
        if (reviews.isEmpty()) {
            throw new ApiException("no reviews found for this user");
        }
        return reviews;
    }

    private final OpenAIService openAIService;


     public Map<String, Object> analyzeVendorReviews(Integer vendorId) {

        List<Review> reviews = reviewRepository.findReviewByVendorId(vendorId);

        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this vendor");
        }

        List<Map<String, String>> analyzedReviews = new ArrayList<>();

        int positive = 0;
        int negative = 0;
        int neutral = 0;

        for (Review review : reviews) {

            Map<String, String> analysis = openAIService.analyzeComment(review.getComment());

            String sentiment = analysis.get("sentiment");

            if (sentiment.equals("POSITIVE")) positive++;
            else if (sentiment.equals("NEGATIVE")) negative++;
            else neutral++;

            Map<String, String> reviewResult = new HashMap<>();
            reviewResult.put("review", review.getComment());
            reviewResult.put("sentiment", sentiment);
            reviewResult.put("reason", analysis.get("reason"));

            analyzedReviews.add(reviewResult);
        }

        String overall;

        if (positive > negative) overall = "POSITIVE";
        else if (negative > positive) overall = "NEGATIVE";
        else overall = "NEUTRAL";

        Map<String, Object> response = new HashMap<>();

        response.put("vendorId", vendorId);
        response.put("totalReviews", reviews.size());
        response.put("positive", positive);
        response.put("negative", negative);
        response.put("neutral", neutral);
        response.put("overallSentiment", overall);
        response.put("reviews", analyzedReviews);

        return response;
    }
}
