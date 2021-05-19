package com.freetonleague.storage.service;

import com.freetonleague.storage.domain.dto.MediaResourceDto;
import com.freetonleague.storage.domain.model.MediaResource;

/**
 * Service-facade for managing media resources
 */
public interface RestMediaResourceFacade {

    /**
     * Returns founded media resource by hash and ownerGUID
     *
     * @param hash      of media resource to search
     * @param ownerGUID of media resource to search
     * @return media resource entity
     */
    MediaResourceDto getMediaResourceByHashAndOwnerGUID(String hash, String ownerGUID);

    /**
     * Returns raw file if media resource by hash
     *
     * @param hash of media resource to search
     * @return inputStream of resource file
     */
    MediaResource getMediaResourceByHash(String hash);

    /**
     * Returns founded media resource by hash
     *
     * @param hash of media resource to search
     * @return media resource entity
     */
    MediaResourceDto getMediaResourceInfoByHash(String hash);

    /**
     * Add new resource to external service and DB.  (for micro service)
     *
     * @param mediaResource to be added
     * @return Added media resource
     */
    MediaResourceDto addResource(MediaResourceDto mediaResource);

//    /**
//     * Edit docket in DB.
//     *
//     * @param docketDto to be edited
//     * @return Edited docket
//     */
//    DocketDto editDocket(DocketDto docketDto);
//
//    /**
//     * Delete docket in DB.
//     *
//     * @param id of docket to search
//     * @return deleted docket
//     */
//    DocketDto deleteDocket(long id);
//
//
//    /**
//     * Getting docket by id and user with privacy check
//     */
//    Docket getVerifiedDocketById(long id);
}
