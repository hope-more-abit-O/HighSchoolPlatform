package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UpdateUserResponseDTO;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.dto.response.UserResponseDTO;
import com.demo.admissionportal.exception.NotAllowedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Gets user.
     *
     * @param username the username
     * @param email    the email
     * @param pageable the pageable
     * @return the user
     */
    ResponseData<Page<UserResponseDTO>> getUser(String username, String email, Pageable pageable);

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    ResponseData<UserProfileResponseDTO> getUserById(Integer id);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;


    /**
     * Update user response data.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<UpdateUserResponseDTO> updateUser(Integer id, UpdateUserRequestDTO requestDTO);

    ResponseData<ChangeStatusUserRequestDTO> changeStatus(Integer id, ChangeStatusUserRequestDTO requestDTO);

    List<InfoUserResponseDTO> getInfoUserResponseDTOList(List<Integer> ids) throws ResourceNotFoundException;

    List<FullUserResponseDTO> getFullUserResponseDTOList(List<Integer> ids) throws ResourceNotFoundException;

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     * @throws ResourceNotFoundException the resource not found exception
     */
    //TODO: javadoc
    User findById(Integer id) throws ResourceNotFoundException;
    List<User> findByIds(List<Integer> ids);
    FullUserResponseDTO mappingResponse(User user) throws ResourceNotFoundException;
    User save(User user, String name) throws StoreDataFailedException;
    User updateUser(Integer id, String username, String email, Integer updateById, String name) throws ResourceNotFoundException, StoreDataFailedException;
    User changeStatus(Integer id, String note, String name) throws StoreDataFailedException, ResourceNotFoundException;
    User changeConsultantStatus(Integer account, String note) throws NotAllowedException, StoreDataFailedException, ResourceNotFoundException;

}
