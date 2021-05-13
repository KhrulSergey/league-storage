package com.freetonleague.storage.repository;


import com.freetonleague.storage.domain.model.MediaResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

/**
 * Interface for data accessing of "Media Resource" entity from the database
 */
public interface MediaResourceRepository extends JpaRepository<MediaResource, Long>,
        JpaSpecificationExecutor<MediaResource> {

    MediaResource findByHashKey(String hashKey);

    MediaResource findByHashKeyAndCreatorGUID(String hashKey, UUID ownerGUID);

    boolean existsByHashKey(String hashKey);

    List<MediaResource> findAllByCreatorGUID(UUID ownerGUID);
}

