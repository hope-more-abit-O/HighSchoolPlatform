package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface University transaction repository.
 */
@Repository
public interface UniversityTransactionRepository extends JpaRepository<UniversityTransaction, Integer> {

    /**
     * Find by transaction id university transaction.
     *
     * @param transactionId the transaction id
     * @return the university transaction
     */
    @Query(value = "SELECT * " +
            "FROM [university_transaction] ut " +
            "WHERE ut.id = :transactionId ", nativeQuery = true)
    UniversityTransaction findByTransactionId(Integer transactionId);

    /**
     * Find campaign by uni id university transaction.
     *
     * @param id the id
     * @return the university transaction
     */
    @Query(value = "SELECT ut.* " +
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
    UniversityTransaction findCampaignByUniId(Integer id);

    /**
     * Find by order code list.
     *
     * @param orderCode the order code
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM university_transaction ut " +
            "WHERE ut.order_code = :orderCode ", nativeQuery = true)
    List<UniversityTransaction> findByOrderCode(Long orderCode);

    /**
     * Find by university id list.
     *
     * @param universityId the university id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM university_transaction ut " +
            "WHERE ut.university_id = :universityId ", nativeQuery = true)
    List<UniversityTransaction> findByUniversityId(Integer universityId);
}
