package com.freetonleague.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudStorageService {

	void saveFile(MultipartFile multipartFile, String key) throws IOException;

//	void deleteFile(Long id) throws Exception;


	MultipartFile getImage(String key) throws IOException;
}
