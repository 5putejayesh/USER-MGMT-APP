package com.jayesh.usermgmt.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayesh.usermgmt.constants.AppConstants;
import com.jayesh.usermgmt.dto.ActivateAccount;
import com.jayesh.usermgmt.dto.Login;
import com.jayesh.usermgmt.dto.User;
import com.jayesh.usermgmt.entity.UserMaster;
import com.jayesh.usermgmt.props.ResponseMessageProperties;
import com.jayesh.usermgmt.repo.UserMasterRepo;
import com.jayesh.usermgmt.service.UserMgmtService;
import com.jayesh.usermgmt.util.EmailUtils;
import com.jayesh.usermgmt.util.ReadResourcesFile;

@Service
public class UserMgmtserviceImpl implements UserMgmtService {

	private Logger logger=LoggerFactory.getLogger(UserMgmtserviceImpl.class);
	
	private UserMasterRepo userMasterRepo;
	private ModelMapper modelMapper;
	private EmailUtils emailUtils;
	
	private Map<String, String> regmail;
	private Map<String, String> recovermail;
	private Map<String,String> messages;
	
	@Autowired
	public UserMgmtserviceImpl(UserMasterRepo userMasterRepo,ModelMapper modelMapper,EmailUtils emailUtils,ResponseMessageProperties  properties) {
		this.userMasterRepo=userMasterRepo;
		this.modelMapper=modelMapper;
		this.emailUtils=emailUtils;
		this.regmail=properties.getRegmail();
		this.recovermail=properties.getRecovermail();
		this.messages=properties.getMessages();
	}
	@Override
	public boolean saveUser(User user) {
		//map dto to entity
		UserMaster userMaster=mapToEntity(user);
		userMaster.setPassword(UUID.randomUUID().toString());
		userMaster.setAccStatus("In-Active");
		UserMaster savedUser = userMasterRepo.save(userMaster);
		
		//send email logic
		String subject=regmail.get(AppConstants.MAIL_SUBJECT);
		String fileName=regmail.get(AppConstants.MAIL_BODY);
		String emailBody = ReadResourcesFile.getFileDataAsStringSat(fileName, userMaster.getFullName(), userMaster.getPassword(), "url");
		
		emailUtils.sendEmail(userMaster.getEmail(), subject, emailBody);
		return savedUser.getId()!=null;
	}
	
	@Override
	public boolean activateUserAcc(ActivateAccount activateAccount) {
		//fetch user record by email
		Optional<UserMaster> findByEmail = userMasterRepo.findByEmail(activateAccount.getEmail());
		if(findByEmail.isPresent()) {
			UserMaster userMaster = findByEmail.get();
			if(userMaster.getPassword().equals(activateAccount.getTempPwd())) {
			
				userMaster.setAccStatus("Active");
				userMaster.setPassword(activateAccount.getConfirmPwd());
				userMasterRepo.save(userMaster);
				return true;
			}
			
		}
		return false;
	}

	@Override
	public List<User> getAllUsers() {
		return userMasterRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	
	@Override
	public User getUserById(Integer id) {
		Optional<UserMaster> findById = userMasterRepo.findById(id);
		if(findById.isPresent()) {
			return mapToDto(findById.get());
		}
		return null;
	}

	@Override
	public User getUserByEmail(String email) {
		Optional<UserMaster> findByEmail = userMasterRepo.findByEmail(email);
		if(findByEmail.isPresent()) {
			UserMaster userMaster = findByEmail.get();
			return mapToDto(userMaster);
		}
		return null;
	}

	@Override
	public boolean deleteuserbyId(Integer id) {
		try {
			userMasterRepo.deleteById(id);
			return true;
		}catch (Exception e) {
			logger.error("Exception occurred",e);
			
		}
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer id, String status) {

		Optional<UserMaster> findById = userMasterRepo.findById(id);
		if(findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(status);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String login(Login login) {
		String loginMsg="";
		 Optional<UserMaster> findByEmailAndPassword = userMasterRepo.findByEmailAndPassword(login.getEmail(),login.getPassword());
		if(findByEmailAndPassword.isPresent()) {
			UserMaster userMaster = findByEmailAndPassword.get();
			if(userMaster.getAccStatus().equals("Active")) {
				loginMsg=messages.get(AppConstants.LOGIN_SUCCESS);
			}else {
				loginMsg=messages.get(AppConstants.LOGIN_INACTIVAE);
			}
		}else {
			loginMsg=messages.get(AppConstants.LOGIN_INVALID);
		}
		return loginMsg;
	}

	@Override
	public String forgotPassword(String email) {
		Optional<UserMaster> findByEmail = userMasterRepo.findByEmail(email);
		if(!findByEmail.isPresent()) {
			return messages.get(AppConstants.FORGOTPASS_FAILED);
		}
		UserMaster userMaster = findByEmail.get();
		
		String subject=recovermail.get(AppConstants.MAIL_SUBJECT);
		String fileName=recovermail.get(AppConstants.MAIL_BODY);
		String emailBody = ReadResourcesFile.getFileDataAsStringSat(fileName, userMaster.getFullName(), userMaster.getPassword(), "url");
		
		boolean sendEmail = emailUtils.sendEmail(emailBody, subject, emailBody);
		if(sendEmail) {
			return messages.get(AppConstants.FORGOTPASS_SUCCESS);
		}
		return null;
	}

	private UserMaster mapToEntity(User user) {
		return modelMapper.map(user, UserMaster.class);		
	}
	
	private User mapToDto(UserMaster userMaster) {
		return modelMapper.map(userMaster, User.class);
	}
}
