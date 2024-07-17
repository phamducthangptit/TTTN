package com.example.productservice.service;

import com.example.productservice.dto.DetailDTO;
import com.example.productservice.dto.DetailResponseDTO;
import com.example.productservice.entity.Detail;
import com.example.productservice.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailService {
    @Autowired
    private DetailRepository repository;

    public List<DetailResponseDTO> getAllDetail(){
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DetailResponseDTO convertToDTO(Detail detail) {
        DetailResponseDTO detailResponseDTO = new DetailResponseDTO();
        detailResponseDTO.setDetailId(detail.getDetailId());
        detailResponseDTO.setName(detail.getName());
        return detailResponseDTO;
    }

    public Detail addNewDetail(DetailDTO detailDTO){
        if(repository.findByname(detailDTO.getName()).isPresent()) return null;
        Detail detail = new Detail();
        detail.setName(detailDTO.getName());
        return repository.save(detail);
    }
}
