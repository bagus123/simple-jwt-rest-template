package com.tbs.app.controller;

import com.tbs.app.exception.AppException;
import com.tbs.app.model.Role;
import com.tbs.app.model.User;
import com.tbs.app.model.enums.RoleName;
import com.tbs.app.payload.request.SignInRequest;
import com.tbs.app.payload.request.SignUpRequest;
import com.tbs.app.payload.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.tbs.app.repository.RoleRepository;
import com.tbs.app.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void signUp() {
        // when
        ResponseEntity<?> response = restTemplate
                .postForEntity("/api/auth/signup", new SignUpRequest("anwar@gmail.com", "1234567"), ApiResponse.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void signUpWithInvalidEmail() {
        // when
        ResponseEntity<?> response = restTemplate
                .postForEntity("/api/auth/signup", new SignUpRequest("anwar", "1234567"), ApiResponse.class);
        // then
        ReadContext ctx = JsonPath.parse(toJson(response.getBody()));
        String message = ctx.read("$.message");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualToIgnoringCase("Validation Error : [email: must be a well-formed email address]");
    }

    @Test
    public void signUpWithBlankEmail() {
        // when
        ResponseEntity<?> response = restTemplate
                .postForEntity("/api/auth/signup", new SignUpRequest("", "1234567"), ApiResponse.class);
        // then
        ReadContext ctx = JsonPath.parse(toJson(response.getBody()));
        String message = ctx.read("$.message");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualToIgnoringCase("Validation Error : [email: email must not blank]");
    }

    @Test
    public void signIn() {
        // when
        ResponseEntity<?> response = restTemplate
                .postForEntity("/api/auth/signin", new SignInRequest("bagus@gmail.com", "1234567"), ApiResponse.class);
        // then
        ReadContext ctx = JsonPath.parse(toJson(response.getBody()));
        String token = ctx.read("$.payloads.accessToken");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(token).isNotEmpty();
    }

    @Test
    public void signInWithInvalidEmailOrPassword() {
        // when
        ResponseEntity<?> response = restTemplate
                .postForEntity("/api/auth/signin", new SignInRequest("bagus123@gmail.com", "12345677"), ApiResponse.class);
        // then
        ReadContext ctx = JsonPath.parse(toJson(response.getBody()));
        String message = ctx.read("$.message");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(message).isEqualToIgnoringCase("Email/Username or Password not valid!");
    }

    @Before
    public void beforeTest() {
        User user = new User("bagus", "bagus@gmail.com", passwordEncoder.encode("1234567"));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        user = userRepository.save(user);
    }

    private String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
        }
        return "";
    }
}
