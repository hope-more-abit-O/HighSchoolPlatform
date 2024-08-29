package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.StatisticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * The type Statistics controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@SecurityRequirement(name = "BearerAuth")
public class StatisticsController {
    private final StatisticsService statisticsService;
    /**
     * Gets statistics.
     *
     * @return the statistics
     */
    @GetMapping
    public ResponseEntity<ResponseData<?>> getStatistics() {
        ResponseData<?> responseData = statisticsService.getStatistics();
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }
        @GetMapping("/v2")
        public ResponseEntity<ResponseData<?>> getStatisticsV2(
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay,
                @RequestParam(required = false) String type,
                @RequestParam(required = false) String role,
                @RequestParam(required = false) String status,
                @RequestParam(required = false) String period

        ) {
            ResponseData<?> responseData = statisticsService.getStatisticsV2(startDay, endDay, type, role, status, period);
            if (responseData.getStatus() == ResponseCode.C200.getCode()) {
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
}
