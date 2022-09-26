package com.jayesh.usermgmt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jayesh.usermgmt.constants.AppConstants;
import com.jayesh.usermgmt.dto.ActivateAccount;
import com.jayesh.usermgmt.dto.Login;
import com.jayesh.usermgmt.dto.User;
import com.jayesh.usermgmt.props.ResponseMessageProperties;
import com.jayesh.usermgmt.service.UserMgmtService;

@RestController
public class UserController {

	
	private UserMgmtService userMgmtService;
	private Map<String,String> messages;
	
	@Autowired
	public UserController(UserMgmtService userMgmtService,ResponseMessageProperties properties) {
		this.userMgmtService=userMgmtService;
		this.messages=properties.getMessages();
	}
	
	@PostMapping("/user")
	public ResponseEntity<Object> userReg(@RequestBody User user){
		boolean saveUser = userMgmtService.saveUser(user);
		if(saveUser) {
			return generateUserRegResponse(messages.get(AppConstants.REG_SUCCESS) +AppConstants.EMPTY_STR+user.getEmail(),HttpStatus.OK,user);
		}else {
			return  generateUserRegResponse(messages.get(AppConstants.REG_FAILED),HttpStatus.INTERNAL_SERVER_ERROR,user);
		}
		
	}
	
	private ResponseEntity<Object> generateUserRegResponse(String msg, HttpStatus status,User user) {
		Map<String, Object> regResponse=new HashMap<>();
		regResponse.put("message", msg);
		regResponse.put("status", status.value());
		regResponse.put("data", user);
		return new ResponseEntity<>(regResponse,status);
	}

	@PostMapping("/user/activate")
	public ResponseEntity<String> activateUserAccount(@RequestBody ActivateAccount account){
		boolean isAccountActivated = userMgmtService.activateUserAcc(account);
		
		if(isAccountActivated) {
			return new ResponseEntity<>(messages.get(AppConstants.ACTIVATION_SUCCESS),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(messages.get(AppConstants.ACTIVATION_FAILED),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		return new ResponseEntity<>(userMgmtService.getAllUsers(),HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId){
		return new ResponseEntity<>(userMgmtService.getUserById(userId),HttpStatus.OK);
	}
	
	@GetMapping("/user/email/{email}")
	public ResponseEntity<User> getUserByEmailId(@PathVariable String email){
		return new ResponseEntity<>(userMgmtService.getUserByEmail(email),HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
		boolean isDeleted = userMgmtService.deleteuserbyId(userId);
		
		if(isDeleted) {
			return new ResponseEntity<>(messages.get(AppConstants.USER_DELETED),HttpStatus.OK);	
		}else {
			return new ResponseEntity<>(messages.get(AppConstants.USER_NOT_DELETED),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/user/{userId}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer userId,@PathVariable String status){
		boolean userStatusChanged = userMgmtService.changeAccountStatus(userId, status);
		if(userStatusChanged) {
			return new ResponseEntity<>(messages.get(AppConstants.USER_ST_CHANGED),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(messages.get(AppConstants.USER_ST_NOT_CHANGED),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<String> loginUser(@RequestBody Login login){
		return new ResponseEntity<>(userMgmtService.login(login),HttpStatus.OK);
	}
	
	@GetMapping("/user/forgotpwd/{email}")
	public ResponseEntity<String> recoverPassword(@PathVariable String email){
		return new ResponseEntity<>(userMgmtService.forgotPassword(email),HttpStatus.OK);
	}
	
}
