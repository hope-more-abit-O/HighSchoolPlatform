package com.demo.admissionportal.service.oauth2;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .username(email)  // Assuming username is the email
                    .password(passwordEncoder.encode("oauth2user")) // Placeholder, as OAuth2 will handle authentication
                    .role(Role.USER)
                    .createTime(new Date())
                    .status("ACTIVE")
                    .providerId("google")
                    .build();

            user = userRepository.save(user);

            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setFirstname(firstName);
            userInfo.setLastName(lastName);
            userInfo.setGender("NOT_SPECIFIED"); // Default value
            userInfoRepository.save(userInfo);
        } else {
            if (user.getRole() != Role.USER) {
                throw new RuntimeException("Only users with USER role can log in with Google");
            }
        }

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())), oAuth2User.getAttributes(), "email");
    }
}
