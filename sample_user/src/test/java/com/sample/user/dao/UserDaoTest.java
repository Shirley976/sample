package com.sample.user.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    private String userId;

    @Before
    public void setUserDao(){
        userId = "1118695432244039680";
    }

    @Test
    public void findByMobile() {
        Assert.assertEquals(userId, userDao.findByMobile("13901238899").getId());
    }
}