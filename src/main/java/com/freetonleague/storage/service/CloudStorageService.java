package com.freetonleague.storage.service;

import com.freetonleague.storage.domain.model.MediaResource;

import java.io.InputStream;

public interface CloudStorageService {

	void saveCloudResource(MediaResource mediaResource);

	InputStream getCloudResource(MediaResource mediaResource);
}
