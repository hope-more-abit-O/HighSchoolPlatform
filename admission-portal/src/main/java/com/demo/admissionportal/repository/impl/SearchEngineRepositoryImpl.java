package com.demo.admissionportal.repository.impl;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.repository.SearchEngineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Search engine repository.
 */
@Repository
public class SearchEngineRepositoryImpl extends SimpleJpaRepository<Post, Integer> implements SearchEngineRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new Search engine repository.
     *
     * @param entityManager the entity manager
     */
    public SearchEngineRepositoryImpl(EntityManager entityManager) {
        super(Post.class, entityManager);
    }

    public List<PostSearchDTO> searchPost(String content) {
        String sql = """
                SELECT DISTINCT p.id AS id,
                              p.title AS title,
                              p.create_time AS createTime,
                              p.quote AS quote,
                              p.thumnail AS thumnail,
                              p.url AS url,
                              CASE
                                        WHEN si.first_name IS NOT NULL THEN TRIM(CONCAT(COALESCE(si.first_name, ''), ' ', COALESCE(si.middle_name, ''), ' ', COALESCE(si.last_name, '')))
                                        WHEN ui.name IS NOT NULL THEN TRIM(ui.name)
                              END AS createBy,
                              u.avatar AS avatar
                FROM [post] p
                JOIN [post_tag] pt ON p.id = pt.post_id
                JOIN [tag] t ON pt.tag_id = t.id
                LEFT JOIN [consultant_info] ci ON p.create_by = ci.consultant_id
                LEFT JOIN [staff_info] si ON p.create_by = si.staff_id
                LEFT JOIN [university_info] ui ON ci.university_id = ui.university_id
                LEFT JOIN [university_campus] uc ON ui.university_id = uc.university_id
                LEFT JOIN [user] u ON u.id = si.staff_id  OR u.id = ui.university_id
                LEFT JOIN [province] pr ON uc.province_id = pr.id OR si.province_id = pr.id
                WHERE p.status = 'ACTIVE'
                        AND ( (p.title IS NULL OR p.title LIKE :content)
                            OR ((CONCAT(ui.name, ' ', uc.campus_name) IS NULL OR CONCAT(ui.name, ' ', uc.campus_name) LIKE :content)
                            OR (t.name IS NULL OR t.name LIKE :content))
                            OR (ui.code IS NULL OR ui.code LIKE :content))
                """;
        Query query = entityManager.createNativeQuery(sql, "PostSearchDTOResult");
        query.setParameter("content", content != null ? "%" + content + "%" : null);
        return query.getResultList();
    }

    @Override
    public List<PostSearchDTO> searchPostByFilter(String content, List<Integer> typeId, List<Integer> locationId, LocalDate startDate, LocalDate endDate, List<Integer> authorId) {
        StringBuilder sql = new StringBuilder("""
                    SELECT p.id AS id,
                        p.title AS title,
                        p.create_time AS createTime,
                        p.quote AS quote,
                        p.thumnail AS thumnail,
                        p.url AS url,
                        u.avatar AS avatar,
                        p.create_by AS createBy,
                        p.title AS orderByTitle,
                        CONCAT(ui.name, ' ', uc.campus_name) AS orderByName,
                        t.name AS orderByTypeName,
                        ui.code AS orderByCode
                    FROM [post] p
                    JOIN [post_type] pt ON p.id = pt.post_id
                    JOIN [type] t ON t.id = pt.type_id
                    LEFT JOIN [consultant_info] ci ON p.create_by = ci.consultant_id
                    LEFT JOIN [staff_info] si ON p.create_by = si.staff_id
                    LEFT JOIN [university_info] ui ON ci.university_id = ui.university_id
                    LEFT JOIN [university_campus] uc ON ui.university_id = uc.university_id
                    LEFT JOIN [user] u ON u.id = p.create_by
                    LEFT JOIN [province] pr ON uc.province_id = pr.id OR si.province_id = pr.id
                    WHERE p.status = 'ACTIVE'
                """);

        boolean hasPreviousCondition = false;


        if (content != null && !content.trim().isEmpty()) {
            sql.append(" AND (p.title LIKE :content OR ui.name LIKE :content OR t.name LIKE :content OR ui.code LIKE :content)");
            hasPreviousCondition = true;
        }

        if (typeId != null && !typeId.isEmpty()) {
            sql.append(hasPreviousCondition ? " OR " : " AND ");
            sql.append(" t.id IN (:typeId)");
            hasPreviousCondition = true;
        }

        if (authorId != null && !authorId.isEmpty()) {
            sql.append(hasPreviousCondition ? " OR " : " AND ");
            sql.append(" ui.university_id IN (:authorId)");
            hasPreviousCondition = true;
        }

        if (locationId != null && !locationId.isEmpty()) {
            sql.append(hasPreviousCondition ? " OR " : " AND ");
            sql.append(" pr.id IN (:locationId)");
            hasPreviousCondition = true;
        }

        if (startDate != null) {
            sql.append(hasPreviousCondition ? " OR " : " AND ");
            sql.append(" p.create_time >= :startDate");
            hasPreviousCondition = true;
        }

        if (endDate != null) {
            sql.append(hasPreviousCondition ? " OR " : " AND ");
            sql.append(" p.create_time <= :endDate");
        }

        sql.append(" ORDER BY CASE WHEN p.title LIKE :content OR ui.name LIKE :content OR t.name LIKE :content OR ui.code LIKE :content THEN 0 ELSE 1 END, p.create_time DESC");
        Query query = entityManager.createNativeQuery(sql.toString(), "PostSearchDTOResult");

        if (typeId != null && !typeId.isEmpty()) {
            query.setParameter("typeId", typeId);
        }

        if (authorId != null && !authorId.isEmpty()) {
            query.setParameter("authorId", authorId);
        }

        if (locationId != null && !locationId.isEmpty()) {
            query.setParameter("locationId", locationId);
        }

        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }

        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        if (content != null && !content.trim().isEmpty()) {
            query.setParameter("content", "%" + content + "%");
        } else {
            query.setParameter("content", null);
        }

        return query.getResultList();
    }
}
