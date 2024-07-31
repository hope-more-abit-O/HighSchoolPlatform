package com.demo.admissionportal.dto.entity.search_engine;

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


@SqlResultSetMapping(
        name = "PostSearchDTOResult",
        classes = @ConstructorResult(
                targetClass = PostSearchDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "createTime", type = Date.class),
                        @ColumnResult(name = "quote", type = String.class),
                        @ColumnResult(name = "thumnail", type = String.class),
                        @ColumnResult(name = "url", type = String.class),
                        @ColumnResult(name = "createBy", type = String.class),
                        @ColumnResult(name = "avatar", type = String.class),
                        @ColumnResult(name = "universityName", type = String.class)
                }
        )
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostSearchDTO implements Serializable {
    private Integer id;
    private String title;
    private Date createTime;
    private String quote;
    private String thumnail;
    private String url;
    private String createBy;
    private String avatar;
    private String universityName;
}
