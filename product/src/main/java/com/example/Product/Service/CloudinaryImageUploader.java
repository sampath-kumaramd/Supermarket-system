package com.example.Product.Service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryImageUploader {

    private final Cloudinary cloudinary;

    public String uploadImage(Long productId, MultipartFile imageFile) throws IOException {
        Map<?, ?> params = ObjectUtils.asMap("public_id", "product_" + productId);
        Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), params);

        if (uploadResult.containsKey("secure_url")) {
            return uploadResult.get("secure_url").toString();
        } else {
            throw new RuntimeException("Secure URL not found in the Cloudinary response.");
            // Handle this case, e.g., throw an exception or log an error.
        }
    }
}