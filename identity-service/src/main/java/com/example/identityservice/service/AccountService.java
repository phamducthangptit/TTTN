package com.example.identityservice.service;

import com.example.identityservice.entity.Account;
import com.example.identityservice.entity.Role;
import com.example.identityservice.entity.User;
import com.example.identityservice.repository.AccountRepository;
import com.example.identityservice.repository.RoleRepository;
import com.example.identityservice.repository.UserRepository;
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
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public int createAccountAndUser(User user, int roleId, String password, int status) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        System.out.println(user.getEmail().getClass());
        Optional<User> searchUserByEmail = userRepository.findByemail(user.getEmail());
        Optional<Account> searchAccountByUserName = accountRepository.findByUserName(user.getUserName());
        if (searchUserByEmail.isPresent()) {
            return -1;
        }
        if (searchAccountByUserName.isPresent()) {
            return -2;
        }

        Account account = new Account();
        account.setRole(role);
        account.setUserName(user.getUserName());
        account.setPassword(passwordEncoder.encode(password));
        account.setStatus(status);
        account.setCreateAt(LocalDateTime.now());

        accountRepository.save(account); // lưu account trước rồi lưu user sau
        user = userRepository.save(user);
        account.setUser(user);

        return 1;
    }

    public String generateToken(String username, String role) {
        return jwtService.generateToken(username, role);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
