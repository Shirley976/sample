package com.sample.user.service;

import com.sample.user.pojo.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    private String key;
    private String mobile;
    private String password;
    private User user = null;

    @Before
    public void setUp() throws Exception {
        key = "smscode_";
        mobile = "13901238833";
        password = "$2a$10$fnxEmDtB7mmAp9GX14hZXeNu0T.FEG5qLwO9gm4KNAUpXCplNCGxi";
    }

    @Test
    public void sendSms() {
        userService.sendSms(mobile);
        String code = redisTemplate.opsForValue().get(key + mobile).toString();
        System.out.println("code: " + code);
    }

    @Test
    public void findByMobileAndPassword() {
        user = userService.findByMobileAndPassword(mobile, "123");
        if(user!=null){
            System.out.println("user.getPassword() : " + user.getPassword());
        }
        Assert.assertEquals(password, user.getPassword());
    }

    @Test
    public void add() {
        user = new User();
        user.setMobile(mobile);
        user.setPassword("123");
        String code = "213573";
        userService.add(user, code);
        Assert.assertEquals(8, userService.findAll().size());
    }

    @Test
    public void findAll() {
        List<User> userList = userService.findAll();
        Assert.assertEquals(8, userList.size());
    }

    @Test
    public void findSearch() {
        user = new User();
        List<User> userList = userService.findSearch(user);
        Iterator iterator = userList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        Assert.assertEquals(8, userList.size());
    }

    @Test
    public void testFindSearch() {
        user = new User();
        Page<User> userPage = userService.findSearch(user, 1, 5);
        Assert.assertEquals(8, userPage.getTotalElements());
        Assert.assertEquals(2, userPage.getTotalPages());
    }

    @Test
    public void findById() {
        String id = "1363566805003669504";
        user = userService.findById(id);
        System.out.println(user.toString());
    }

    @Test
    public void testAdd() {
        user = new User();
        user.setMobile("13901238844");
        user.setPassword("123");
        userService.add(user);
        Assert.assertEquals(8, userService.findAll().size());
    }

    @Test
    public void update() {
        user = new User();
        user.setId("1363566805003669504");
        user.setMobile("13901238866");
        user.setLastdate(new Date());
        userService.update(user);
        System.out.println(user.toString());
    }

    @Test
    public void deleteById() {
        userService.deleteById("1363571340505911296");
        Assert.assertEquals(8, userService.findAll().size());
    }
}