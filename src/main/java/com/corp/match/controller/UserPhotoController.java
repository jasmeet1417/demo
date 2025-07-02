package com.corp.match.controller;


import com.corp.match.service.UserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/photo")
public class UserPhotoController {

    @Autowired
    private UserPhotoService userPhotoService;

    @PostMapping(value = "", consumes = "multipart/form-data")
    public String uploadPhoto(@RequestParam Long userId,
                              @RequestParam int position,
                              @RequestParam("file") MultipartFile file) {
        return userPhotoService.uploadAndSavePhoto(userId, file, position);
    }

}
