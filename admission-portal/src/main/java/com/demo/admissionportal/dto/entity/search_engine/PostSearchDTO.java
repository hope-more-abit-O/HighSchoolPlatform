package com.demo.admissionportal.dto.entity.search_engine;

import com.demo.admissionportal.constants.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@SqlResultSetMapping(
        name = "PostSearchDTOResult",
        classes = @ConstructorResult(
                targetClass = PostSearchDTO.PostSearch.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "createTime", type = Date.class),
                        @ColumnResult(name = "quote", type = String.class),
                        @ColumnResult(name = "thumnail", type = String.class),
                        @ColumnResult(name = "url", type = String.class),
                        @ColumnResult(name = "createBy", type = Integer.class),
                        @ColumnResult(name = "avatar", type = String.class),
                        @ColumnResult(name = "status", type = PostStatus.class)
                }
        )
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostSearchDTO implements Serializable {
    private List<InfoUniversitySearchDTO> university;
    private List<PostSearch> post;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InfoUniversitySearchDTO implements Serializable {
        private Integer id;
        private String code;
        private String name;
        private String avatar;
        private String description;
        private String type;
        private String specificAddress;
        private String phone;
        private String email;
        private String province;
        private String district;
        private String ward;
        private String coverImage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostSearch implements Serializable {
        private Integer id;
        private String title;
        private Date createTime;
        private String quote;
        private String thumnail;
        private String url;
        @JsonIgnore
        private Integer createBy;
        private String fullName;
        private String avatar;
        private PostStatus status;
    }
}
