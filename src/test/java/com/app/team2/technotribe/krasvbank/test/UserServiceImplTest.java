package com.app.team2.technotribe.krasvbank.test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.team2.technotribe.krasvbank.Security.JwtUtils;
import com.app.team2.technotribe.krasvbank.dto.*;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.entity.UserRole;
import com.app.team2.technotribe.krasvbank.external.services.ExternalTransactionService;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.service.impl.UserServiceImpl;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;

@ExtendWith(MockitoExtension.class)
 class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ExternalTransactionService externalTransactionService;

    @InjectMocks
    private UserServiceImpl userService;

    private SignupRequest signupRequest;
    private User user;
    private CreditDebitRequest creditDebitRequest;
    private EnquiryRequest enquiryRequest;
    private TransferRequest transferRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("John Doe", "Male", "123 Street", "State", "email@example.com",
                                          "password", "1234567890", "0987654321", "USER", "ACTIVE");
        user = User.builder()
                   .id(1L)
                   .name("John Doe")
                   .gender("Male")
                   .address("123 Street")
                   .stateOfOrigin("State")
                   .accountNumber("123456789")
                   .accountBalance(BigDecimal.ZERO)
                   .email("email@example.com")
                   .password("encodedPassword")
                   .phoneNumber("1234567890")
                   .alternativePhoneNumber("0987654321")
                   .status("INACTIVE")
                   .role(UserRole.ROLE_USER)
                   .build();
        creditDebitRequest = new CreditDebitRequest("123456789", BigDecimal.TEN, "password");
        enquiryRequest = new EnquiryRequest("123456789", "password");
        transferRequest = new TransferRequest("123456789", "987654321", BigDecimal.TEN, "password");
    }

    @Test
    void testCreateAccount() {
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        BankResponse response = userService.createAccount(signupRequest);

        assertEquals(AccountUtils.ACCOUNT_CREATION_SUCCESS, response.getResponseCode());
        assertEquals(AccountUtils.ACCOUNT_CREATION_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getAccountInfo());
    }

    @Test
    void testCreateAccountAlreadyExists() {
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        BankResponse response = userService.createAccount(signupRequest);

        assertEquals(AccountUtils.ACCOUNT_EXISTS_CODE, response.getResponseCode());
        assertEquals(AccountUtils.ACCOUNT_EXISTS_MESSAGE, response.getResponseMessage());
        assertNull(response.getAccountInfo());
    }

    @Test
    void testBalanceEnquiry() {
        when(userRepository.findByAccountNumber(enquiryRequest.getAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(enquiryRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(externalTransactionService.balanceEnquiry(enquiryRequest.getAccountNumber())).thenReturn(BigDecimal.TEN);

        BankResponse response = userService.balanceEnquiry(enquiryRequest);

        assertEquals(AccountUtils.ACCOUNT_FOUND_CODE, response.getResponseCode());
        assertEquals(AccountUtils.ACCOUNT_FOUND_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getAccountInfo());
        assertEquals(BigDecimal.TEN, response.getAccountInfo().getAccountBalance());
    }

    @Test
    void testBalanceEnquiryIncorrectPassword() {
        when(userRepository.findByAccountNumber(enquiryRequest.getAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(enquiryRequest.getPassword(), user.getPassword())).thenReturn(false);

        BankResponse response = userService.balanceEnquiry(enquiryRequest);

        assertEquals(AccountUtils.INCORRECT_PASSWORD_MESSAGE, response.getResponseMessage());
    }

    @Test
    void testCreditAccount() {
        when(userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(creditDebitRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(externalTransactionService.creditAccount(creditDebitRequest)).thenReturn(new BankResponse());

        BankResponse response = userService.creditAccount(creditDebitRequest);

        assertNotNull(response);
    }

    @Test
    void testCreditAccountIncorrectPassword() {
        when(userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(creditDebitRequest.getPassword(), user.getPassword())).thenReturn(false);

        BankResponse response = userService.creditAccount(creditDebitRequest);

        assertEquals(AccountUtils.INCORRECT_PASSWORD_CODE, response.getResponseCode());
        assertEquals(AccountUtils.INCORRECT_PASSWORD_MESSAGE, response.getResponseMessage());
    }

    @Test
    void testDebitAccount() {
        when(userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(creditDebitRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(externalTransactionService.debitAccount(creditDebitRequest)).thenReturn(new BankResponse());

        BankResponse response = userService.debitAccount(creditDebitRequest);

        assertNotNull(response);
    }

    @Test
    void testDebitAccountIncorrectPassword() {
        when(userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(creditDebitRequest.getPassword(), user.getPassword())).thenReturn(false);

        BankResponse response = userService.debitAccount(creditDebitRequest);

        assertEquals(AccountUtils.INCORRECT_PASSWORD_CODE, response.getResponseCode());
        assertEquals(AccountUtils.INCORRECT_PASSWORD_MESSAGE, response.getResponseMessage());
    }

    @Test
    void testTransfer() {
        when(userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber())).thenReturn(true);
        when(userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(transferRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(externalTransactionService.transfer(transferRequest)).thenReturn(new BankResponse());

        BankResponse response = userService.transfer(transferRequest);

        assertNotNull(response);
    }

    @Test
    void testTransferIncorrectPassword() {
        when(userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber())).thenReturn(true);
        when(userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber())).thenReturn(user);
        when(passwordEncoder.matches(transferRequest.getPassword(), user.getPassword())).thenReturn(false);

        BankResponse response = userService.transfer(transferRequest);

        assertEquals(AccountUtils.INCORRECT_PASSWORD_CODE, response.getResponseCode());
        assertEquals(AccountUtils.INCORRECT_PASSWORD_MESSAGE, response.getResponseMessage());
    }
}

