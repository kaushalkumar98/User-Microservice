package com.app.team2.technotribe.krasvbank.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.team2.technotribe.krasvbank.dto.CreateBankAccountDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.external.services.ExternalTransactionService;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.service.impl.AdminServiceImpl;

public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private ExternalTransactionService externalTransactionService;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActivateUserSuccess() {
        // Arrange
        UpdateAccountStatusRequest request = new UpdateAccountStatusRequest();
        request.setAccountNumber("12345");
        User user = new User();
        user.setAccountNumber("12345");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setPhoneNumber("1234567890");
        user.setAlternativePhoneNumber("0987654321");

        when(userRepo.findByAccountNumber("12345")).thenReturn(user);
        when(externalTransactionService.createAccount(any(CreateBankAccountDto.class))).thenReturn("Account Created");

        // Act
        String result = adminService.activateUser(request);

        // Assert
        verify(userRepo, times(1)).save(any(User.class));
        verify(externalTransactionService, times(1)).createAccount(any(CreateBankAccountDto.class));
        assertEquals("Account Created", result);
    }

    @Test
    void testActivateUserNotFound() {
        // Arrange
        UpdateAccountStatusRequest request = new UpdateAccountStatusRequest();
        request.setAccountNumber("12345");

        when(userRepo.findByAccountNumber("12345")).thenReturn(null);

        // Act
        String result = adminService.activateUser(request);

        // Assert
        assertEquals("user not found", result);
    }

    @Test
    void testGetUserByStatus() {
        // Arrange
        GetUserByStatusDto request = new GetUserByStatusDto();
        request.setStatus("ACTIVE");
        User user = new User();
        user.setStatus("ACTIVE");

        when(userRepo.findByStatus("ACTIVE")).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = adminService.getUserByStatus(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    void testGetUserByAccNum() {
        // Arrange
        GetUserByAccNumDto request = new GetUserByAccNumDto();
        request.setAccountNumber("12345");
        User user = new User();
        user.setAccountNumber("12345");

        when(userRepo.findByAccountNumber("12345")).thenReturn(user);

        // Act
        User result = adminService.getUserByAccNum(request);

        // Assert
        assertEquals("12345", result.getAccountNumber());
    }

    @Test
    void testInactiveUserSuccess() {
        // Arrange
        UpdateAccountStatusRequest request = new UpdateAccountStatusRequest();
        request.setAccountNumber("12345");
        User user = new User();
        user.setAccountNumber("12345");

        when(userRepo.findByAccountNumber("12345")).thenReturn(user);

        // Act
        String result = adminService.inactiveUser(request);

        // Assert
        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("User is Inactive", result);
    }

    @Test
    void testInactiveUserNotFound() {
        // Arrange
        UpdateAccountStatusRequest request = new UpdateAccountStatusRequest();
        request.setAccountNumber("12345");

        when(userRepo.findByAccountNumber("12345")).thenReturn(null);

        // Act
        String result = adminService.inactiveUser(request);

        // Assert
        assertEquals("user not found", result);
    }

    @Test
    void testUpdateUserDetailsSuccess() {
        // Arrange
        User user = new User();
        user.setAccountNumber("12345");

        when(userRepo.findByAccountNumber("12345")).thenReturn(user);

        // Act
        String result = adminService.updateUserDetails(user);

        // Assert
        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("User Details Saved Successfully", result);
    }

    @Test
    void testUpdateUserDetailsInvalidAccountNumber() {
        // Arrange
        User user = new User();
        user.setAccountNumber("12345");

        when(userRepo.findByAccountNumber("12345")).thenReturn(null);

        // Act
        String result = adminService.updateUserDetails(user);

        // Assert
        assertEquals("Invalid Account Number", result);
    }
}
