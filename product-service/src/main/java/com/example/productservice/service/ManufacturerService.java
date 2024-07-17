package com.example.productservice.service;

import com.example.productservice.dto.ManufacturerDTO;
import com.example.productservice.dto.ManufacturerUpdateDTO;
import com.example.productservice.entity.Manufacturer;
import com.example.productservice.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository repository;

    public List<ManufacturerUpdateDTO> getAllManufacturer() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ManufacturerUpdateDTO convertToDTO(Manufacturer manufacturer) {
        ManufacturerUpdateDTO manufacturerUpdateDTO = new ManufacturerUpdateDTO();
        manufacturerUpdateDTO.setManufacturerId(manufacturer.getManufacturerId());
        manufacturerUpdateDTO.setName(manufacturer.getName());
        manufacturerUpdateDTO.setCountry(manufacturer.getCountry());
        manufacturerUpdateDTO.setImage(manufacturer.getImage());
        return manufacturerUpdateDTO;
    }

    public Manufacturer addNewManufacturer(ManufacturerDTO manufacturerDTO) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerDTO.getName());
        manufacturer.setCountry(manufacturerDTO.getCountry());
        manufacturer.setImage(manufacturerDTO.getImage());
        manufacturer.setProducts(null);
        Manufacturer manufacturer1 = repository.save(manufacturer);
        return manufacturer1;
    }

    public void updateManufacturer(ManufacturerUpdateDTO manufacturerUpdateDTO) {
        repository.updateManufacturer(manufacturerUpdateDTO.getName(), manufacturerUpdateDTO.getCountry(), manufacturerUpdateDTO.getImage(), manufacturerUpdateDTO.getManufacturerId());
    }

    public int deleteManufacturer(int id) {
        Manufacturer manufacturer = repository.findBymanufacturerId(id);
        if (manufacturer == null) {
            return -2;
        } else {
            if (manufacturer.getProducts().isEmpty()) { // nhà sản xuất chưa cung cấp sản phẩm thì xóa được
                repository.deleteById(id);
                return 1;
            }
            return -1;
        }
    }

    public Manufacturer getManufacturerById(int id){
        return repository.findBymanufacturerId(id);
    }
}
