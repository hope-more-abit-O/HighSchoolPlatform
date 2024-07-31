package com.demo.admissionportal.repository.impl;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.repository.SearchEngineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

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

    public Page<PostSearchDTO> searchPost(String title, String tag, String schoolName, String code, Pageable pageable) {
        String sql = """
        SELECT p.id AS id,
               p.title AS title, 
               p.create_time AS createTime, 
               p.quote AS quote, 
               p.thumnail AS thumnail, 
               p.url AS url, 
               TRIM(CONCAT(COALESCE(si.first_name, ''), ' ', COALESCE(si.middle_name, ''), ' ', COALESCE(si.last_name, ''))) AS createBy,
               u.avatar AS avatar, 
               TRIM(CONCAT(COALESCE(ui.name, ''), ' ', COALESCE(uc.campus_name, ''))) AS universityName
        FROM [post] p
        JOIN [post_tag] pt ON p.id = pt.post_id
        JOIN [tag] t ON pt.tag_id = t.id
        LEFT JOIN [consultant_info] ci ON p.create_by = ci.consultant_id
        LEFT JOIN [staff_info] si ON p.create_by = si.staff_id
        LEFT JOIN [user] u ON u.id = p.create_by
        LEFT JOIN [university_info] ui ON ci.university_id = ui.university_id
        LEFT JOIN [university_campus] uc ON ui.university_id = uc.university_id
        LEFT JOIN [province] pr ON uc.province_id = pr.id OR si.province_id = pr.id
        WHERE (:title IS NULL OR p.title LIKE :title)
        AND (:schoolName IS NULL OR (CONCAT(ui.name, ' ', uc.campus_name) LIKE :schoolName AND ui.name LIKE :schoolName))
        AND (:tag IS NULL OR t.name LIKE :tag)
        AND (:code IS NULL OR ui.code LIKE :code)
        """;
        Query query = entityManager.createNativeQuery(sql, "PostSearchDTOResult");
        query.setParameter("title", title != null ? "%" + title + "%" : null);
        query.setParameter("schoolName", schoolName != null ? "%" + schoolName + "%" : null);
        query.setParameter("tag", tag != null ? "%" + tag + "%" : null);
        query.setParameter("code", code != null ? "%" + code + "%" : null);

        List<PostSearchDTO> results = query.getResultList();

        return new PageImpl<>(results, pageable, results.size());
    }
}
