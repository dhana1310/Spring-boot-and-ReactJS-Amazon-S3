package com.asyouwish.datastore;

import com.asyouwish.entity.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore implements UserProfileRepository {

    private static List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("5f65f85e-ec12-41b4-ae33-9d3c9859811a"), "Dhananjay", "362d290e-1e44-434c-975a-413c722db1d6-Dhana.jpeg"));
        USER_PROFILES.add(new UserProfile(UUID.fromString("b6d3aded-0ea5-40fe-939c-aae5fdcdd59c"), "Famia", "c9bc3dd6-830e-46a9-a769-f39cc8ea0c0a-Famu.jpeg"));
        USER_PROFILES.add(new UserProfile(UUID.fromString("34ba633b-a95d-4781-a3a3-2b69d6bb6242"), "Mandeep", "e776d3ca-fed9-4961-8985-9a1c70122fa0-Mandeep.jpeg"));
    }

    @Override
    public List<UserProfile> findAll() {
        return  USER_PROFILES;
    }

    @Override
    public Optional<UserProfile> findById(UUID userProfileId) {
        return USER_PROFILES.stream().filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId)).findFirst();
    }
}
