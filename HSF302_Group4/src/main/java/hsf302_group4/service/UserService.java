package hsf302_group4.service;

import hsf302_group4.entity.UserAccount;

import java.util.Optional;

public interface UserService {
    Optional<UserAccount> login(String username, String password);
    Optional<UserAccount> findByUsername(String username);
}
