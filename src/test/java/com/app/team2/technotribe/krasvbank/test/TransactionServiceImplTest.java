package com.app.team2.technotribe.krasvbank.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.team2.technotribe.krasvbank.dto.TransactionDto;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.repository.TransactionRepository;
import com.app.team2.technotribe.krasvbank.service.impl.TransactionServiceImpl;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTransaction() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("DEBIT");
        transactionDto.setAmount(new BigDecimal("100.00"));
        transactionDto.setAccountNumber("12345");
        transactionDto.setStatus("PENDING");

        // Act
        transactionService.saveTransaction(transactionDto);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}

