package com.sample.user.controller;

import com.sample.user.pojo.Admin;
import com.sample.user.service.AdminService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        Admin admin1 = adminService.findByLoginnameAndPassword(admin.getLoginname(), admin.getPassword());
        if(admin1!=null){
            String token = jwtUtil.createJWT(admin.getId(), admin.getLoginname(), "admin");
            Map map = new HashMap();
            map.put("token", token);
            map.put("name", admin.getLoginname());
            return new Result(true, StatusCode.OK, "Login successfully", map);
        }else{
            return new Result(false, StatusCode.LOGINERROR, "Wrong user name or password");
        }
    }

    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK, "Search successful", adminService.findAll());
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        return new Result(true, StatusCode.OK, "Search successful", adminService.findById(id));
    }

    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody Admin admin, @PathVariable int page, @PathVariable int size){
        Page<Admin> pageList = adminService.findSearch(admin, page, size);
        return new Result(true, StatusCode.OK, "Search successful", new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()));
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Admin admin){
        return new Result(true, StatusCode.OK, "Search successful", adminService.findSearch(admin));
    }

    @PostMapping
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return new Result(true, StatusCode.OK, "Added successfully");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Admin admin, @PathVariable String id){
        admin.setId(id);
        adminService.update(admin);
        return new Result(true, StatusCode.OK, "Successfully modified");
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable String id){
        adminService.deleteById(id);
        return new Result(true, StatusCode.OK, "Successfully deleted");
    }
}
