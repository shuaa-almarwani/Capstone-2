package com.example.cloud_kitchen.Controller;

import com.example.cloud_kitchen.Api.ApiResponse;
import com.example.cloud_kitchen.Model.Review;
import com.example.cloud_kitchen.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody @Valid Review review,
                                       Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        reviewService.addReview(review);
        return ResponseEntity.status(201).body(new ApiResponse("review added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id,
                                          @RequestBody @Valid Review review,
                                          Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        reviewService.updateReview(id, review);
        return ResponseEntity.ok(new ApiResponse("review updated"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {

        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse("review deleted"));
    }





    // #10
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getByVendor(@PathVariable Integer vendorId) {
        return ResponseEntity.ok(reviewService.getByVendor(vendorId));
    }

    // #11
    @GetMapping("user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }




    // #12
    @GetMapping("/{vendorId}/reviews-analysis")
    public ResponseEntity<Map<String, Object>> analyzeVendorReviews(@PathVariable Integer vendorId) {

        Map<String, Object> result = reviewService.analyzeVendorReviews(vendorId);

        return ResponseEntity.ok(result);
    }
}