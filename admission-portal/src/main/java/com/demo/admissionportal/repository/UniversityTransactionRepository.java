package com.demo.admissionportal.repository;

import com.demo.admissionportal.dto.response.statistics.StatisticRevenueByTime;
import com.demo.admissionportal.entity.UniversityTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM university_transaction ut " +
            "WHERE ut.university_id = :universityId ", nativeQuery = true)
    List<UniversityTransaction> findByUniversityId(Integer universityId);

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

    @Query(value = "SELECT CONVERT(DATE, ut.create_time) AS date, SUM(ap.price) AS revenue " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID' AND ut.create_time >= DATEADD(MONTH, -12, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ut.create_time) " +
            "ORDER BY CONVERT(DATE, ut.create_time)", nativeQuery = true)
    List<Object[]> findRevenueByLast12MonthsForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ut.create_time) AS date, SUM(ap.price) AS revenue " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID' AND ut.create_time >= DATEADD(MONTH, -6, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ut.create_time) " +
            "ORDER BY CONVERT(DATE, ut.create_time)", nativeQuery = true)
    List<Object[]>  findRevenueByLast6MonthsForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ut.create_time) AS date, SUM(ap.price) AS revenue " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID' AND ut.create_time >= DATEADD(DAY, -30, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ut.create_time) " +
            "ORDER BY CONVERT(DATE, ut.create_time)", nativeQuery = true)
    List<Object[]>  findRevenueByLast30DaysForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ut.create_time) AS date, SUM(ap.price) AS revenue " +
            "FROM university_transaction ut " +
            "INNER JOIN ads_package ap ON ut.ads_package_id = ap.id " +
            "WHERE ut.status = 'PAID' AND ut.create_time >= DATEADD(DAY, -7, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ut.create_time) " +
            "ORDER BY CONVERT(DATE, ut.create_time)", nativeQuery = true)
    List<Object[]> findRevenueByLast7DaysForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ul.create_time) AS date, " +
            "SUM(CASE WHEN ul.status = 'LIKE' THEN 1 ELSE 0 END) AS like_count, " +
            "SUM(CASE WHEN ul.status = 'UNLIKE' THEN 1 ELSE 0 END) AS unlike_count " +
            "FROM user_like ul " +
            "WHERE ul.create_time >= DATEADD(DAY, -7, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ul.create_time) " +
            "ORDER BY CONVERT(DATE, ul.create_time)", nativeQuery = true)
    List<Object[]> findLikeAndUnlikeCountByLast7DaysForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, uf.create_time) AS date, " +
            "SUM(CASE WHEN uf.status = 'FOLLOW' THEN 1 ELSE 0 END) AS follow_count, " +
            "SUM(CASE WHEN uf.status = 'UNFOLLOW' THEN 1 ELSE 0 END) AS unfollow_count " +
            "FROM user_favorite uf " +
            "WHERE uf.create_time >= DATEADD(DAY, -7, GETDATE()) " +
            "GROUP BY CONVERT(DATE, uf.create_time) " +
            "ORDER BY CONVERT(DATE, uf.create_time)", nativeQuery = true)
    List<Object[]> findFollowAndUnfollowCountByLast7DaysForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ul.create_time) AS date, " +
            "SUM(CASE WHEN ul.status = 'LIKE' THEN 1 ELSE 0 END) AS like_count, " +
            "SUM(CASE WHEN ul.status = 'UNLIKE' THEN 1 ELSE 0 END) AS unlike_count " +
            "FROM user_like ul " +
            "WHERE ul.create_time >= DATEADD(DAY, -30, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ul.create_time) " +
            "ORDER BY CONVERT(DATE, ul.create_time)", nativeQuery = true)
    List<Object[]> findLikeAndUnlikeCountByLast30DaysForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, uf.create_time) AS date, " +
            "SUM(CASE WHEN uf.status = 'FOLLOW' THEN 1 ELSE 0 END) AS follow_count, " +
            "SUM(CASE WHEN uf.status = 'UNFOLLOW' THEN 1 ELSE 0 END) AS unfollow_count " +
            "FROM user_favorite uf " +
            "WHERE uf.create_time >= DATEADD(DAY, -30, GETDATE()) " +
            "GROUP BY CONVERT(DATE, uf.create_time) " +
            "ORDER BY CONVERT(DATE, uf.create_time)", nativeQuery = true)
    List<Object[]> findFollowAndUnfollowCountByLast30DaysForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ul.create_time) AS date, " +
            "SUM(CASE WHEN ul.status = 'LIKE' THEN 1 ELSE 0 END) AS like_count, " +
            "SUM(CASE WHEN ul.status = 'UNLIKE' THEN 1 ELSE 0 END) AS unlike_count " +
            "FROM user_like ul " +
            "WHERE ul.create_time >= DATEADD(MONTH, -6, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ul.create_time) " +
            "ORDER BY CONVERT(DATE, ul.create_time)", nativeQuery = true)
    List<Object[]> findLikeAndUnlikeCountByLast6MonthsForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, uf.create_time) AS date, " +
            "SUM(CASE WHEN uf.status = 'FOLLOW' THEN 1 ELSE 0 END) AS follow_count, " +
            "SUM(CASE WHEN uf.status = 'UNFOLLOW' THEN 1 ELSE 0 END) AS unfollow_count " +
            "FROM user_favorite uf " +
            "WHERE uf.create_time >= DATEADD(MONTH, -6, GETDATE()) " +
            "GROUP BY CONVERT(DATE, uf.create_time) " +
            "ORDER BY CONVERT(DATE, uf.create_time)", nativeQuery = true)
    List<Object[]> findFollowAndUnfollowCountByLast6MonthsForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, uf.create_time) AS date, " +
            "SUM(CASE WHEN uf.status = 'FOLLOW' THEN 1 ELSE 0 END) AS follow_count, " +
            "SUM(CASE WHEN uf.status = 'UNFOLLOW' THEN 1 ELSE 0 END) AS unfollow_count " +
            "FROM user_favorite uf " +
            "WHERE uf.create_time >= DATEADD(MONTH, -12, GETDATE()) " +
            "GROUP BY CONVERT(DATE, uf.create_time) " +
            "ORDER BY CONVERT(DATE, uf.create_time)", nativeQuery = true)
    List<Object[]> findFollowAndUnfollowCountByLast12MonthsForAllUniversities();

    @Query(value = "SELECT CONVERT(DATE, ul.create_time) AS date, " +
            "SUM(CASE WHEN ul.status = 'LIKE' THEN 1 ELSE 0 END) AS like_count, " +
            "SUM(CASE WHEN ul.status = 'UNLIKE' THEN 1 ELSE 0 END) AS unlike_count " +
            "FROM user_like ul " +
            "WHERE ul.create_time >= DATEADD(MONTH, -12, GETDATE()) " +
            "GROUP BY CONVERT(DATE, ul.create_time) " +
            "ORDER BY CONVERT(DATE, ul.create_time)", nativeQuery = true)
    List<Object[]> findLikeAndUnlikeCountByLast12MonthsForAllUniversities();
}
