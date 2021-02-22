package com.sample.user.controller;

import com.sample.user.pojo.User;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import util.JwtUtil;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtUtil jwtUtil;

    private MockHttpServletRequest request;

    private String mobile;
    private User user = null;
    private String tokenAdm = "";

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        request = new MockHttpServletRequest();
    }

    @Test
    public void sendsms() throws Exception{
        mobile = "13901238855";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/user/sendsms/{mobile}", mobile);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void register() throws Exception{
        String requestJson = "{\"mobile\":\"13901238855\",\"password\":\"123\"}";

        String code = "714896";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/user/register/{code}", code)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void login() throws Exception{
        String user = "{\"mobile\":\"13901238899\",\"password\":\"123\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/user/login").content(user).contentType(MediaType.APPLICATION_JSON);
        String result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findAll() throws Exception{
        RequestBuilder requestBuilder = null;
        requestBuilder = get("/user");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findById() throws Exception{
        String id = "1363566488614735872";

        RequestBuilder requestBuilder = null;
        requestBuilder = get("/user/{id}", id);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findSearch() throws Exception{
        String user = "{\"mobile\":\"\", \"nickname\":\"\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/user/search").content(user).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testFindSearch() throws Exception{
        String user = "{\"mobile\":\"\", \"nickname\":\"\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/user/search/{page}/{size}", 1, 5).content(user).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void add() throws Exception{
        String user = "{\"mobile\":\"13901238822\",\"password\":\"123\"}";

        RequestBuilder requestBuilder = null;
        requestBuilder = post("/user").content(user).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void update() throws Exception{
        String user = "{\"mobile\":\"13901238855\",\"password\":\"$2a$10$dw7KO5TmXFK5UnNX75vnxO3z86jCatxT9Q8YSfPK6QaMObtTlN2a2\",\"nickname\":\"hello\"}";
        String id = "1363576693649969152";

        RequestBuilder requestBuilder = null;
        requestBuilder = put("/user/{id}", id).content(user).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void delete() throws Exception{
        RequestBuilder requestBuilder = null;

        tokenAdm = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUb21fMSIsImlhdCI6MTYxMzk1MTYxNiwicm9sZXMiOiJhZG1pbiIsImV4cCI6MTYxMzk1MTk3Nn0.wQ_Qb3eywZDdwU1lqqP6ZWZBQD4_PSb5AV4Ck4Sn_aY";
        Claims claims = jwtUtil.parseJWT(tokenAdm);

        requestBuilder = MockMvcRequestBuilders.delete("/user/{id}", "1363567017323532288").requestAttr("admin_claims", claims);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
}