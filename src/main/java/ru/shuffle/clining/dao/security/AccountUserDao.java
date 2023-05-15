package ru.shuffle.clining.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shuffle.clining.entity.security.AccountUser;

import java.util.Optional;

public interface AccountUserDao extends JpaRepository<AccountUser, Long> {
    Optional<AccountUser> findByUsername(String username);
}
