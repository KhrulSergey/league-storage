package com.freetonleague.storage.service;

import com.freetonleague.storage.domain.model.MediaResource;

import java.util.List;
import java.util.UUID;


public interface MediaResourceService {

    /**
     * Adding a new media resource to the database
     *
     * @param mediaResource new data to add
     * @return added media resource
     */
    MediaResource add(MediaResource mediaResource);

    /**
     * Getting a media resource by Hash from the database
     *
     * @param hashKey resource hashKey to search
     * @return User data or null - if the user is not found
     */
    MediaResource findByHash(String hashKey);

    /**
     * Getting a user by Hash amd Owner GUID from the database
     *
     * @param hashKey   resource hashKey to search
     * @param ownerGUID identifier of owner
     * @return User data or null - if the user is not found
     */
    MediaResource findByHashAndOwner(String hashKey, UUID ownerGUID);

    /**
     * Getting a media resource by Hash from the database
     *
     * @param ownerGUID resource hashKey to search
     * @return User data or null - if the user is not found
     */
    List<MediaResource> findAllByOwner(UUID ownerGUID);

    /**
     * Returns sign of existence resource by hash
     */
    boolean isExistsResourceByHash(String hashKey);
}
