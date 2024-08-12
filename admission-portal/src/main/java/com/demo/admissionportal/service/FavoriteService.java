package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.favorite.FavoriteResponseDTO;
import com.demo.admissionportal.dto.response.favorite.TotalCountResponseDTO;
import com.demo.admissionportal.dto.response.favorite.UserFavoriteResponseDTO;

import java.util.List;

/**
 * The interface Favorite service.
 */
public interface FavoriteService {
    /**
     * Create favorite user favorite.
     *
     * @param universityID the university id
     * @return the user favorite
     */
    ResponseData<FavoriteResponseDTO> createFavorite(Integer universityID);

    /**
     * Gets favorite.
     *
     * @param universityID the university id
     * @return the favorite
     */
    ResponseData<FavoriteResponseDTO> getFavorite(Integer universityID);

    /**
     * Gets total favorite.
     *
     * @param universityID the university id
     * @return the total favorite
     */
    ResponseData<TotalCountResponseDTO> getTotalFavorite(Integer universityID);

    /**
     * Gets list favorite.
     *
     * @return the list favorite
     */
    ResponseData<List<UserFavoriteResponseDTO>> getListFavorite();
}
