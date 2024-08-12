package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface University package repository.
 */
public interface UniversityPackageRepository extends JpaRepository<UniversityPackage, Integer> {
    /**
     * Find university package by transaction id university package.
     *
     * @param transactionId the transaction id
     * @return the university package
     */
    @Query(value = "SELECT * " +
            "FROM [university_package] up " +
            "WHERE up.university_transaction_id = :transactionId", nativeQuery = true)
    UniversityPackage findUniversityPackageByTransactionId(Integer transactionId);

    /**
     * Find old university package.
     *
     * @param universityId the university id
     * @param postId       the post id
     * @return the university package
     */
    @Query(value = "SELECT TOP 1 * " +
            "FROM [university_package] up " +
            "WHERE up.university_id = :universityId AND up.post_id = :postId AND up.status = 'ACTIVE' AND up.complete_time >= GETDATE() " +
            "ORDER BY up.complete_time DESC", nativeQuery = true)
    UniversityPackage findOldUniversityPackage(Integer universityId, Integer postId);

    /**
     * Find campaign by uni id university transaction.
     *
     * @param id the id
     * @return the university transaction
     */
    @Query(value = "SELECT up.* " +
            "            FROM post p " +
            "            LEFT JOIN ( " +
            "                SELECT post_id, MAX(complete_time) AS max_complete_time " +
            "                FROM university_package " +
            "                WHERE status = 'ACTIVE' " +
            "                GROUP BY post_id " +
            "            ) max_up " +
            "            ON p.id = max_up.post_id " +
            "            LEFT JOIN university_package up ON up.post_id = p.id AND up.complete_time = max_up.max_complete_time " +
            "            JOIN university_transaction ut ON up.university_transaction_id = ut.id " +
            "            JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "            WHERE p.id = :id", nativeQuery = true)
    UniversityPackage findCampaignByUniId(Integer id);

    /**
     * Find campaign by post id university package.
     *
     * @param id the id
     * @return the university package
     */
    @Query(value = "SELECT up.* " +
            "            FROM post p " +
            "            LEFT JOIN ( " +
            "                SELECT post_id, MAX(complete_time) AS max_complete_time " +
            "                FROM university_package " +
            "                GROUP BY post_id " +
            "            ) max_up " +
            "            ON p.id = max_up.post_id " +
            "            LEFT JOIN university_package up ON up.post_id = p.id AND up.complete_time = max_up.max_complete_time " +
            "            JOIN university_transaction ut ON up.university_transaction_id = ut.id " +
            "            JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "            WHERE p.id = :id", nativeQuery = true)
    UniversityPackage findCampaignByPostId(Integer id);
}
