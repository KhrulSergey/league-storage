package com.freetonleague.storage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudStorageConfig {

	@Value("${freetonleague.service.cloud-storage.endpoint}")
	private String cloudSpaceEndpoint;

	@Value("${freetonleague.service.cloud-storage.key}")
	private String cloudSpaceKey;

	@Value("${freetonleague.service.cloud-storage.secret}")
	private String cloudSpaceSecret;

	@Value("${freetonleague.service.cloud-storage.region}")
	private String cloudSpaceRegion;

	@Bean
	public AmazonS3 cloudClient() {
		BasicAWSCredentials creds = new BasicAWSCredentials(cloudSpaceKey, cloudSpaceSecret);
		return AmazonS3ClientBuilder.standard()
				.withEndpointConfiguration(new EndpointConfiguration(cloudSpaceEndpoint, cloudSpaceRegion))
				.withCredentials(new AWSStaticCredentialsProvider(creds)).build();
	}
}
