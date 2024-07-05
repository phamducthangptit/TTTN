package com.example.informationservice.service;

import com.example.informationservice.dto.EmployeeDTO;
import com.example.informationservice.dto.ResetPasswordRequest;
import com.example.informationservice.dto.UpdateEmployeeRequest;
import com.example.informationservice.entity.User;
import com.example.informationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public List<EmployeeDTO> getAllUsers() {
        List<User> users = userRepository.getAdminAndEmployee();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public int updateEmployee(UpdateEmployeeRequest updateEmployeeRequest){
        Optional<User> existsEmail = userRepository.findByEmail(updateEmployeeRequest.getEmail());
        // tồn tại user có email đó
        if(existsEmail.isPresent() && existsEmail.get().getUserId() != updateEmployeeRequest.getId()){
            return -1;
        }
        userRepository.updateUser(updateEmployeeRequest.getId(), updateEmployeeRequest.getFirstName(), updateEmployeeRequest.getLastName(),
                updateEmployeeRequest.getEmail(), updateEmployeeRequest.getAddress(), updateEmployeeRequest.getPhoneNumber());
        return 1;
    }

    public int resetPassword(ResetPasswordRequest resetPasswordRequest){
        Optional<User> existUser = userRepository.getUserByEmailAndUserName(resetPasswordRequest.getEmail(), resetPasswordRequest.getUserName());
        if(existUser.isPresent()){
            accountService.resetPassword(resetPasswordRequest);
            return 1;
        }
        return -1;
    }

    private EmployeeDTO convertToDTO(User user) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(user.getUserId());
        employeeDTO.setAccountId(user.getAccount() != null ? user.getAccount().getAccount_id() : null);
        employeeDTO.setStatus(user.getAccount() != null ? user.getAccount().getStatus() : null);
        employeeDTO.setFirstName(user.getFirstName());
        employeeDTO.setLastName(user.getLastName());
        employeeDTO.setEmail(user.getEmail());
        employeeDTO.setAddress(user.getAddress());
        employeeDTO.setPhoneNumber(user.getPhoneNumber());
        employeeDTO.setCreateAt(user.getCreateAt());
        employeeDTO.setRoleName(user.getAccount().getRole().getRoleName());
        return employeeDTO;
    }
}
