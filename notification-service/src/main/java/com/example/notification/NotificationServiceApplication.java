package com.example.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    @Autowired
    private JavaMailSender javaMailSender; // Inject the JavaMailSender

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // send out an email notification
        log.info("Received Notification for order - {}", orderPlacedEvent.getOrderNumber());

        System.out.println("Sending email...");

        // Call a method to send the email
        sendEmail(orderPlacedEvent);
    }

    private void sendEmail(OrderPlacedEvent orderPlacedEvent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your-email@example.com");
            message.setTo("customer-email@example.com"); // Replace with the customer's email address
            message.setSubject("Order Confirmation - Order Number: " + orderPlacedEvent.getOrderNumber());
            message.setText("Dear Customer,\n\nYour order with order number "
                    + orderPlacedEvent.getOrderNumber() + " has been successfully placed.");

            javaMailSender.send(message);

            log.info("Email sent successfully!");
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage(), e);
        }
    }
}
