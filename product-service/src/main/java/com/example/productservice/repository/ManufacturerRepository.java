package com.example.productservice.repository;

import com.example.productservice.entity.Manufacturer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Manufacturer m SET m.name = :name, m.country = :country, m.image = :image WHERE m.id = :id")
    void updateManufacturer(@Param("name") String name, @Param("country") String country, @Param("image") String image, @Param("id") int id);

    Manufacturer findBymanufacturerId(int id);
}
