package com.codegym.foody.repository;

import com.codegym.foody.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
