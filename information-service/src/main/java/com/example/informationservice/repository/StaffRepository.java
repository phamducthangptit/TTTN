package com.example.informationservice.repository;

import com.example.informationservice.entity.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByEmail(String email);

    @Query("SELECT s FROM Staff s WHERE s.account.role.roleId = 2")
    List<Staff> getListStaff();

    Optional<Staff> findByuserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Staff s SET s.firstName = :firstName, s.lastName = :lastName, " +
            "s.gender = :gender, s.birthday = :birthday, s.phoneNumber = :phoneNumber, s.address = :address WHERE s.userName = :userName")
    void updateInformation(@Param("userName") String userName, @Param("firstName") String firstName,
                           @Param("lastName") String lastName, @Param("gender") int gender,
                           @Param("birthday") LocalDateTime birthdayUpdate, @Param("phoneNumber") String phoneNumber,
                           @Param("address") String address);
}
