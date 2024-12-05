package com.example.importgoodsservice.service;

import com.example.importgoodsservice.dto.ManufacturerDTO;
import com.example.importgoodsservice.entity.Manufacturer;
import com.example.importgoodsservice.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<ManufacturerDTO> getAllManufacturers() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        return manufacturers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ManufacturerDTO convertToDTO(Manufacturer manufacturer) {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setManufacturerId(manufacturer.getId());
        manufacturerDTO.setManufacturerName(manufacturer.getName());
        return manufacturerDTO;
    }
}
