package com.sample.user.service;

import com.sample.user.dao.UserDao;
import com.sample.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RedisTemplate redisTemplate;

    public void sendSms(String mobile){
        Random random = new Random();
        int max = 999999;
        int min = 100000;
        int code = random.nextInt(max);
        if(code<min){
            code = code + min;
        }
        System.out.println(mobile + " The verification code received is：" + code);
        redisTemplate.opsForValue().set("smscode_" + mobile, code + "", 60, TimeUnit.SECONDS);
    }

    public User findByMobileAndPassword(String mobile, String password){
        User user = userDao.findByMobile(mobile);
        if(user!=null && encoder.matches(password, user.getPassword())){
            return user;
        }else{
            return null;
        }
    }

    public void add(User user, String code){
        String syscode = (String)redisTemplate.opsForValue().get("smscode_" + user.getMobile());
        if(syscode==null){
            throw new RuntimeException("Please click to get SMS verification code.");
        }
        if(!syscode.equals(code)){
            throw new RuntimeException("Verification code is not crect");
        }
        user.setId(idWorker.nextId() + "");
        user.setFollowcount(0);
        user.setFanscount(0);
        user.setOnline(0L);
        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());

        String newpassword = encoder.encode(user.getPassword());
        user.setPassword(newpassword);

        userDao.save(user);
    }

    public List<User> findAll(){
        return userDao.findAll();
    }

    public Page<User> findSearch(User user, int page, int size){
        Specification<User> specification = createSpecification(user);
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return userDao.findAll(specification, pageRequest);
    }

    public List<User> findSearch(User user){
        Specification<User> specification = createSpecification(user);
        return userDao.findAll(specification);
    }

    public User findById(String id){
        return userDao.findById(id).get();
    }

    public void add(User user){
        user.setId(idWorker.nextId() + "");

        String newpassword = encoder.encode(user.getPassword());
        user.setPassword(newpassword);

        userDao.save(user);
    }

    public void update(User user){
        userDao.save(user);
    }

    public void deleteById(String id){
        userDao.deleteById(id);
    }

    private Specification<User> createSpecification(User user){
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(user.getId()!=null && !"".equals(user.getId())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getId()).as(String.class), "%" + (String)user.getId() + "%"));
                }
                if(user.getMobile()!=null && !"".equals(user.getMobile())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getMobile()).as(String.class), "%" + (String)user.getMobile() + "%"));
                }
                if(user.getPassword()!=null && !"".equals(user.getPassword())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getPassword()).as(String.class), "%" + (String)user.getPassword() + "%"));
                }
                if(user.getNickname()!=null && !"".equals(user.getNickname())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getNickname()).as(String.class), "%" + (String)user.getNickname() + "%"));
                }
                if(user.getSex()!=null && !"".equals(user.getSex())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getSex()).as(String.class), "%" + (String)user.getSex() + "%"));
                }
                if(user.getAvatar()!=null && !"".equals(user.getAvatar())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getAvatar()).as(String.class), "%" + (String)user.getAvatar() + "%"));
                }
                if(user.getEmail()!=null && !"".equals(user.getEmail())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getEmail()).as(String.class), "%" + (String)user.getEmail() + "%"));
                }
                if(user.getInterest()!=null && !"".equals(user.getInterest())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getInterest()).as(String.class), "%" + (String)user.getInterest() + "%"));
                }
                if(user.getPersonality()!=null && !"".equals(user.getPersonality())){
                    predicateList.add(criteriaBuilder.like(root.get(user.getPersonality()).as(String.class), "%" + (String)user.getPersonality() + "%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
