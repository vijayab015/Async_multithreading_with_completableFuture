package com.vbharti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vbharti.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vbharti.repository.UserRepositoy;

@Service
public class UserService {

	@Autowired
	private UserRepositoy repository;
	Object target;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	@Async
	public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception {
		long startTime = System.currentTimeMillis();
		List<User> users = parseCSVFile(file);
		logger.info("Saving the List of User in DB of Size {} " + users.size() + "Thread name "
				+ Thread.currentThread().getName());
		users = repository.saveAll(users);
		long endTime = System.currentTimeMillis();
		logger.info("Total Time take {} ", (endTime - startTime));
		return CompletableFuture.completedFuture(users);

	}

	@Async
	public CompletableFuture<List<User>> findAllUser() {
		logger.info("Get list of User {} " + Thread.currentThread().getName());
		List<User> users = repository.findAll();
		return CompletableFuture.completedFuture(users);
	}

	private List<User> parseCSVFile(final MultipartFile file) throws Exception {
		final List<User> users = new ArrayList<>();
		try {
			try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					final String[] data = line.split(",");
					final User user = new User();
					user.setName(data[0]);
					user.setEmail(data[1]);
					user.setGender(data[2]);
					users.add(user);
				}
				return users;
			}
		} catch (final IOException e) {
			logger.error("Failed to parse CSV file {}", e);
			throw new Exception("Failed to parse CSV file {}", e);
		}
	}

}
