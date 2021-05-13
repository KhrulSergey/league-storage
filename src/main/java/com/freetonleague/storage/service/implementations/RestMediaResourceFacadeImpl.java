package com.freetonleague.storage.service.implementations;

import com.freetonleague.storage.domain.dto.MediaResourceDto;
import com.freetonleague.storage.domain.model.MediaResource;
import com.freetonleague.storage.exception.ExceptionMessages;
import com.freetonleague.storage.exception.MediaResourceManageException;
import com.freetonleague.storage.mapper.MediaResourceMapper;
import com.freetonleague.storage.service.MediaResourceService;
import com.freetonleague.storage.service.RestMediaResourceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestMediaResourceFacadeImpl implements RestMediaResourceFacade {

    private final MediaResourceService mediaResourceService;
    private final MediaResourceMapper mediaResourceMapper;
    private final Validator validator;

    /**
     * Returns founded media resource by hash and ownerGUID
     */
    @Override
    public MediaResourceDto getMediaResourceByHashAndOwnerGUID(String hash, String ownerGUID) {
        MediaResource mediaResource = this.getVerifiedMediaResourceByHash(hash);
        if (!mediaResource.getCreatorGUID().toString().equals(ownerGUID)){
            log.warn("~ Media resource with requested hash {} was not enabled for {}. 'getVerifiedMediaResourceByHash' in " +
                    "getMediaResourceByHashAndOwnerGUID request denied", hash, ownerGUID);
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_NOT_FOUND_ERROR,
                    "Visible media resource with requested hash " + hash + " and ownerGUID " + ownerGUID + " was not found");
        }
        return mediaResourceMapper.toDto(mediaResource);
    }

    /**
     * Returns founded media resource by hash (for micro service)
     */
    @Override
    public MediaResourceDto getMediaResourceByHash(String hash) {
        return mediaResourceMapper.toDto(this.getVerifiedMediaResourceByHash(hash));
    }

//    /**
//     * Add new docket to DB.
//     */
//    @CanManageDocket
//    @Override
//    public DocketDto addDocket(DocketDto docketDto) {
//        docketDto.setId(null);
//        docketDto.setStatus(DocketStatusType.CREATED);
//
//        Docket docket = this.getVerifiedDocketByDto(docketDto);
//        docket = mediaResourceService.addDocket(docket);
//
//        if (isNull(docket)) {
//            log.error("!> error while creating docket from dto {}.", docketDto);
//            throw new DocketManageException(ExceptionMessages.DOCKET_CREATION_ERROR,
//                    "Docket was not saved on Portal. Check requested params.");
//        }
//        return mediaResourceMapper.toDto(docket);
//    }
//

    /**
     * Getting media resource by hash and user with privacy check
     */
    private MediaResource getVerifiedMediaResourceByHash(String hash) {
        MediaResource mediaResource = mediaResourceService.findByHash(hash);
        if (isNull(mediaResource)) {
            log.debug("^ Media resource with requested hash {} was not found. 'getVerifiedMediaResourceByHash' " +
                    "in RestMediaResourceFacade request denied", hash);
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_NOT_FOUND_ERROR,
                    "Media resource with requested hash " + hash + " was not found");
        }
        if (mediaResource.getStatus().isDeleted()) {
            log.debug("^ Media resource with requested hash {} was {}. 'getVerifiedMediaResourceByHash' in " +
                    "RestMediaResourceFacade request denied", hash, mediaResource.getStatus());
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_VISIBLE_ERROR,
                    "Visible media resource with requested hash " + hash + " was not found");
        }
        return mediaResource;
    }

    /**
     * Getting media resource by DTO with deep validation and privacy check
     */
    private MediaResource getVerifiedMediaResourceByDto(MediaResourceDto mediaResourceDto) {
        // Verify MediaResource information
        Set<ConstraintViolation<MediaResourceDto>> violations = validator.validate(mediaResourceDto);
        if (!violations.isEmpty()) {
            log.debug("^ transmitted MediaResourceDto : {} have constraint violations: {}", mediaResourceDto, violations);
            throw new ConstraintViolationException(violations);
        }
        return mediaResourceMapper.fromDto(mediaResourceDto);
    }
}