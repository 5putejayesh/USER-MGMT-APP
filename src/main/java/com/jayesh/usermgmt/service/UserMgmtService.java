package com.jayesh.usermgmt.service;

import java.util.List;

import com.jayesh.usermgmt.dto.ActivateAccount;
import com.jayesh.usermgmt.dto.Login;
import com.jayesh.usermgmt.dto.User;

public interface UserMgmtService {

	public boolean saveUser(User user);
	public boolean activateUserAcc(ActivateAccount activateAccount);
	public List<User> getAllUsers();
	public User getUserById(Integer id);
	public User getUserByEmail(String email);
	public boolean deleteuserbyId(Integer id);
	public boolean changeAccountStatus(Integer id, String status);
	public String login(Login login);
	public String forgotPassword(String email);
}
