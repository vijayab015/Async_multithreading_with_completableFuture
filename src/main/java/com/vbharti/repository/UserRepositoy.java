package com.vbharti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbharti.entity.User;

public interface UserRepositoy extends JpaRepository<User, Integer> {
	
	

}
