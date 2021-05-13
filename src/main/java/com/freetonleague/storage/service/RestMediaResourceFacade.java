package com.freetonleague.storage.service;

import com.freetonleague.storage.domain.dto.MediaResourceDto;

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
     * Returns founded media resource by hash (for micro service)
     *
     * @param hash      of media resource to search
     * @return media resource entity
     */
    MediaResourceDto getMediaResourceByHash(String hash);

//    /**
//     * Add new docket to DB.
//     *
//     * @param docketDto to be added
//     * @return Added docket
//     */
//    DocketDto addDocket(DocketDto docketDto);
//
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
