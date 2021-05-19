package com.freetonleague.storage.service.implementations;

import com.freetonleague.storage.domain.model.MediaResource;
import com.freetonleague.storage.repository.MediaResourceRepository;
import com.freetonleague.storage.security.permissions.CanManageResource;
import com.freetonleague.storage.service.CloudStorageService;
import com.freetonleague.storage.service.MediaResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Implementation of a service for data access from repositories
 */
@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class MediaResourceServiceImpl implements MediaResourceService {

    private final MediaResourceRepository repository;
    private final CloudStorageService cloudStorageService;
    private final Validator validator;

    /**
     * Getting a user by Hash amd Owner GUID from the database
     */
    @CanManageResource
    @Override
    public MediaResource findByHashAndOwner(String hashKey, UUID ownerGUID) {
        if (isBlank(hashKey) || isNull(ownerGUID)) {
            log.error("!> requesting find media resource with findByHashAndOwner for BLANK hashKey {} or NULL " +
                    "ownerGUID {}. Check evoking clients", hashKey, ownerGUID);
            return null;
        }
        log.debug("^ trying to get media resource by hash {} and owner: {}", hashKey, ownerGUID);
        return repository.findByHashKeyAndCreatorGUID(hashKey, ownerGUID);
    }

    /**
     * Returns list of all MediaResource owned by specified GUID
     */
    @Override
    public List<MediaResource> findAllByOwner(UUID ownerGUID) {
        if (isNull(ownerGUID)) {
            log.error("!> requesting find media resource with findAllByOwner for ownerGUID. Check evoking clients");
            return null;
        }
        log.debug("^ trying to get media resource by ownerGUID: {}", ownerGUID);
        return repository.findAllByCreatorGUID(ownerGUID);
    }

    /**
     * Adding a new media resource to the database
     */
    @Override
    public MediaResource add(MediaResource mediaResource) {
        log.debug("^ trying to add new media resource {}", mediaResource);

        mediaResource.generateHash();
        cloudStorageService.saveCloudResource(mediaResource);

        if (!this.verifyMediaResource(mediaResource)) {
            return null;
        }
        log.debug("^ trying to add new media resource {}", mediaResource);
        return repository.save(mediaResource);
    }

    /**
     * Getting a media resource by Hash from the database
     */
    @Override
    public MediaResource findByHash(String hashKey) {
        if (isBlank(hashKey)) {
            log.error("!> requesting find media resource with findByHashAndOwner for BLANK hashKey. Check evoking clients");
            return null;
        }
        log.debug("^ trying to get media resource by hash {}", hashKey);
        MediaResource mediaResource = repository.findByHashKey(hashKey);
        if (isNull(mediaResource)) {
            return null;
        }

        InputStream rawResourceStream = cloudStorageService.getCloudResource(mediaResource);
        if (isNull(rawResourceStream)) {
            log.error("!> error while get file from cloud service {}", hashKey);
            return null;
        }
        mediaResource.setRawResourceData(rawResourceStream);
        return mediaResource;
    }

    /**
     * Returns sign of docket existence for specified id.
     */
    @Override
    public boolean isExistsResourceByHash(String hashKey) {
        return repository.existsByHashKey(hashKey);
    }

    /**
     * Validate tournament parameters and settings to modify
     */
    private boolean verifyMediaResource(MediaResource mediaResource) {
        if (isNull(mediaResource)) {
            log.error("!> requesting modify media resource with verifyMediaResource for NULL docket. Check evoking clients");
            return false;
        }
        Set<ConstraintViolation<MediaResource>> violations = validator.validate(mediaResource);
        if (!violations.isEmpty()) {
            log.error("!> requesting modify media resource id {} name {} with verifyMediaResource for resource with ConstraintViolations. Check evoking clients",
                    mediaResource.getId(), mediaResource.getHashKey());
            return false;
        }
        return true;
    }
}
