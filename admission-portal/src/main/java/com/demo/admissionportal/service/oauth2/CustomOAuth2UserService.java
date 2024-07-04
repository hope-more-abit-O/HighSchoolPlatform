package com.demo.admissionportal.service.oauth2;

import com.demo.admissionportal.constants.EducationLevel;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println("Google User Info: " + attributes);

        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = registerNewUser(attributes);
        }

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())), oAuth2User.getAttributes(), "email");
    }

    @Transactional
    public User registerNewUser(Map<String, Object> attributes) {
        User user = new User();
        user.setEmail((String) attributes.get("email"));
        user.setUsername((String) attributes.get("email"));
        user.setPassword(passwordEncoder.encode("oauth2user"));
        user.setRole(Role.USER);
        user.setCreateTime(new Date());
        user.setStatus("ACTIVE");
        user.setProviderId("google");
        user.setAvatar((String) attributes.get("picture"));

        User savedUser = userRepository.save(user);

        // Map attributes to UserInfo
        UserInfo userInfo = new UserInfo();
        modelMapper.map(attributes, userInfo);

        // Set additional fields manually if necessary
        userInfo.setId(savedUser.getId());
        userInfo.setFirstname(StringUtils.hasText((String) attributes.get("given_name")) ? (String) attributes.get("given_name") : "Unknown");
        userInfo.setLastName(StringUtils.hasText((String) attributes.get("family_name")) ? (String) attributes.get("family_name") : "Unknown");
        userInfo.setGender(resolveGender((String) attributes.get("gender")).name());
        userInfo.setMiddleName(StringUtils.hasText(userInfo.getMiddleName()) ? userInfo.getMiddleName() : "");
        userInfo.setSpecificAddress(StringUtils.hasText(userInfo.getSpecificAddress()) ? userInfo.getSpecificAddress() : "");
        userInfo.setEducationLevel(StringUtils.hasText(userInfo.getEducationLevel()) ? userInfo.getEducationLevel() : EducationLevel.OTHER.name());
        userInfo.setBirthday(attributes.get("birthdate") != null ? java.sql.Date.valueOf((String) attributes.get("birthdate")) : null);
        userInfo.setPhone(StringUtils.hasText(userInfo.getPhone()) ? userInfo.getPhone() : "");
        userInfo.setDistrict(userInfo.getDistrict() != null ? userInfo.getDistrict() : 0);
        userInfo.setProvince(userInfo.getProvince() != null ? userInfo.getProvince() : 0);
        userInfo.setWard(userInfo.getWard() != null ? userInfo.getWard() : 0);

        userInfoRepository.save(userInfo);

        return savedUser;
    }

    private Gender resolveGender(String gender) {
        if (StringUtils.hasText(gender)) {
            switch (gender.toLowerCase()) {
                case "male":
                    return Gender.MALE;
                case "female":
                    return Gender.FEMALE;
                default:
                    return Gender.OTHER;
            }
        }
        return Gender.OTHER;
    }
}
