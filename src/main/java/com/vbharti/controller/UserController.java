package com.vbharti.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vbharti.entity.User;
import com.vbharti.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService service;

	@PostMapping(value = "/addusers", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")

	public ResponseEntity saveUser(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
		for (MultipartFile file : files) {
			service.saveUser(file);
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@GetMapping(value = "/getusers", produces = "application/json")
	public CompletableFuture<ResponseEntity> findAllUser() {
		return service.findAllUser().thenApply(ResponseEntity::ok);
	}
	
	@GetMapping(value = "/getusersByThread", produces = "application/json")
	public ResponseEntity getUsers()
	{
		CompletableFuture<List<User>> user1=service.findAllUser();
		CompletableFuture<List<User>> user2=service.findAllUser();
		CompletableFuture<List<User>> user3=service.findAllUser();
		CompletableFuture.allOf(user1,user2,user3).join();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	

}
