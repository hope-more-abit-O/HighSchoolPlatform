package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;

/**
 * Represents detailed information about a consultant.
 *
 * <p>This entity stores comprehensive information related to a consultant,
 * including their personal details, contact information, and address.
 *
 * <p> Example Usage:
 * <pre>
 * {@code
 * // Create Province, District, and Ward objects first (or fetch them from a data source).
 * Province someProvince = new Province(1, "Some Province");
 * District someDistrict = new District(1, "Some District");
 * Ward someWard = new Ward(1, "Some Ward");
 *
 * // Create a ConsultantInfo instance
 * ConsultantInfo consultantInfo = new ConsultantInfo(
 *     1,                              // Consultant ID
 *     123,                            // University ID
 *     "John",                         // First name
 *     "Doe",                          // Middle Name
 *     "Consultant",                   // Last name
 *     "1234567890",                   // Phone Number
 *     "123 Main Street",              // Specific address
 *     Gender.MALE,                    // Gender
 *     someProvince,                   // Province object
 *     someDistrict,                   // District object
 *     someWard                        // Ward object
 * );
 *
 * // ... Use a repository to persist the consultantInfo object
 * }
 * </pre>
 *
 * @see lombok.Getter - Generates getter methods for all fields.
 * @see lombok.Setter - Generates setter methods for all fields.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "consultant_info")
public class ConsultantInfo {
    @Id
    @Column(name = "consultant_id", nullable = false)
    private Integer id;

    @NotNull
    @Column
    private Integer universityId;

    @NotNull
    @Nationalized
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstname;

    @NotNull
    @Nationalized
    @Column(name = "middle_name", nullable = false, length = 30)
    private String middleName;

    @NotNull
    @Nationalized
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "phone", length = 11)
    private String phone;

    @NotNull
    @Nationalized
    @Column(name = "specific_address", nullable = false)
    private String specificAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_id")
    private Ward ward;

//    @Column(name = "province_id")
//    private Integer province;
//
//    @Column(name = "district_id")
//    private Integer district;
//
//    @Column(name = "ward_id")
//    private Integer ward;

    public ConsultantInfo(Integer id, Integer universityId, String firstname, String middleName, String lastName, String phone, String specificAddress, Gender gender, Province province, District district, Ward ward) {
        this.id = id;
        this.universityId = universityId;
        this.firstname = firstname;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.specificAddress = specificAddress;
        this.gender = gender;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }


//    public ConsultantInfo(Integer id, Integer universityId, String firstname, String middleName, String lastName, String phone, String specificAddress, Gender gender, Province province, District district, Ward ward) {
//        this.id = id;
//        this.universityId = universityId;
//        this.firstname = firstname;
//        this.middleName = middleName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.specificAddress = specificAddress;
//        this.gender = gender;
//        this.province = province.getId();
//        this.district = district.getId();
//        this.ward = ward.getId();
//    }

}