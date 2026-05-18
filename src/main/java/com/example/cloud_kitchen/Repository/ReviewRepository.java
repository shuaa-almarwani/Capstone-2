package com.example.cloud_kitchen.Repository;


import com.example.cloud_kitchen.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);

    List<Review> findReviewByVendorId(Integer vendorId);

    List<Review> findReviewByUserId(Integer userId);


}
