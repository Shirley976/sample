package com.sample.user.service;

import com.sample.user.dao.AdminDao;
import com.sample.user.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Admin findByLoginnameAndPassword(String loginname, String password){
        Admin admin = adminDao.findByLoginname(loginname);
        if(admin!=null && encoder.matches(password, admin.getPassword())){
            return admin;
        }else{
            return null;
        }
    }

    public List<Admin> findAll(){
        return adminDao.findAll();
    }

    public Page<Admin> findSearch(Admin admin, int page, int size){
        Specification<Admin> specification = createSpecification(admin);
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return adminDao.findAll(specification, pageRequest);
    }

    public List<Admin> findSearch(Admin admin){
        Specification<Admin> specification = createSpecification(admin);
        return adminDao.findAll(specification);
    }

    public Admin findById(String id){
        return adminDao.findById(id).get();
    }

    public void add(Admin admin){
        admin.setId(idWorker.nextId() + "");

        String newpassword = encoder.encode(admin.getPassword());
        admin.setPassword(newpassword);

        adminDao.save(admin);
    }

    public void update(Admin admin){
        adminDao.save(admin);
    }

    public void deleteById(String id){
        adminDao.deleteById(id);
    }

    private Specification<Admin> createSpecification(Admin admin){
        return new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(admin.getId()!=null && !"".equals(admin.getId())){
                    predicateList.add(criteriaBuilder.like(root.get(admin.getId()).as(String.class), "%" + (String)admin.getId() + "%"));
                }
                if(admin.getLoginname()!=null && !"".equals(admin.getLoginname())){
                    predicateList.add(criteriaBuilder.like(root.get(admin.getLoginname()).as(String.class), "%" + (String)admin.getLoginname() + "%"));
                }
                if(admin.getPassword()!=null && !"".equals(admin.getPassword())){
                    predicateList.add(criteriaBuilder.like(root.get(admin.getPassword()).as(String.class), "%" + (String)admin.getPassword() + "%"));
                }
                if(admin.getState()!=null && !"".equals(admin.getState())){
                    predicateList.add(criteriaBuilder.like(root.get(admin.getState()).as(String.class), "%" + (String)admin.getState() + "%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
