package com.example.Product.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary("cloudinary://772226489956594:OnRNIJeGWPKd0kTOsFEFLV8iOGQt@dgylpud5a");
    }
}

