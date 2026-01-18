package com.praveen.jpa.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.jpa.dto.UserDetailsRequest;
import com.praveen.jpa.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegistrationController.class)
class UserRegistrationControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private RegistrationService registrationService;

  @Test
  @DisplayName("Should register user successfully")
  void registerUserSuccess() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    UserDetailsRequest request = new UserDetailsRequest("testuser", "testpassword");

    Mockito.doNothing().when(registrationService).register(anyString(), anyString());

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().string("User registered"));
  }
}
