package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.PaginationDTO;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * The interface Staff service.
 */
@Service
public interface StaffService {
    /**
     * Register staff response data.
     *
     * @param staff the staff
     * @return the response data
     */
    ResponseData<?> registerStaff(RegisterStaffRequestDTO staff);
    Staff getStaffById(int id);
    PaginationDTO<Staff> getAllStaffs(int page, int size, Map<String, String> filters, List<String> order);
    ResponseData<Staff> getStaffById(Integer id);
    ResponseData<Staff> updateStaff(Integer id, UpdateStaffRequestDTO request);
    ResponseData<Void> deleteStaff(Integer id);
}
