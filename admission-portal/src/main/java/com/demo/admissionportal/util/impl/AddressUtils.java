package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.Ward;
import org.springframework.stereotype.Component;

@Component
public class AddressUtils {
    public static String getFullAddress (String specificAddress, Ward ward, District district, Province province){
        return specificAddress + " " + ward.getName() + " " + district.getName() + ", " + province.getName();
    }
}
