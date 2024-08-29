package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
     * @param orderCode    the order code
     * @param pageable     the pageable
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM university_transaction ut " +
            "WHERE ut.university_id = :universityId " +
            "AND (:orderCode IS NULL OR ut.order_code  LIKE N'%' + :orderCode + '%')", nativeQuery = true)
    Page<UniversityTransaction> findByUniversityId(Integer universityId, @Param("orderCode") String orderCode, Pageable pageable);

    /**
     * Calculator total transaction integer.
     *
     * @return the integer
     */
    @Query(value = "SELECT SUM(ap.price) " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID'", nativeQuery = true)
    Optional<Integer> calculatorTotalTransaction();

    /**
     * Calculator current transaction integer.
     *
     * @return the integer
     */
    @Query(value = "SELECT SUM(ap.price) " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID' AND CONVERT(DATE, ut.create_time) = CONVERT(DATE, GETDATE())", nativeQuery = true)
    Optional<Integer> calculatorCurrentTransaction();

    /**
     * Find university transaction by university id list.
     *
     * @param universityId the university id
     * @return the list
     */
    @Query(value = "SELECT ut.* " +
            "FROM university_transaction ut " +
            "JOIN university_info ci ON ut.university_id = ci.university_id " +
            "WHERE ci.university_id = :universityId AND ut.status = 'PAID'", nativeQuery = true)
    List<UniversityTransaction> findUniversityTransactionByUniversityId(Integer universityId);

    /**
     * Find total revenue by period list.
     *
     * @param startDay the start day
     * @param endDay   the end day
     * @return the list
     */
    @Query(value = "SELECT FORMAT(ut.create_time, 'yyyy-MM-dd') AS date, SUM(ap.price) AS revenue " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID' AND ut.create_time BETWEEN :startDay AND :endDay " +
            "GROUP BY FORMAT(ut.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(ut.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalRevenueByPeriod(@Param("startDay") Date startDay, @Param("endDay") Date endDay);

    /**
     * Find total interactions by period list.
     *
     * @param startDay the start day
     * @param endDay   the end day
     * @return the list
     */
    @Query(value = "SELECT FORMAT(ul.create_time, 'yyyy-MM-dd') AS month, " +
            "SUM(CASE WHEN ul.status = 'LIKE' THEN 1 ELSE 0 END + " +
            "CASE WHEN ul.status = 'UNLIKE' THEN 1 ELSE 0 END + " +
            "CASE WHEN uf.status = 'FOLLOW' THEN 1 ELSE 0 END + " +
            "CASE WHEN uf.status = 'UNFOLLOW' THEN 1 ELSE 0 END) AS total_interactions " +
            "FROM user_like ul " +
            "LEFT JOIN user_favorite uf ON ul.create_time = uf.create_time " +
            "WHERE ul.create_time BETWEEN :startDay AND :endDay " +
            "GROUP BY FORMAT(ul.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(ul.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalInteractionsByPeriod(@Param("startDay") Date startDay, @Param("endDay") Date endDay);

    /**
     * Find total accounts by period list.
     *
     * @param startDay the start day
     * @param endDay   the end day
     * @param role     the role
     * @param status   the status
     * @return the list
     */
    @Query(value = "SELECT FORMAT(u.create_time, 'yyyy-MM-dd') AS date, COUNT(u.id) AS totalAccount " +
            "FROM [user] u " +
            "WHERE u.create_time BETWEEN :startDay AND :endDay " +
            "AND (:role IS NULL OR u.role = :role) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "GROUP BY FORMAT(u.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(u.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalAccountsByPeriod(@Param("startDay") Date startDay,
                                             @Param("endDay") Date endDay,
                                             @Param("role") String role,
                                             @Param("status") String status);

    /**
     * Find total posts by period list.
     *
     * @param startDay the start day
     * @param endDay   the end day
     * @param status   the status
     * @return the list
     */
    @Query(value = "SELECT FORMAT(p.create_time, 'yyyy-MM-dd') AS date, COUNT(p.id) AS totalPosts " +
            "FROM post p " +
            "WHERE p.create_time BETWEEN :startDay AND :endDay " +
            "AND (:status IS NULL OR p.status = :status) " +
            "GROUP BY FORMAT(p.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(p.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalPostsByPeriod(@Param("startDay") Date startDay,
                                          @Param("endDay") Date endDay,
                                          @Param("status") String status);

    /**
     * Find total posts by period and university list.
     *
     * @param startDay     the start day
     * @param endDay       the end day
     * @param universityId the university id
     * @param status       the status
     * @return the list
     */
    @Query(value = "SELECT FORMAT(p.create_time, 'yyyy-MM-dd') AS date, COUNT(p.id) AS totalPosts " +
            "FROM post p " +
            "JOIN consultant_info c ON p.create_by = c.consultant_id " +
            "WHERE p.create_time BETWEEN :startDay AND :endDay " +
            "AND c.university_id = :universityId " +
            "AND (:status IS NULL OR p.status = :status) " +
            "GROUP BY FORMAT(p.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(p.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalPostsByPeriodAndUniversity(@Param("startDay") Date startDay,
                                                       @Param("endDay") Date endDay,
                                                       @Param("universityId") Integer universityId,
                                                       @Param("status") String status);

    /**
     * Find total likes by period and university list.
     *
     * @param startDay     the start day
     * @param endDay       the end day
     * @param universityId the university id
     * @param status       the status
     * @return the list
     */
    @Query(value = "SELECT FORMAT(ul.create_time, 'yyyy-MM-dd') AS date, COUNT(ul.user_id) AS totalLikes " +
            "FROM user_like ul " +
            "JOIN post p ON ul.post_id = p.id " +
            "JOIN consultant_info c ON p.create_by = c.consultant_id " +
            "WHERE ul.create_time BETWEEN :startDay AND :endDay " +
            "AND c.university_id = :universityId " +
            "AND (:status IS NULL OR ul.status = :status) " +
            "GROUP BY FORMAT(ul.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(ul.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalLikesByPeriodAndUniversity(@Param("startDay") Date startDay,
                                                       @Param("endDay") Date endDay,
                                                       @Param("universityId") Integer universityId,
                                                       @Param("status") String status);

    /**
     * Find total favorites by period and university list.
     *
     * @param startDay     the start day
     * @param endDay       the end day
     * @param universityId the university id
     * @param status       the status
     * @return the list
     */
    @Query(value = "SELECT FORMAT(uf.create_time, 'yyyy-MM-dd') AS date, COUNT(uf.user_id) AS totalFavorites " +
            "FROM user_favorite uf " +
            "WHERE uf.create_time BETWEEN :startDay AND :endDay " +
            "AND uf.university_id = :universityId " +
            "AND (:status IS NULL OR uf.status = :status) " +
            "GROUP BY FORMAT(uf.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(uf.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalFavoritesByPeriodAndUniversity(@Param("startDay") Date startDay,
                                                           @Param("endDay") Date endDay,
                                                           @Param("universityId") Integer universityId,
                                                           @Param("status") String status);

    /**
     * Find total comments by period and university list.
     *
     * @param startDay      the start day
     * @param endDay        the end day
     * @param universityId  the university id
     * @param commentStatus the comment status
     * @return the list
     */
    @Query(value = "SELECT FORMAT(c.create_time, 'yyyy-MM-dd') AS date, COUNT(c.id) AS totalComments " +
            "FROM comment c " +
            "JOIN post p ON c.post_id = p.id " +
            "JOIN consultant_info ci ON p.create_by = ci.consultant_id " +
            "WHERE c.create_time BETWEEN :startDay AND :endDay " +
            "AND ci.university_id = :universityId " +
            "AND (:commentStatus IS NULL OR c.comment_status = :commentStatus) " +
            "GROUP BY FORMAT(c.create_time, 'yyyy-MM-dd') " +
            "ORDER BY FORMAT(c.create_time, 'yyyy-MM-dd')", nativeQuery = true)
    List<Object[]> findTotalCommentsByPeriodAndUniversity(@Param("startDay") Date startDay,
                                                          @Param("endDay") Date endDay,
                                                          @Param("universityId") Integer universityId,
                                                          @Param("commentStatus") String commentStatus);

    /**
     * Find list transaction page.
     *
     * @param adsName   the ads name
     * @param status    the status
     * @param orderCode the order code
     * @param pageable  the pageable
     * @return the page
     */
    @Query(value = "SELECT ut.*, ad.name " +
            "FROM university_transaction ut " +
            "JOIN ads_package ad on ut.ads_package_id = ad.id " +
            "WHERE (:adsName IS NULL OR ad.name  LIKE N'%' + :adsName + '%') " +
            "AND (:orderCode IS NULL OR ut.order_code  LIKE N'%' + :orderCode + '%')" +
            "AND (:status IS NULL OR ut.status = :status)", nativeQuery = true)
    Page<UniversityTransaction> findListTransaction(@Param(value = "adsName") String adsName,
                                                    @Param(value = "status") String status,
                                                    @Param(value = "orderCode") String orderCode,
                                                    Pageable pageable);
}
