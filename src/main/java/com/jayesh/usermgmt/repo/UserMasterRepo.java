package com.jayesh.usermgmt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jayesh.usermgmt.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Integer>{
	Optional<UserMaster> findByEmail(String email);
	Optional<UserMaster> findByEmailAndPassword(String email,String password);

}
