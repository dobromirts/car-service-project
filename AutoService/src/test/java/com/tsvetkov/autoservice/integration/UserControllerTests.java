package com.tsvetkov.autoservice.integration;


import com.tsvetkov.autoservice.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init(){
        userRepository.deleteAll();
    }

    @Test
    public void login_ReturnsCorrectView() throws Exception {
        this.mvc.perform(get("/users/login")).andExpect(view().name("login"));
    }

    @Test
    public void register_ReturnsCorrectView() throws Exception {
        this.mvc.perform(get("/users/register")).andExpect(view().name("register"));
    }

    @Test
    public void register_registerUserCorrectly() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "pesho")
                .param("password","1")
                .param("confirmPassword","1")
                .param("email","p@p.bg"));

        Assert.assertEquals(1,this.userRepository.count());
    }

    @Test
    public void register_registerRedirectCorrect() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "pesho")
                .param("password","1")
                .param("confirmPassword","1")
                .param("email","p@p.bg"))
                .andExpect(view().name("redirect:/users/login"));
    }

    @Test
    public void register_registerWithUsernameLowerThan2Symbols_expectedNoUserRegistered() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "p")
                .param("password","1")
                .param("confirmPassword","1")
                .param("email","p@p.bg"));
        Assert.assertEquals(0,this.userRepository.count());
    }
    @Test
    public void register_registerWithUsernameBiggerThan20Symbols_expectedNoUserRegistered() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "pppppppppppppppppppppppppppppp")
                .param("password","1")
                .param("confirmPassword","1")
                .param("email","p@p.bg"));
        Assert.assertEquals(0,this.userRepository.count());
    }

    @Test
    public void register_registerWithNotMatchingPasswords_expectedNoUserRegistered() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "test")
                .param("password","2")
                .param("confirmPassword","1")
                .param("email","p@p.bg"));

        Assert.assertEquals(0,this.userRepository.count());
    }


}
