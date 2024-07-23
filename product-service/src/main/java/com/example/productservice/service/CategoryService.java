package com.example.productservice.service;

import com.example.productservice.dto.*;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.CategoryDetail;
import com.example.productservice.id.CategoryDetailId;
import com.example.productservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryDetailService categoryDetailService;

    public List<CategoryResponseDTO> getAllCategory() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }




    private CategoryResponseDTO convertToDTO(Category category) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryId(category.getCategoryId());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDetailList(category.getCategoryDetails().stream().map(this::convertDetailToDTO).collect(Collectors.toList()));
        categoryResponseDTO.setImage(category.getImage());
        return categoryResponseDTO;
    }

    private DetailResponseDTO convertDetailToDTO(CategoryDetail categoryDetail) {
        DetailResponseDTO detailResponseDTO = new DetailResponseDTO();
        detailResponseDTO.setDetailId(categoryDetail.getCategoryDetailId().getDetailId());
        detailResponseDTO.setName(categoryDetail.getDetail().getName());
        return detailResponseDTO;
    }

    public Category addNewCategory(CategoryDTO categoryDTO) {
        if (repository.findByname(categoryDTO.getName()).isPresent()) return null; // neu ton tai name do thi khong luu
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());
        return repository.save(category);
    }

    @Transactional
    public CategoryResponseDTO updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        System.out.println(categoryUpdateDTO.toString());
        repository.updateCategory(categoryUpdateDTO.getName(), categoryUpdateDTO.getCategoryId()); // update name
        if(!categoryUpdateDTO.getImage().isEmpty()){// thay đổi ảnh
            repository.updateImageCategory(categoryUpdateDTO.getImage(), categoryUpdateDTO.getCategoryId());
        }
        // list detail này là list người dùng đã thay đổi, từ fe gửi về
        List<DetailResponseDTO> categoryResponseDTOList = categoryUpdateDTO.getCategoryDetails();
        List<CategoryDetailId> categoryDetailIdListFE = new ArrayList<>();
        System.out.println(categoryResponseDTOList.toString());
        // list này là list detail của sản phẩm trước khi chỉnh sửa
        List<CategoryDetailId> categoryDetailIdListBE = categoryDetailService.getCategoryDetail(categoryUpdateDTO.getCategoryId());
        // không có trong be mà có trong fe --> thêm mới detail
        List<CategoryDetailId> listAddDetail = new ArrayList<>();
        // có trong be mà không có trong fe --> xóa
        List<CategoryDetailId> listDeleteDetail = new ArrayList<>();

        // duyệt qua list treen front end -> chuyen ve category detail id
        for (DetailResponseDTO dto : categoryResponseDTOList) {
            CategoryDetailId detailId = new CategoryDetailId();
            detailId.setCategoryId(categoryUpdateDTO.getCategoryId());
            detailId.setDetailId(dto.getDetailId());
            categoryDetailIdListFE.add(detailId);
        }
        System.out.println("LIST FE: " + categoryDetailIdListFE.toString());
        System.out.println("LIST BE: " + categoryDetailIdListBE.toString());
        // duyet qua list tren front end de tim detail them
        for (CategoryDetailId categoryDetailId : categoryDetailIdListFE) {
            if (!categoryDetailIdListBE.contains(categoryDetailId)) listAddDetail.add(categoryDetailId);
        }
        // Duyệt qua danh sách hiện tại từ backend để tìm các chi tiết cần xóa
        for (CategoryDetailId categoryDetailId : categoryDetailIdListBE) {
            if (!categoryDetailIdListFE.contains(categoryDetailId)) listDeleteDetail.add(categoryDetailId);
        }
        for (CategoryDetailId categoryDetailId : listAddDetail) {
            categoryDetailService.addNewCategoryDetail(categoryDetailId.getCategoryId(), categoryDetailId.getDetailId());
        }
        for (CategoryDetailId categoryDetailId : listDeleteDetail) {
            categoryDetailService.deleteCategoryDetail(categoryDetailId.getCategoryId(), categoryDetailId.getDetailId());
        }

        repository.flush();
        System.out.println("List cần xóa: " + listDeleteDetail.toString());
        System.out.println("List cần theem: " + listAddDetail.toString());
        Category category = repository.findBycategoryId(categoryUpdateDTO.getCategoryId());
        CategoryResponseDTO categoryResponseDTO = convertToDTO(category);
        System.out.println(categoryResponseDTO.getDetailList().toString());
        return convertToDTO(category);
    }

    @Transactional
    public int deleteCategory(int id) {
        Category category = repository.findBycategoryId(id);
        if (category == null) {
            return -1;
        } else {
            if (!category.getProducts().isEmpty()) { // đã có sản phẩm nên không xóa được
                return -2;
            } else { // chưa có sản phẩm --> đi xóa khóa ngoại trước --> xóa bảng category detail
                categoryDetailService.deleteCategoryDetail(id);
                repository.deleteById(id);
                return 1;
            }
        }
    }


    public Category getCategoryById(int id) {
        return repository.findBycategoryId(id);
    }
}
