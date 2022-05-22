package com.sankore.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sankore.bank.auth.util.LoginRequestUtil;
import com.sankore.bank.repositories.SecureUserRepo;
import com.sankore.bank.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private SecureUserRepo secureUserRepo;

    @Test
    public void testGenerateToken() throws Exception {
        // TODO: This test is incomplete.
        //   Reason: Failed to create Spring context.
        //   Please contact Diffblue support.
        //   See https://diff.blue/E019

        LoginRequestUtil loginRequestUtil = new LoginRequestUtil();
        loginRequestUtil.setPassword("iloveyou");
        loginRequestUtil.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginRequestUtil);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/ ")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.authController).build().perform(requestBuilder);
    }
}

