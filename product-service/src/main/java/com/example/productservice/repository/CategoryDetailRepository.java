package com.example.productservice.repository;

import com.example.productservice.dto.CategoryDetailDTO;
import com.example.productservice.entity.CategoryDetail;
import com.example.productservice.id.CategoryDetailId;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, CategoryDetailId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CategoryDetail cd WHERE cd.categoryDetailId.categoryId = :categoryId")
    void deleteCategoryDetail(@Param("categoryId") int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM CategoryDetail cd WHERE cd.categoryDetailId.categoryId = :categoryId AND cd.categoryDetailId.detailId = :detailId")
    void deleteCategoryDetail(@Param("categoryId") int categoryId, @Param("detailId") int detailId);

    @Query("SELECT cd.categoryDetailId FROM CategoryDetail cd WHERE cd.categoryDetailId.categoryId = :categoryId")
    List<CategoryDetailId> getCategoryDetailId(@Param("categoryId") int categoryId);

    @Query("SELECT cd.detail.detailId as detailId, cd.detail.name as name FROM CategoryDetail cd WHERE cd.categoryDetailId.categoryId = :categoryId")
    List<Tuple> getCategoryDetailByCategoryId(@Param("categoryId") int categoryId);
}
