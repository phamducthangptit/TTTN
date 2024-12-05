package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
//    List<Manufacturer> findAll
}
