package com.freetonleague.storage.service.implementations;

import com.freetonleague.storage.domain.dto.MediaResourceDto;
import com.freetonleague.storage.domain.dto.MediaResourceMetaDataDto;
import com.freetonleague.storage.domain.enums.ResourceFileExtensionType;
import com.freetonleague.storage.domain.enums.ResourceStatusType;
import com.freetonleague.storage.domain.model.MediaResource;
import com.freetonleague.storage.exception.ExceptionMessages;
import com.freetonleague.storage.exception.MediaResourceManageException;
import com.freetonleague.storage.mapper.MediaResourceMapper;
import com.freetonleague.storage.security.permissions.CanManageResource;
import com.freetonleague.storage.service.MediaResourceService;
import com.freetonleague.storage.service.RestMediaResourceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

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
        if (!mediaResource.getCreatorGUID().toString().equals(ownerGUID)) {
            log.warn("~ Media resource with requested hash {} was not enabled for {}. 'getVerifiedMediaResourceByHash' in " +
                    "getMediaResourceByHashAndOwnerGUID request denied", hash, ownerGUID);
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_NOT_FOUND_ERROR,
                    "Visible media resource with requested hash " + hash + " and ownerGUID " + ownerGUID + " was not found");
        }
        return mediaResourceMapper.toDto(mediaResource);
    }

    /**
     * Returns raw file of media resource by hash
     */
    @Override
    public MediaResource getMediaResourceByHash(String hash) {
        return this.getVerifiedMediaResourceByHash(hash);
    }

    /**
     * Returns founded media resource by hash
     */
    @Override
    public MediaResourceDto getMediaResourceInfoByHash(String hash) {
        return mediaResourceMapper.toDto(this.getVerifiedMediaResourceByHash(hash));
    }

    /**
     * Add new resource to external service and DB.
     */
    @CanManageResource
    @Override
    public MediaResourceDto addResource(MediaResourceDto mediaResourceDto) {
        // Parse raw data #"data:image/png;base64,abcdefghijklmnopqrstuvwxyz0123456789"
        String[] rawImageWithMetadata = mediaResourceDto.getRawData().split(",");

        if (isEmpty(rawImageWithMetadata)) {
            log.warn("~ raw image data is not valid");
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_CREATION_ERROR,
                    "raw image data is not valid");
        }

        //decode the extension of raw data
        ResourceFileExtensionType resourceFileExtensionType = ResourceFileExtensionType
                .fromHttpExtension(rawImageWithMetadata[0]);
        if (isNull(resourceFileExtensionType)) {
            log.warn("~ file extension is not recognized");
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_CREATION_ERROR,
                    "file extension is not recognized");
        }

        // decode the content of raw data
        byte[] decodedBytes = Base64
                .getDecoder()
                .decode(rawImageWithMetadata[1]);

        MediaResourceMetaDataDto mediaResourceMetaDataDto = new MediaResourceMetaDataDto();
        // parse metadata from image
        if (resourceFileExtensionType.getFileType().isImage()) {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(decodedBytes)) {
                BufferedImage image = ImageIO.read(stream);
                if (isNull(image)) {//If image=null means that the upload is not an image format
                    throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_CREATION_ERROR,
                            "file content is not match specified extension");
                }
                mediaResourceMetaDataDto.setWidth(image.getWidth());
                mediaResourceMetaDataDto.setHeight(image.getHeight());
                mediaResourceMetaDataDto.setDimension(String.format("%sx%s", image.getWidth(), image.getHeight()));
                mediaResourceMetaDataDto.setBitDepth(image.getColorModel().getPixelSize());
            } catch (IOException e) {
                log.debug("!!> error while convert to file");
                throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_CREATION_ERROR,
                        "Error while convert base64 string to file");
            }
        }
        //set protected properties from decoded file
        mediaResourceDto.setStatus(ResourceStatusType.ACTIVE);
        mediaResourceDto.setResourceType(resourceFileExtensionType);
        mediaResourceDto.setSizeInBytes(decodedBytes.length);
        mediaResourceDto.setResourceMetaData(mediaResourceMetaDataDto);

        MediaResource mediaResource = this.getVerifiedMediaResourceByDto(mediaResourceDto);
        mediaResource.setRawResourceData(new ByteArrayInputStream(decodedBytes));

        mediaResource = mediaResourceService.add(mediaResource);

        if (isNull(mediaResource)) {
            log.error("!> error while creating media resource from dto {}.", mediaResourceDto);
            throw new MediaResourceManageException(ExceptionMessages.MEDIA_RESOURCE_CREATION_ERROR,
                    "Media resource was not saved on Portal. Check requested params.");
        }
        return mediaResourceMapper.toDto(mediaResource);
    }

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
            log.debug("^ transmitted MediaResourceDto.name:{}, hash {} have constraint violations: {}",
                    mediaResourceDto.getName(), mediaResourceDto.getHashKey(), violations);
            throw new ConstraintViolationException(violations);
        }
        Set<ConstraintViolation<MediaResourceMetaDataDto>> violationsMetaData = validator.validate(mediaResourceDto.getResourceMetaData());
        if (!violationsMetaData.isEmpty()) {
            log.debug("^ transmitted MetaData {} of MediaResourceDto.name: {} have constraint violations: {}",
                    mediaResourceDto.getResourceMetaData(), mediaResourceDto.getName(), violationsMetaData);
            throw new ConstraintViolationException(violationsMetaData);
        }
        return mediaResourceMapper.fromDto(mediaResourceDto);
    }
}
