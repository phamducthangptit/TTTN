package com.example.informationservice.repository;

import com.example.informationservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.account.role.roleId = 2")
    List<User> getEmployee();

    @Query("SELECT u FROM User u WHERE u.account.role.roleId = 3")
    List<User> getListGuest();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, " +
            "u.email = :email, u.address = :address, u.phoneNumber = :phoneNumber WHERE u.userId = :id")
    void updateUser(@Param("id") int id, @Param("firstName") String firstName, @Param("lastName") String lastName,
                    @Param("email") String email, @Param("address") String address, @Param("phoneNumber") String phoneNumber);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.account.userName = :username")
    Optional<User> getUserByEmailAndUserName(@Param("email") String email, @Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, " +
            "u.address = :address, u.phoneNumber = :phoneNumber WHERE u.userId = :id")
    void updateInformation(@Param("id") int id, @Param("firstName") String firstName, @Param("lastName") String lastName,
                           @Param("address") String address, @Param("phoneNumber") String phoneNumber);
}
