package com.doc.management.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doc.management.payload.UploadDocResponse;
import com.doc.management.service.AmazonClient;

@RestController
public class DocController {

	private static final Logger logger = LoggerFactory.getLogger(DocController.class);

	private AmazonClient amazonClient;

	@Autowired
	DocController(AmazonClient amazonClient) {
		this.amazonClient = amazonClient;
	}

	@PostMapping("/uploadFile")
	public UploadDocResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		logger.info("request recieved to upload file.");
		String fileDownloadUri = this.amazonClient.uploadFile(file);
		return new UploadDocResponse(file.getName(), fileDownloadUri, file.getContentType(), file.getSize());
	}


}
