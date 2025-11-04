package org.hsf302_group4.service;

import org.hsf302_group4.entity.UserAccount;
import org.hsf302_group4.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserAccountRepository userRepo;
    public AuthService(UserAccountRepository userRepo){ this.userRepo = userRepo; }

    public UserAccount login(String username, String password){
        return userRepo.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}
