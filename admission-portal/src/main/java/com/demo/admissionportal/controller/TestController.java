package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.EducationLevel;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;
    private final UserInfoRepository userInfoRepository;

    public TestController(UserRepository userRepository, UserServiceImpl userServiceImpl, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/")
    public String home(){
        return "Hello home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured";
    }

    @GetMapping("/test/user")
    public Page<User> getUser(Pageable pageable, @RequestParam(required = false) String name){
        return userRepository.findAll(pageable);
    }

    @GetMapping("/minh")
    public String a(){
        UserInfo userInfo = UserInfo.builder()
                .id(9)
                .firstName("a")
                .lastName("b")
                .middleName("c")
                .gender("MALE")
                .specificAddress("abc")
                .build();

        UserInfo userInfo1 = UserInfo.builder()
                .id(10)
                .firstName("a")
                .lastName("b")
                .middleName("c")
                .educationLevel(EducationLevel.HIGH.name())
                .build();

        UserInfo userInfo2 = UserInfo.builder()
                .id(1)
                .firstName("a")
                .lastName("b")
                .middleName("c")
                .specificAddress("abc")
                .build();

        List<UserInfo> userInfos = new ArrayList<>();
//        userInfos.add(userInfo);
        userInfos.add(userInfo1);
        userInfos.add(userInfo2);
        try {
            userInfoRepository.saveAll(userInfos);
        } catch (Exception e) {
            return  e.getCause().getCause().toString();
        }

        return "hi";
    }
}
