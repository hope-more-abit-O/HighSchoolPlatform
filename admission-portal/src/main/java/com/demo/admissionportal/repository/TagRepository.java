package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
