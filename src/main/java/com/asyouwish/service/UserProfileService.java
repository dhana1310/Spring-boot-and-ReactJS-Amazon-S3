package com.asyouwish.service;

import com.asyouwish.buckets.BucketName;
import com.asyouwish.datastore.UserProfileRepository;
import com.asyouwish.entity.UserProfile;
import com.asyouwish.filestore.FileStore;
import com.asyouwish.properties.UserProfileProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@PropertySource(value = {"classpath:application.yml"})
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final FileStore fileStore;

    @Autowired
    private UserProfileProperty userProfileProperty;

    @Autowired
    public UserProfileService(@Qualifier("fakeUserProfileDataStore") UserProfileRepository userProfileRepository, FileStore fileStore) {
        this.userProfileRepository = userProfileRepository;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    public String uploadUserProfileImage(UUID userProfileId, MultipartFile file) throws IOException {

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfileId);
        if (file.isEmpty() || userProfileOptional.isEmpty() || file.getOriginalFilename() == null) {
            return null;
        }

        String imageName = file.getOriginalFilename();
        String filenameExtension = StringUtils.getFilenameExtension(imageName);
        if (!userProfileProperty.getImageExtensions().contains(filenameExtension.toLowerCase())) {
            return null;
        }

        if (!Arrays.asList(
                MediaType.IMAGE_JPEG_VALUE,
                MediaType.IMAGE_PNG_VALUE,
                MediaType.IMAGE_GIF_VALUE)
                .contains(file.getContentType())) {
            return null;
        }


        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.value(), userProfileId);
        String filename = String.format("%s-%s", userProfileId, file.getOriginalFilename());
        fileStore.save(path, filename, file.getInputStream(), Optional.of(metadata));

        userProfileOptional.get().setUserProfileImageLink(filename);
        return "Successfully saved";
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfileId);
        if(userProfileOptional.isEmpty()) {
            return null;
        }
        UserProfile userProfile = userProfileOptional.get();
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.value(), userProfileId);

        return userProfile.getUserProfileImageLink().map(key -> fileStore.download(userProfileId.toString(), path, key)).orElse(new byte[0]);

    }
}
