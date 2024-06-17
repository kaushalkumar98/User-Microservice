package com.app.team2.technotribe.krasvbank.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.app.team2.technotribe.krasvbank.Security.JwtUtils;
import com.app.team2.technotribe.krasvbank.bankStatement.BankStatement;
import com.app.team2.technotribe.krasvbank.controller.UserController;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.SigninRequest;
import com.app.team2.technotribe.krasvbank.dto.SigninResponse;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.dto.SignupRequest;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.repository.TokenBlacklistRepository;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.service.impl.UserService;

public class UserControllerTest {

    @Mock
    private JwtUtils utils;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Mock
    private AuthenticationManager mgr;

    @Mock
    private BankStatement bankStatement;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateBankStatement() {
        List<Transaction> transactions = Collections.emptyList();
        String accountNumber = "123456789";
        String startDate = "2022-01-01";
        String endDate = "2022-12-31";

        when(bankStatement.generateStatement(accountNumber, startDate, endDate)).thenReturn(transactions);

        List<Transaction> result = userController.generateBankStatemant(accountNumber, startDate, endDate);

        verify(bankStatement, times(1)).generateStatement(accountNumber, startDate, endDate);
    }

    @Test
    void testCreateAccount() {
        SignupRequest userRequest = new SignupRequest("John", "Doe", "123 Street", "NY", "john.doe@example.com", "password123", "9876543210", "9876543211", "User", null);
        BankResponse bankResponse = new BankResponse("200", "Account created", null);

        when(userService.createAccount(any(SignupRequest.class))).thenReturn(bankResponse);

        BankResponse result = userController.createAccount(userRequest);

        verify(userService, times(1)).createAccount(userRequest);
    }

    @Test
    void testLogout() {
        String authHeader = "Bearer testToken";
        String token = "testToken";

        doNothing().when(tokenBlacklistRepository).add(token);

        ResponseEntity<String> response = userController.logout(authHeader);

        verify(tokenBlacklistRepository, times(1)).add(token);
    }

    @Test
    void testSignIn() {
        SigninRequest request = new SigninRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setAccountNumber("123456789");

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        String jwtToken = "jwtToken";

        User foundUser = new User();
        foundUser.setName("John Doe");
        foundUser.setAccountNumber(request.getAccountNumber());

        when(mgr.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(utils.generateJwtToken(authentication)).thenReturn(jwtToken);
        when(userRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(foundUser);

        ResponseEntity<?> response = userController.signIn(request);

        verify(mgr, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(utils, times(1)).generateJwtToken(authentication);
        verify(userRepository, times(1)).findByAccountNumber(request.getAccountNumber());
    }

    @Test
    void testBalanceEnquiry() {
        EnquiryRequest request = new EnquiryRequest("password123", "123456789");
        BankResponse bankResponse = new BankResponse("200", "Balance is 1000", null);

        when(userService.balanceEnquiry(request)).thenReturn(bankResponse);

        BankResponse response = userController.balanceEnquiry(request);

        verify(userService, times(1)).balanceEnquiry(request);
    }

    @Test
    void testNameEnquiry() {
        EnquiryRequest request = new EnquiryRequest("password123", "123456789");
        String userName = "John Doe";

        when(userService.nameEnquiry(request)).thenReturn(userName);

        String response = userController.nameEnquiry(request);

        verify(userService, times(1)).nameEnquiry(request);
    }

    @Test
    void testCreditAccount() {
        CreditDebitRequest request = new CreditDebitRequest("123456789", new BigDecimal("1000"), null);
        BankResponse bankResponse = new BankResponse("200", "Account credited", null);

        when(userService.creditAccount(request)).thenReturn(bankResponse);

        BankResponse response = userController.creditAccount(request);

        verify(userService, times(1)).creditAccount(request);
    }

    @Test
    void testDebitAccount() {
        CreditDebitRequest request = new CreditDebitRequest("123456789", new BigDecimal("1000"), null);
        BankResponse bankResponse = new BankResponse("200", "Account debited", null);

        when(userService.debitAccount(request)).thenReturn(bankResponse);

        BankResponse response = userController.debitAccount(request);

        verify(userService, times(1)).debitAccount(request);
    }

    @Test
    void testTransfer() {
        TransferRequest request = new TransferRequest("password123", "123456789", "987654321", new BigDecimal("1000"));
        BankResponse bankResponse = new BankResponse("200", "Transfer successful", null);

        when(userService.transfer(request)).thenReturn(bankResponse);

        BankResponse response = userController.transfer(request);

        verify(userService, times(1)).transfer(request);
    }
}
