package com.demo.admissionportal.dto.response.entity;

import com.demo.admissionportal.entity.CreateUniversityTicket;
import com.demo.admissionportal.entity.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUniversityTicketResponseDTO {
    private Integer id;
    private String universityName;
    private String universityCode;
    private List<String> documents;
//        return "https://firebasestorage.googleapis.com/v0/b/highschoolvn-28300.appspot.com/o/" + fileName + "?alt=media"; // Return combined response string

    public static CreateUniversityTicketResponseDTO toDTO(CreateUniversityTicket createUniversityTicket) {
        List<String> documents = new ArrayList<>();
        Arrays.stream(createUniversityTicket.getDocuments().split(",")).forEach(d -> documents.add("https://firebasestorage.googleapis.com/v0/b/highschoolvn-28300.appspot.com/o/" + d + "?alt=media"));
        return new CreateUniversityTicketResponseDTO(createUniversityTicket.getId(), createUniversityTicket.getUniversityName(), createUniversityTicket.getUniversityCode(), documents);
    }
}
