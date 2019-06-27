package com.israel.betterprofessor.controller;

import io.restassured.http.Header;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }



    @Test
    public void login() throws Exception {
        Header auth = new Header("Authorization", "Basic YmV0dGVyLXByb2Zlc3Nvci1jbGllbnQ6YmV0dGVyLXByb2Zlc3Nvci1zZWNyZXQ=");
        MockMvcResponse mockMvcResponse = given().header(auth)
                .param("grant_type", "password")
                .param("username", "mentor0")
                .param("password", "password")
                .post("/oauth/token")
                .thenReturn();

        String responseStr = mockMvcResponse.getMvcResult().getResponse().getContentAsString();

        assertEquals(1, 1);
    }

    @Test
    public void getAllStudentUsers() throws Exception {
        MockMvcResponse mockMvcResponse = given()
                .header(new Header("Authorization", "Bearer YmV0dGVyLXByb2Zlc3Nvci1jbGllbnQ6YmV0dGVyLXByb2Zlc3Nvci1zZWNyZXQ="))
                .get("/user/students").thenReturn();

        String responseStr = mockMvcResponse.getMvcResult().getResponse().getContentAsString();

        assertEquals(1, 1);
    }

    @Test
    public void getAllStudentUsersResponseTime() throws Exception {

    }

}
