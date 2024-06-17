package com.app.team2.technotribe.krasvbank.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.File;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.app.team2.technotribe.krasvbank.dto.EmailDetails;
import com.app.team2.technotribe.krasvbank.service.impl.EmailServiceImpl;

public class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmailAlert() {
        // Arrange
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient("test@example.com");
        emailDetails.setMessageBody("Test message");
        emailDetails.setSubject("Test subject");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("sender@example.com");
        mailMessage.setTo(emailDetails.getRecipient());
        mailMessage.setText(emailDetails.getMessageBody());
        mailMessage.setSubject(emailDetails.getSubject());

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmailAlert(emailDetails);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    
}

