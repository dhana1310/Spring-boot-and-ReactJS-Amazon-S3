package com.asyouwish.datastore;

import com.asyouwish.entity.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {

    List<UserProfile> findAll();

    Optional<UserProfile> findById(UUID userProfileId);
}
