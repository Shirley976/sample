package com.sample.user.service;

import com.sample.user.pojo.Admin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    private Admin admin = null;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findByLoginnameAndPassword() {
        admin = adminService.findByLoginnameAndPassword("Tom_1", "123");
        String password = "$2a$10$bTEilpb9bOj66423O3rWLuZyuZxO4JAzSogo3Jgc.wapC8rgLqr8i";
        Assert.assertEquals(password, admin.getPassword());
    }

    @Test
    public void findAll() {
        List<Admin> adminList = adminService.findAll();
        Assert.assertEquals(3, adminList.size());
        System.out.println(adminList.toString());
    }

    @Test
    public void findSearch() {
        admin = new Admin();
        List<Admin> adminList = adminService.findSearch(admin);
        Iterator iterator = adminList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
        Assert.assertEquals(3, adminList.size());
    }

    @Test
    public void testFindSearch() {
        admin = new Admin();
        Page<Admin> adminPage = adminService.findSearch(admin, 1, 5);
        Assert.assertEquals(3, adminPage.getTotalElements());
        Assert.assertEquals(1, adminPage.getTotalPages());
    }

    @Test
    public void findById() {
        String id = "1363572798752165888";
        admin = adminService.findById(id);
        System.out.println(admin.toString());
    }

    @Test
    public void add() {
        admin = new Admin();
        admin.setLoginname("Tom_3");
        admin.setPassword("123");
        adminService.add(admin);
        Assert.assertEquals(4, adminService.findAll().size());
    }

    @Test
    public void update() {
        admin = new Admin();
        admin.setId("1363572798752165888");
        admin.setLoginname("Tom_1");
        admin.setPassword("$2a$10$bTEilpb9bOj66423O3rWLuZyuZxO4JAzSogo3Jgc.wapC8rgLqr8i");
        admin.setState("1");
        adminService.update(admin);
        System.out.println(admin.toString());
    }

    @Test
    public void deleteById() {
        String id = "1363574952107839488";
        adminService.deleteById(id);
        Assert.assertEquals(3, adminService.findAll().size());
        System.out.println(adminService.findAll().toString());
    }
}