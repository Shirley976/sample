package com.sample.user.controller;

import com.sample.user.pojo.User;
import com.sample.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/sendsms/{mobile}")
    public Result sendsms(@PathVariable String mobile){
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "Sent successfully");
    }

    @PostMapping("/register/{code}")
    public Result register(@RequestBody User user, @PathVariable String code){
        userService.add(user, code);
        return new Result(true, StatusCode.OK, "Registration success");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User user1 = userService.findByMobileAndPassword(user.getMobile(), user.getPassword());
        if(user1!=null){
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            Map map = new HashMap();
            map.put("token", token);
            map.put("name", user1.getNickname());
            map.put("avatar", user.getAvatar());
            return new Result(true, StatusCode.OK, "Login successfully", map);
        }else{
            return new Result(false, StatusCode.LOGINERROR, "Wrong user name or password");
        }
    }

    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK, "Search successful", userService.findAll());
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        return new Result(true, StatusCode.OK, "Search successful", userService.findById(id));
    }

    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody User user, @PathVariable int page, @PathVariable int size){
        Page<User> userPage = userService.findSearch(user, page, size);
        return new Result(true, StatusCode.OK, "Search successful", new PageResult<User>(userPage.getTotalElements(), userPage.getContent()));
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody User user){
        return new Result(true, StatusCode.OK, "Search successful", userService.findSearch(user));
    }

    @PostMapping
    public Result add(@RequestBody User user){
        userService.add(user);
        return new Result(true, StatusCode.OK, "Added successfully");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody User user, @PathVariable String id){
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "Successfully modified");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id){
        Claims claims = (Claims)request.getAttribute("admin_claims");
        if(claims==null){
            return new Result(true, StatusCode.ACCESSERROR, "No access");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "Successfully deleted");
    }
}
