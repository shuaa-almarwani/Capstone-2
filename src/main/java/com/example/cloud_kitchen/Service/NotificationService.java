package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Notification;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Repository.NotificationRepository;
import com.example.cloud_kitchen.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Value("${resendApiKey}")
    private String resendApiKey;

    @Value("${WhatsAppToken}")
    private String whatsappToken;

    @Value("${WhatsAppInstance}")
    private String whatsappInstance;

    private final RestTemplate restTemplate = new RestTemplate();


   public List<Notification> getAll() {
        return notificationRepository.findAll();
    }


    public List<Notification> getByUser(Integer userId) {

        List<Notification> notifications =
                notificationRepository.findNotificationByUserId(userId);

        if (notifications.isEmpty()) {
            throw new ApiException("No notifications found");
        }

        return notifications;
    }



    public void sendWelcomeNotification(Integer userId, String role) {

        String msg =
                "Welcome to Cloud Kitchen 🍽️\n\n" +
                        "Your " + role + " account has been created successfully.\n\n" +
                        "You can now start using our services and track your orders easily.";


        sendNotification(userId, null, "WELCOME", msg);
    }


    public void sendOrderUpdate(Integer userId,
                                Integer orderId,
                                String status) {

        String msg =
                "Hello " + userRepository.findUserById(userId).getName() + ",\n\n" +
                        "Your order #" + orderId +
                        " status has been updated to: " + status + ".\n" +
                        "Thank you for choosing Cloud Kitchen.";

        sendNotification(userId, orderId, "ORDER_UPDATE", msg);
    }


    public void sendInvoiceNotification(Integer userId,
                                        Integer orderId,
                                        Double amount) {

        String msg =
                "Invoice for Order #" + orderId + "\n" +
                        "Total Price: " + amount + " SAR\n\n" +
                        "Thank you for choosing Cloud Kitchen.";

        sendNotification(userId, orderId, "INVOICE", msg);
    }


    public void sendDeleteAccountNotification(Integer userId) {

        String msg =
                "Your account has been deleted successfully.";

        sendNotification(userId, null, "ACCOUNT_DELETE", msg);
    }



    private void sendNotification(Integer userId,
                                  Integer orderId,
                                  String type,
                                  String msg) {

        User user = getUser(userId);

        Notification notification = new Notification();

        notification.setUserId(user.getId());
        notification.setOrderId(orderId);
        notification.setType(type);
        notification.setMessage(msg);

        boolean whatsappSent = false;
        boolean emailSent = false;

        try {

            sendWhatsApp(user.getPhone(), msg);
            whatsappSent = true;

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
        // SEND EMAIL
        try {

            sendEmail(
                    user.getEmail(),
                    generateSubject(type, orderId),
                    msg
            );

            emailSent = true;

        } catch (Exception e) {

            System.out.println("Email Error");
        }

        notification.setSentByWhatsApp(whatsappSent);
        notification.setSentByEmail(emailSent);

        notificationRepository.save(notification);
    }

    private void sendWhatsApp(String phone,
                              String message) {

        String url =
                "https://api.ultramsg.com/"
                        + whatsappInstance +
                        "/messages/chat";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );

        MultiValueMap<String, String> params =
                new LinkedMultiValueMap<>();

        params.add("token", whatsappToken);
        params.add("to", phone);
        params.add("body", message);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        restTemplate.postForEntity(
                url,
                request,
                String.class
        );
    }



    private void sendEmail(String toEmail,
                           String subject,
                           String content) {

        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        String html =
                "<h2>Cloud Kitchen</h2>" +
                        "<p>" + content + "</p>";

        Map<String, Object> body = new HashMap<>();

        body.put("from", "onboarding@resend.dev");
        body.put("to", new String[]{toEmail});
        body.put("subject", subject);
        body.put("html", html);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                url,
                request,
                String.class
        );
    }


    // EMAIL SUBJECT
    private String generateSubject(String type,
                                   Integer orderId) {

        switch (type) {

            case "WELCOME":
                return "Welcome to Cloud Kitchen";

            case "ORDER_UPDATE":
                return "Your Order #" + orderId + " Status Update";

            case "INVOICE":
                return "Invoice for Order #" + orderId;

            case "ACCOUNT_DELETE":
                return "Your Cloud Kitchen Account Has Been Deleted";

            default:
                return "Cloud Kitchen Notification";
        }
    }


    private User getUser(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        return user;
    }
}