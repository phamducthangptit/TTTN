package com.example.informationservice.service;

import com.example.informationservice.dto.*;
import com.example.informationservice.entity.Account;
import com.example.informationservice.entity.Staff;
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

    @Autowired
    private StaffService staffService;


    public List<GuestDTO> getAllGuest() {
        List<User> users = userRepository.getListGuest();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



//
    public int resetPassword(ResetPasswordRequest resetPasswordRequest){
        // check xem tài khoản đó là user hay staff
        Optional<User> user = userRepository.findByEmail(resetPasswordRequest.getEmail());
        Optional<Staff> staff = staffService.getStaffByEmail(resetPasswordRequest.getEmail());
        // gọi account service để reset pass
        if(user.isPresent()){ // reset pass user
            accountService.resetPasswordUser(resetPasswordRequest);
            return 1;
        }
        if(staff.isPresent()){ // reset pass staff
            accountService.resetPasswordStaff(resetPasswordRequest);
            return 1;
        }
        return -1;
    }
    public UserDTO getInformationUser(String userName){
        User user = userRepository.findByuserName(userName);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setCreateAt(user.getCreateAt());
        return userDTO;
    }
    public void updateInformation(UserDTO userDTO){
        userRepository.updateInformation(userDTO.getUserName(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getGender());
    }

    private GuestDTO convertToDTO(User user) {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setUserId(user.getUserId());
        guestDTO.setAccountId(user.getAccount() != null ? user.getAccount().getAccountId() : null);
        guestDTO.setStatus(user.getAccount() != null ? user.getAccount().getStatus() : null);
        guestDTO.setFirstName(user.getFirstName());
        guestDTO.setLastName(user.getLastName());
        guestDTO.setEmail(user.getEmail());;
        guestDTO.setCreateAt(user.getCreateAt());
        guestDTO.setRoleName(user.getAccount().getRole().getRoleName());
        return guestDTO;
    }
}
