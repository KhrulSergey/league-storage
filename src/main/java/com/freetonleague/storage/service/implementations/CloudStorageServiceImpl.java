package com.freetonleague.storage.service.implementations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.freetonleague.storage.domain.model.MediaResource;
import com.freetonleague.storage.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    private final AmazonS3 cloudStorageClient;
    @Value("${freetonleague.service.cloud-storage.bucket}")
    private String cloudSpaceBucket;

    //TODO remove deleting method until 01/09/2021 if no need
//	@Override
//	public void deleteFile(Long fileId) throws Exception {
//		Optional<Image> imageOpt = imageRepo.findById(fileId);
//		if (imageOpt.get() != null) {
//			Image image = imageOpt.get();
//			String key = FOLDER + image.getName() + "." + image.getExt();
//			cloudStorageClient.deleteObject(new DeleteObjectRequest(doSpaceBucket, key));
//			imageRepo.delete(image);
//		}
//	}

    @Override
    public void saveCloudResource(MediaResource mediaResource) {
        log.debug("^ trying to save file with hash {} to cloud storage", mediaResource.getHashKey());
        if (isNull(mediaResource.getRawResourceData())) {
            log.error("!> requesting find media resource with findByHashAndOwner for BLANK hashKey. Check evoking clients");
            return;

        }
        String cloudKey = this.composeCloudHashKey(mediaResource);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(mediaResource.getSizeInBytes());
        metadata.setContentType(mediaResource.getResourceType().getHttpExtension());

        PutObjectResult putObjectResult = cloudStorageClient.putObject(
                new PutObjectRequest(cloudSpaceBucket, cloudKey, mediaResource.getRawResourceData(), metadata)
                        .withCannedAcl(CannedAccessControlList.Private));
        log.debug("^ successfully saved file {} to cloud storage {}", cloudKey, putObjectResult.getVersionId());
    }

    @Override
    public InputStream getCloudResource(MediaResource mediaResource) {
        log.debug("^ trying to get file with hash {} from cloud storage", mediaResource.getHashKey());
        String cloudKey = this.composeCloudHashKey(mediaResource);
        S3Object s3object = cloudStorageClient.getObject(cloudSpaceBucket, cloudKey);
        log.debug("^ successfully get file {} from cloud storage", s3object);
        return s3object.getObjectContent();
    }

    private String composeCloudHashKey(MediaResource mediaResource) {
        String folderName = mediaResource.getResourceType().getFileType().getFolderName();
        String extension = mediaResource.getResourceType().getExtension();
        return String.format("%s/%s.%s", folderName, mediaResource.getHashKey(), extension);
    }
}
