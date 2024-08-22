package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsUniversityResponse implements Serializable {
    private Integer totalLike;
    private Integer totalFavorite;
    private Integer totalComment;
    private Integer totalPost;
    private List<StatisticsTransactionDetailResponse> transactionDetail;
}
