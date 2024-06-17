package com.app.team2.technotribe.krasvbank.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.app.team2.technotribe.krasvbank.controller.AdminController;
import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.service.impl.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private UpdateAccountStatusRequest updateAccountStatusRequest;
    private GetUserByStatusDto getUserByStatusDto;
    private GetUserByAccNumDto getUserByAccNumDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

        user = new User();
        user.setAccountNumber("1234567890");

        updateAccountStatusRequest = new UpdateAccountStatusRequest();
        updateAccountStatusRequest.setAccountNumber("1234567890");

        getUserByStatusDto = new GetUserByStatusDto();
        getUserByStatusDto.setStatus("ACTIVE");

        getUserByAccNumDto = new GetUserByAccNumDto();
        getUserByAccNumDto.setAccountNumber("1234567890");
    }

    @Test
    void testMethod() throws Exception {
        mockMvc.perform(get("/api/admin/page"))
                .andExpect(status().isOk())
                .andExpect(content().string("admin page"));
    }

    @Test
    void testActivateUser() throws Exception {
        when(adminService.activateUser(any(UpdateAccountStatusRequest.class))).thenReturn("User activated");

        mockMvc.perform(post("/api/admin/activateuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountStatusRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User activated"));

        verify(adminService, times(1)).activateUser(any(UpdateAccountStatusRequest.class));
    }

    @Test
    void testDeactivateUser() throws Exception {
        when(adminService.inactiveUser(any(UpdateAccountStatusRequest.class))).thenReturn("User deactivated");

        mockMvc.perform(post("/api/admin/inactiveUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountStatusRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User deactivated"));

        verify(adminService, times(1)).inactiveUser(any(UpdateAccountStatusRequest.class));
    }

    @Test
    void testGetUserByStatus() throws Exception {
        when(adminService.getUserByStatus(any(GetUserByStatusDto.class))).thenReturn(Collections.singletonList(user));

        mockMvc.perform(post("/api/admin/usersbystatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserByStatusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("1234567890"));

        verify(adminService, times(1)).getUserByStatus(any(GetUserByStatusDto.class));
    }

    @Test
    void testGetUserByAccNum() throws Exception {
        when(adminService.getUserByAccNum(any(GetUserByAccNumDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/userbyaccountnumber")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserByAccNumDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890"));

        verify(adminService, times(1)).getUserByAccNum(any(GetUserByAccNumDto.class));
    }

    @Test
    void testUpdateUserDetails() throws Exception {
        when(adminService.updateUserDetails(any(User.class))).thenReturn("User updated");

        mockMvc.perform(put("/api/admin/updateuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated"));

        verify(adminService, times(1)).updateUserDetails(any(User.class));
    }
}
