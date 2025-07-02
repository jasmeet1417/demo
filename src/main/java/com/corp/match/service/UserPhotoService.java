package com.corp.match.service;



import com.corp.match.entity.UserPhoto;
import com.corp.match.entity.UserProfile;
import com.corp.match.repository.UserPhotoRepository;
import com.corp.match.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserPhotoService {

   

    @Autowired
    private UserProfileRepository userProfileRepository;

    public String uploadAndSavePhoto(Long userId, MultipartFile file, int position) {
        Optional<UserProfile> userOpt = userProfileRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return "User not found.";
        }

        UserProfile user = userOpt.get();
        if (user.getPhotos().size() >= 10) {
            return "You can only upload up to 10 photos.";
        }

        try {
            // Save file to local folder
            String uploadDir = "uploads/"; // make sure this folder exists
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), path);

            // Assuming you host static files under http://localhost:8080/images/
            String photoUrl = "http://localhost:8080/images/" + fileName;

            UserPhoto photo = new UserPhoto(photoUrl, position, user);
            user.getPhotos().add(photo);
            userProfileRepository.save(user);

            return "Photo uploaded and saved successfully.";
        } catch (IOException e) {
            return "Error uploading photo: " + e.getMessage();
        }
    }

}
