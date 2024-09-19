package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.exception.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareMajorsFromUniversitiesRequest {
    private Integer universityId;
    private Integer majorId;

    public CompareMajorsFromUniversitiesRequest(String str){
        // Split the string by "-"
        String[] strings = str.split("-");

        // Check if the split array has exactly 2 elements
        if (strings == null || strings.length != 2) {
            throw new BadRequestException("Cặp id của trường mà ngành sai format");
        }

        try {
            // Parse and assign the universityId and majorId
            this.universityId = Integer.parseInt(strings[0].trim());
            this.majorId = Integer.parseInt(strings[1].trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Cặp id của trường mà ngành phải là số nguyên");
        }
    }
}
