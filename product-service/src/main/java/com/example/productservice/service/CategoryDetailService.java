package com.example.productservice.service;

import com.example.productservice.dto.CategoryDetailDTO;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.CategoryDetail;
import com.example.productservice.entity.Detail;
import com.example.productservice.id.CategoryDetailId;
import com.example.productservice.repository.CategoryDetailRepository;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.DetailRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryDetailService {
    @Autowired
    private CategoryDetailRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DetailRepository detailRepository;

    public CategoryDetail addNewCategoryDetail(int categoryId, int detailId) {
        CategoryDetail categoryDetail = new CategoryDetail();
        categoryDetail.setCategoryDetailId(new CategoryDetailId(detailId, categoryId));
        Category category = categoryRepository.findBycategoryId(categoryId);
        Optional<Detail> detail = detailRepository.findBydetailId(detailId);
        categoryDetail.setCategory(category);
        categoryDetail.setDetail(detail.get());
        return repository.save(categoryDetail);
    }

    public void deleteCategoryDetail(int categoryId, int detailId){
        repository.deleteCategoryDetail(categoryId, detailId);
    }

    public void deleteCategoryDetail(int categoryId) {
        repository.deleteCategoryDetail(categoryId);
    }

    public List<CategoryDetailId> getCategoryDetail(int categoryId) {
        return repository.getCategoryDetailId(categoryId);
    }

    public List<CategoryDetailDTO> getCategoryDetailByCategoryId(int categoryId) {
        List<Tuple> tuples = repository.getCategoryDetailByCategoryId(categoryId);
        List<CategoryDetailDTO> dtos = new ArrayList<>();
        for (Tuple tuple : tuples) {
            int detailId = (int) tuple.get("detailId");
            String name = (String) tuple.get("name");
            dtos.add(new CategoryDetailDTO(detailId, name));
        }
        return dtos;
    }
}
