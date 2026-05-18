package com.example.cloud_kitchen.Repository;


import com.example.cloud_kitchen.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {


    List<Notification> findNotificationByUserId(Integer userId);


}
