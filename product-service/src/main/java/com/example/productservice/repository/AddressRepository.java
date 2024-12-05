package com.example.productservice.repository;


import com.example.productservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT a FROM Address a WHERE a.user.account.userName = :userName")
    public List<Address> getAllAddressByUserName(String userName);

    @Query("SELECT COUNT(a) > 0 FROM Address a WHERE a.user.userId = :userId AND a.province = :provinceCode AND a.district = :districtCode AND a.wards = :wardCode AND a.houseNumber = :houseNumber")
    public boolean existsAddress(@Param("userId") int userId,
                                 @Param("provinceCode") int provinceCode,
                                 @Param("districtCode") int districtCode,
                                 @Param("wardCode") int wardCode,
                                 @Param("houseNumber") String houseNumber);
}
