package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.favorite.FavoriteResponseDTO;
import com.demo.admissionportal.dto.response.favorite.FavoriteUsersListResponseDTO;
import com.demo.admissionportal.dto.response.favorite.TotalCountResponseDTO;
import com.demo.admissionportal.dto.response.favorite.UserFavoriteResponseDTO;
import com.demo.admissionportal.service.FavoriteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Favorite controller.
 */
@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    /**
     * Gets favorite.
     *
     * @param universityID the post id
     * @return the favorite
     */
    @PostMapping("/{universityID}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<FavoriteResponseDTO>> createFavorite(@PathVariable(name = "universityID") Integer universityID) {
        if (universityID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<FavoriteResponseDTO> favorite = favoriteService.createFavorite(universityID);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }


    /**
     * Gets total.
     *
     * @param universityID the university id
     * @return the total
     */
    @GetMapping("/total/{universityID}")
    public ResponseEntity<ResponseData<TotalCountResponseDTO>> getTotal(@PathVariable(name = "universityID") Integer universityID) {
        if (universityID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<TotalCountResponseDTO> resultOfTotal = favoriteService.getTotalFavorite(universityID);
        if (resultOfTotal.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(resultOfTotal);
        } else if (resultOfTotal.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(resultOfTotal);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfTotal);
    }

    /**
     * Gets favorite.
     *
     * @param universityID the university id
     * @return the favorite
     */
    @GetMapping("/{universityID}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<FavoriteResponseDTO>> getFavorite(@PathVariable(name = "universityID") Integer universityID) {
        if (universityID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "universityID null"));
        }
        ResponseData<FavoriteResponseDTO> favorite = favoriteService.getFavorite(universityID);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }

    /**
     * Get favorite by user id response entity.
     *
     * @return the response entity
     */
    @GetMapping("/list")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<List<UserFavoriteResponseDTO>>> getFavoriteByUserId() {
        ResponseData<List<UserFavoriteResponseDTO>> list = favoriteService.getListFavorite();
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(list);
    }

    @GetMapping("/list-users")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAnyAuthority('UNIVERSITY','CONSULTANT')")
    public ResponseEntity<ResponseData<List<FavoriteUsersListResponseDTO>>> getFavoriteListUsers() {
        ResponseData<List<FavoriteUsersListResponseDTO>> list = favoriteService.getFavoriteListUsers();
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(list);
    }
}
