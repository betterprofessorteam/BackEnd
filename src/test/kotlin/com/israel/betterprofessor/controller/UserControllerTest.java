package com.israel.betterprofessor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.israel.betterprofessor.model.Mentor;
import com.israel.betterprofessor.model.Student;
import com.israel.betterprofessor.model.User;
import com.israel.betterprofessor.service.UserService;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ArrayList<User> users;

    @Before
    public void setup() {
        users = new ArrayList<>();
        User mentorUser0 = new User("mentor0", "password", new ArrayList<>());
        mentorUser0.setUserId(1L);
        mentorUser0.setMentorData(new Mentor("mentor_fname", "mentor_lname"));
        users.add(mentorUser0);

        User studentUser0 = new User("student0", "password", new ArrayList<>());
        studentUser0.setUserId(1L);
        studentUser0.setStudentData(new Student("student_fname", "student_lname"));
        users.add(studentUser0);
    }

    @Test
    public void addUser() throws Exception {
        String url = "/users";

        User returnUser = new User();
        returnUser.setUserId(7L);
        returnUser.setUsername("usernameregister123");
        Mentor mentorData = new Mentor();
        mentorData.setFirstName("mentor first name register");
        mentorData.setLastName("mentor last name register");
        returnUser.setMentorData(mentorData);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(returnUser);

        RequestBuilder rb = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username123\"}");

        MvcResult result = mockMvc.perform(rb).andReturn();

        String responseStr = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        User responseUser = mapper.readValue(responseStr, User.class);

        assertEquals(7L, responseUser.getUserId());
        assertEquals("usernameregister123", responseUser.getUsername());
        assertEquals("mentor first name register", responseUser.getMentorData().getFirstName());
        assertEquals("mentor last name register", responseUser.getMentorData().getLastName());

    }

    @Test
    public void getUser() throws Exception {
        String url = "/user";

        User returnUser = new User();
        returnUser.setUserId(7L);
        returnUser.setUsername("username123");
        Mentor mentorData = new Mentor();
        mentorData.setFirstName("mentor first name");
        mentorData.setLastName("mentor last name");
        returnUser.setMentorData(mentorData);

        Mockito.when(userService.findCurrentUser()).thenReturn(returnUser);

        RequestBuilder rb = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username123\"}");

        MvcResult result = mockMvc.perform(rb).andReturn();

        String responseStr = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        User responseUser = mapper.readValue(responseStr, User.class);

        assertEquals(7L, responseUser.getUserId());
        assertEquals("username123", responseUser.getUsername());
        assertEquals("mentor first name", responseUser.getMentorData().getFirstName());
        assertEquals("mentor last name", responseUser.getMentorData().getLastName());
    }

}