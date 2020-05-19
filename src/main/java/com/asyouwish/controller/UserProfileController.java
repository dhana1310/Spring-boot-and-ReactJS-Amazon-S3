package com.asyouwish.controller;

import com.asyouwish.entity.UserProfile;
import com.asyouwish.service.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/userprofiles")
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getUserProfile() {
        return userProfileService.getAllUsers();
    }

    @PostMapping(value = "/{userProfileId}/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadProfileImage(@PathVariable("userProfileId")UUID userProfileId, @RequestParam("file") MultipartFile file) throws IOException {

        String response = userProfileService.uploadUserProfileImage(userProfileId, file);

       return Optional.ofNullable(response).map(res -> new ResponseEntity<>("Uploaded successfully",HttpStatus.OK))
                .orElse(new ResponseEntity<>("Upload Failed", HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/{userProfileId}/download/image")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable("userProfileId")UUID userProfileId) throws IOException {

        byte[] response = userProfileService.downloadUserProfileImage(userProfileId);

        return Optional.ofNullable(response).map(res -> new ResponseEntity<>(response,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
