package com.demo.admissionportal.dto.response.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoriteResponseDTO implements Serializable {
    private String currentStatus;
}
