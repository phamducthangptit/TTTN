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

import java.sql.Date;
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
    public int createAccountAndUser(User user, int roleId, String userName, String password, int status) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Optional<User> searchUserByEmail = userRepository.findByemail(user.getEmail());
        Optional<Account> searchAccountByUserName = accountRepository.findByuserName(userName);
        if(searchUserByEmail.isPresent()){
            return -1;
        }
        if(searchAccountByUserName.isPresent()){
            return -2;
        }
        user = userRepository.save(user);

        Account account = new Account();
        account.setUser(user);
        account.setRole(role);
        account.setUserName(userName);
        account.setPassword(passwordEncoder.encode(password));
        account.setStatus(status);
        account.setCreateAt(LocalDateTime.now());

        accountRepository.save(account);
        return 1;
    }

    public String generateToken(String username, String role){
        return jwtService.generateToken(username, role);
    }

    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
