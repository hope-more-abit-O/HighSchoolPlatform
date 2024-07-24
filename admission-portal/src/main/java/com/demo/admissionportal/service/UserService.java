package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.exception.NotAllowedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;


/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Gets user.
     *
     * @param username        the username
     * @param firstName       the first name
     * @param middleName      the middle name
     * @param lastName        the last name
     * @param phone           the phone
     * @param email           the email
     * @param specificAddress the specific address
     * @param educationLevel  the education level
     * @param status          the status
     * @param pageable        the pageable
     * @return the user
     */
    ResponseData<Page<UserResponseDTO>> getUser(String username, String firstName, String middleName, String lastName, String phone, String email,
                                                String specificAddress, String educationLevel, String status, Pageable pageable);

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

    /**
     * Change status response data.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<ChangeStatusUserResponseDTO> changeStatus(Integer id, ChangeStatusUserRequestDTO requestDTO);

    /**
     * Gets info user response dto list.
     *
     * @param ids the ids
     * @return the info user response dto list
     * @throws ResourceNotFoundException the resource not found exception
     */
    List<InfoUserResponseDTO> getInfoUserResponseDTOList(List<Integer> ids) throws ResourceNotFoundException;

    /**
     * Gets full user response dto list.
     *
     * @param ids the ids
     * @return the full user response dto list
     * @throws ResourceNotFoundException the resource not found exception
     */
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

    /**
     * Find by ids list.
     *
     * @param ids the ids
     * @return the list
     */
    List<User> findByIds(List<Integer> ids);

    /**
     * Mapping response full user response dto.
     *
     * @param user the user
     * @return the full user response dto
     * @throws ResourceNotFoundException the resource not found exception
     */
    FullUserResponseDTO mappingResponse(User user) throws ResourceNotFoundException;

    /**
     * Mapping response full user response dto.
     *
     * @param user         the user
     * @param actionerDTOS the actioner dtos
     * @return the full user response dto
     * @throws ResourceNotFoundException the resource not found exception
     */
    FullUserResponseDTO mappingResponse(User user, List<ActionerDTO> actionerDTOS) throws ResourceNotFoundException;

    /**
     * Save user.
     *
     * @param user the user
     * @param name the name
     * @return the user
     * @throws StoreDataFailedException the store data failed exception
     */
    User save(User user, String name) throws StoreDataFailedException;

    /**
     * Update user user.
     *
     * @param id         the id
     * @param username   the username
     * @param email      the email
     * @param updateById the update by id
     * @param name       the name
     * @return the user
     * @throws ResourceNotFoundException the resource not found exception
     * @throws StoreDataFailedException  the store data failed exception
     */
    User updateUser(Integer id, String username, String email, Integer updateById, String name) throws ResourceNotFoundException, StoreDataFailedException;

    /**
     * Change status user.
     *
     * @param id   the id
     * @param note the note
     * @param name the name
     * @return the user
     * @throws StoreDataFailedException  the store data failed exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    User changeStatus(Integer id, String note, String name) throws StoreDataFailedException, ResourceNotFoundException;

    /**
     * Change consultant status user.
     *
     * @param account the account
     * @param note    the note
     * @return the user
     * @throws NotAllowedException       the not allowed exception
     * @throws StoreDataFailedException  the store data failed exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    User changeConsultantStatus(Integer account, String note) throws NotAllowedException, StoreDataFailedException, ResourceNotFoundException;

    /**
     * Find by create by and role page.
     *
     * @param id       the id
     * @param role     the role
     * @param pageable the pageable
     * @return the page
     * @throws ResourceNotFoundException the resource not found exception
     */
    Page<User> findByCreateByAndRole(Integer id, Role role, Pageable pageable) throws ResourceNotFoundException;

    /**
     * Gets actioners.
     *
     * @param ids the ids
     * @return the actioners
     * @throws ResourceNotFoundException the resource not found exception
     */
    List<ActionerDTO> getActioners(List<Integer> ids) throws ResourceNotFoundException;

    /**
     * Find by role list.
     *
     * @param role the role
     * @return the list
     */
    List<User> findByRole(Role role);

    /**
     * Find by role and pageable page.
     *
     * @param role     the role
     * @param pageable the pageable
     * @return the page
     */
    Page<User> findByRoleAndPageable(Role role, Pageable pageable);
}
