package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
