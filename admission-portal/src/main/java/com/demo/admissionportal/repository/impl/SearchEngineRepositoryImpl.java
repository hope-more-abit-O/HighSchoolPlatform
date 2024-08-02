package com.demo.admissionportal.repository.impl;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.repository.SearchEngineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
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
                LEFT JOIN [user] u ON u.id = p.create_by
                LEFT JOIN [university_info] ui ON ci.university_id = ui.university_id
                LEFT JOIN [university_campus] uc ON ui.university_id = uc.university_id
                LEFT JOIN [province] pr ON uc.province_id = pr.id OR si.province_id = pr.id
                WHERE p.status = 'ACTIVE'
                        AND (p.title LIKE :content
                            OR CONCAT(ui.name, ' ', uc.campus_name) LIKE :content
                            OR t.name LIKE :content
                            OR ui.code LIKE :content)
                """;
        Query query = entityManager.createNativeQuery(sql, "PostSearchDTOResult");
        query.setParameter("content", content != null ? "%" + content + "%" : null);
        return query.getResultList();
    }

    @Override
    public List<PostSearchDTO> searchPostByFilter(Integer typeId, Integer locationId, LocalDate startDate, LocalDate endDate, Integer authorId) {
        String sql = """
                                
                 SELECT DISTINCT p.id AS id,
                 p.title AS title,
                 p.create_time AS createTime,
                 p.quote AS quote,
                 p.thumnail AS thumnail,
                 p.url AS url,
                CASE
                    WHEN ui.name IS NOT NULL THEN TRIM(ui.name)
                END AS createBy,
                u.avatar AS avatar
                FROM [post] p
                JOIN [post_type] pt ON p.id = pt.post_id
                JOIN [type] t ON t.id = pt.type_id
                LEFT JOIN [consultant_info] ci ON p.create_by = ci.consultant_id
                LEFT JOIN [staff_info] si ON p.create_by = si.staff_id
                LEFT JOIN [user] u ON u.id = p.create_by
                LEFT JOIN [university_info] ui ON ci.university_id = ui.university_id
                LEFT JOIN [university_campus] uc ON ui.university_id = uc.university_id
                LEFT JOIN [province] pr ON uc.province_id = pr.id OR si.province_id = pr.id
                WHERE p.status = 'ACTIVE'
                       AND (
                        t.id = :typeId
                       OR  ui.university_id = :authorId
                       OR  pr.id = :locationId
                       OR (p.create_time >= :startDate AND p.create_time <= :endDate))
                 """;
        Query query = entityManager.createNativeQuery(sql, "PostSearchDTOResult");
        query.setParameter("typeId", typeId != null ? typeId : null);
        query.setParameter("locationId", locationId != null ? locationId : null);
        query.setParameter("startDate", startDate != null ? startDate : null);
        query.setParameter("endDate", endDate != null ? endDate : null);
        query.setParameter("authorId", authorId != null ? authorId : null);
        return query.getResultList();
    }
}
