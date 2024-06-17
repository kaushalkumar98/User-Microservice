package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.CreateBankAccountDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.external.services.ExternalTransactionService;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;

import lombok.Data;

@Data
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserRepository userRepo;
	@Autowired
	ExternalTransactionService externalTransactionService;

	@Override
	public String activateUser(UpdateAccountStatusRequest request) {
		User user = userRepo.findByAccountNumber(request.getAccountNumber());
		if (user != null) {
			user.setStatus("ACTIVE");
			userRepo.save(user);
			
			CreateBankAccountDto newacc = CreateBankAccountDto.builder()
					.name(user.getName())
					.accountNumber(user.getAccountNumber())
					.accountBalance(BigDecimal.ZERO)
					.email(user.getEmail())
					.password(user.getPassword())
					.phoneNumber(user.getPhoneNumber())
					.alternativePhoneNumber(user.getAlternativePhoneNumber())
					.build();
			
			return externalTransactionService.createAccount(newacc);
			
			
		}
		return "user not found";
	}

	@Override
	public List<User> getUserByStatus(GetUserByStatusDto request) {
		return userRepo.findByStatus(request.getStatus());

	}

	@Override
	public User getUserByAccNum(GetUserByAccNumDto request) {
		// TODO Auto-generated method stub
		return userRepo.findByAccountNumber(request.getAccountNumber());
	}

	@Override
	public String inactiveUser(UpdateAccountStatusRequest request) {
		User user = userRepo.findByAccountNumber(request.getAccountNumber());
		if (user != null) {
			user.setStatus("INACTIVE");
			userRepo.save(user);
			return "User is Inactive";
		}
		return "user not found";
	}

	@Override
	public String updateUserDetails(User user) {
		if(null!=userRepo.findByAccountNumber(user.getAccountNumber())){
			userRepo.save(user);
			return "User Details Saved Successfully";
		}
		return "Invalid Account Number";
	}

}
