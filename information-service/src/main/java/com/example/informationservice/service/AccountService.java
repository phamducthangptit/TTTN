package com.example.informationservice.service;

import com.example.informationservice.dto.ChangePasswordDTO;
import com.example.informationservice.dto.ResetPasswordRequest;
import com.example.informationservice.dto.UpdateStatusRequest;
import com.example.informationservice.entity.Account;
import com.example.informationservice.entity.Role;
import com.example.informationservice.entity.Staff;
import com.example.informationservice.entity.User;
import com.example.informationservice.repository.AccountRepository;
import com.example.informationservice.repository.RoleRepository;
import com.example.informationservice.repository.StaffRepository;
import com.example.informationservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateStatusAccount(UpdateStatusRequest updateStatusRequest){
        accountRepository.updateStatusAccount(updateStatusRequest.getStatus(), updateStatusRequest.getId());
    }

    @Transactional
    public int createAccountAndStaff(Staff staff, int roleId, String password, int status) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Optional<Staff> searchUserByEmail = staffRepository.findByEmail(staff.getEmail());
        Optional<Account> searchAccountByUserName = accountRepository.findByuserName(staff.getUserName());
        if(searchUserByEmail.isPresent()){
            return -1;
        }
        if(searchAccountByUserName.isPresent()){
            return -2;
        }


        Account account = new Account();

        account.setRole(role);
        account.setUserName(staff.getUserName());
        account.setPassword(passwordEncoder.encode(password));
        account.setStatus(status);
        account.setCreateAt(LocalDateTime.now());

        accountRepository.save(account);
        staff = staffRepository.save(staff);
        account.setStaff(staff);

        return 1;
    }

    public int changePassword(ChangePasswordDTO changePasswordDTO) {
        Optional<Account> account = accountRepository.findByuserName(changePasswordDTO.getUserName());
        if (account.isPresent()) {
            if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), account.get().getPassword())) {
                return -1; // mật khẩu cũ không khớp với mật khẩu trong db
            } else if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), account.get().getPassword())) {
                return -2; // mật khẩu mới trùng với mật khẩu cũ
            } else {
                accountRepository.updatePassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()), changePasswordDTO.getUserName());
                return 1; // thay đổi thành công
            }
        }
        return -3;
    }

    public void resetPasswordUser(ResetPasswordRequest resetPasswordRequest){
        accountRepository.resetPasswordUser(passwordEncoder.encode(resetPasswordRequest.getNewPassword()), resetPasswordRequest.getEmail());
    }

    public void resetPasswordStaff(ResetPasswordRequest resetPasswordRequest){
        accountRepository.resetPasswordStaff(passwordEncoder.encode(resetPasswordRequest.getNewPassword()), resetPasswordRequest.getEmail());
    }


}
