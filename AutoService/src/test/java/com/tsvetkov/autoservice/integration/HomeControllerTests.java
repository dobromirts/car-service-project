package com.tsvetkov.autoservice.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTests {

    @Autowired
    private MockMvc mvc;



    @Test
    public void index_ReturnsCorrectView() throws Exception {
        this.mvc.perform(get("/")).andExpect(view().name("index"));
    }
    @Test
    public void about_ReturnsCorrectView() throws Exception {
        this.mvc.perform(get("/about")).andExpect(view().name("about"));
    }@Test
    public void contacts_ReturnsCorrectView() throws Exception {
        this.mvc.perform(get("/contacts")).andExpect(view().name("contacts"));
    }
    @Test
    @WithMockUser
    public void home_ReturnsCorrectView() throws Exception {
        this.mvc.perform(get("/home")).andExpect(view().name("home"));
    }
}
