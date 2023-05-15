package ru.shuffle.clining.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shuffle.clining.entity.security.Authority;

public interface AuthorityDao extends JpaRepository<Authority, Long> {
}
