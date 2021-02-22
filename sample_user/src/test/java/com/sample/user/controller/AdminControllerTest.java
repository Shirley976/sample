package com.sample.user.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void login() throws Exception{
        String admin = "{\"loginname\":\"Tom_1\",\"password\":\"123\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/admin/login").content(admin).contentType(MediaType.APPLICATION_JSON);
        String result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findAll() throws Exception{
        RequestBuilder requestBuilder = null;
        requestBuilder = get("/admin");
        String result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findById() throws Exception{
        String id = "1363572798752165888";
        RequestBuilder requestBuilder = null;
        requestBuilder = get("/admin/{id}", id);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findSearch() throws Exception{
        String admin = "{\"loginname\":\"\", \"state\":\"\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/admin/search").content(admin).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testFindSearch() throws Exception{
        String admin = "{\"loginname\":\"\", \"state\":\"\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/admin/search/{page}/{size}", 1, 5).content(admin).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void add() throws Exception{
        String admin = "{\"loginname\":\"Tom_4\",\"password\":\"123\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/admin").content(admin).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void update() throws Exception{
        String admin = "{\"loginname\":\"Tom_4\",\"password\":\"$2a$10$mSDOuDy941QqeCHjcaQyK.JJjOlvSzQEp8smfxyexnDuDYLgbZGzO\", \"state\":\"1\"}";
        String id = "1363582278554488832";

        RequestBuilder requestBuilder = null;
        requestBuilder = put("/admin/{id}", id).content(admin).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteById() throws Exception{
        String id = "1363582278554488832";

        RequestBuilder requestBuilder = null;
        requestBuilder = delete("/admin/{id}", id);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
}