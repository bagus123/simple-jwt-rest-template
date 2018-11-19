package com.tbs.app.controller;

import com.tbs.app.payload.request.DepositRequest;
import com.tbs.app.payload.request.SignInRequest;
import com.tbs.app.payload.request.SignUpRequest;
import com.tbs.app.payload.request.WithdrawalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.springframework.test.annotation.DirtiesContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void depositWithInvalidToken() throws Exception {
        DepositRequest body = new DepositRequest(100, "terminal_id|merchant_id|etc");
        mockMvc.perform(post("/api/trx/deposit")
                .header("Authorization", "Bearer 123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String obtainAccessToken() throws Exception {
        SignUpRequest body = new SignUpRequest("bagus@gmail.com", "1234567");
        mockMvc.perform(post("/api/auth/signup")
                .content(toJson(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        SignInRequest bodySignIn = new SignInRequest("bagus@gmail.com", "1234567");
        ResultActions result = mockMvc.perform(post("/api/auth/signin")
                .content(toJson(bodySignIn))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        String json = result.andReturn().getResponse().getContentAsString();

        ReadContext ctx = JsonPath.parse(json);
        return ctx.read("$.payloads.accessToken");
    }

    @Test
    public void deposit() throws Exception {
        String token = obtainAccessToken();
        DepositRequest body = new DepositRequest(100, "terminal_id|merchant_id|etc");
        mockMvc.perform(post("/api/trx/deposit")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //cek balance
        ResultActions result = mockMvc.perform(get("/api/user/balance")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        String json = result.andReturn().getResponse().getContentAsString();
        ReadContext ctx = JsonPath.parse(json);
        double balance = ctx.read("$.payloads.balance");
        assertThat(balance).isEqualTo(200);
    }

    @Test
    public void withdrawal() throws Exception {
        String token = obtainAccessToken();
        WithdrawalRequest body = new WithdrawalRequest(10, "123456", "terminal_id|merchant_id|etc");
        mockMvc.perform(post("/api/trx/withdrawal")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //cek balance
        ResultActions result = mockMvc.perform(get("/api/user/balance")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        String json = result.andReturn().getResponse().getContentAsString();
        ReadContext ctx = JsonPath.parse(json);
        double balance = ctx.read("$.payloads.balance");
        assertThat(balance).isEqualTo(90);
    }

    @Test
    public void withdrawalWithOverBalance() throws Exception {
        String token = obtainAccessToken();
        WithdrawalRequest body = new WithdrawalRequest(110, "123456", "terminal_id|merchant_id|etc");
        mockMvc.perform(post("/api/trx/withdrawal")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Before
    public void beforeTest() throws Exception {

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
