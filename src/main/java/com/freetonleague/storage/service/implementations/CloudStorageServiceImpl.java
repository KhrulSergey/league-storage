package com.freetonleague.storage.service.implementations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.freetonleague.storage.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    private final AmazonS3 cloudStorageClient;
    @Value("${freetonleague.service.cloud-storage.bucket}")
    private String cloudSpaceBucket;

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
    public void saveFile(MultipartFile multipartFile, String key) throws IOException {
        log.debug("^ trying to save file {} to cloud storage", multipartFile.getName());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getInputStream().available());
        if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
            metadata.setContentType(multipartFile.getContentType());
        }
        PutObjectResult putObjectResult = cloudStorageClient.putObject(new PutObjectRequest(cloudSpaceBucket, key, multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        log.debug("^ successfully saved file {} to cloud storage {}", multipartFile.getName(), putObjectResult.getVersionId());
    }

    @Override
    public MultipartFile getImage(String key) throws IOException {
        log.debug("^ trying to get file with key {} from cloud storage", key);
        S3Object s3object = cloudStorageClient.getObject(cloudSpaceBucket, key);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream,
                new File("/Users/Khsa/Documents/FreeTonLeague/file.jpg"));
        log.debug("^ successfully get file {} from cloud storage", s3object);
        return null;
    }
}
