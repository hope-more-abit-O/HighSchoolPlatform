package com.demo.admissionportal.entity;

import com.demo.admissionportal.dto.request.university.CreateUniversityTicketRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "create_university_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUniversityTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Tên trường đại học không được để trống!")
    @Column(name = "university_name")
    private String universityName;

    @NotNull(message = "Mã trường đại học không được để trống")
    @Column(name = "university_code")
    private String universityCode;

    @NotNull(message = "Tài liệu đính kèm không được để trống")
    @Column(name = "documents")
    private String documents;

    public CreateUniversityTicket(String universityName, String universityCode, String documents) {
        this.universityName = universityName;
        this.universityCode = universityCode;
        this.documents = documents;
    }

    public CreateUniversityTicket(CreateUniversityTicketRequestDTO request){
        this.universityName = request.getUniversityName();
        this.universityCode = request.getUniversityCode();
        this.documents = request.getDocuments();
    }
}
