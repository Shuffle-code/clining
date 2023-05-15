package ru.shuffle.clining.dao.security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shuffle.clining.entity.security.AccountRole;

;

public interface AccountRoleDao extends JpaRepository<AccountRole, Long> {
    AccountRole findByName(String name);

    @Query(value = "SELECT user_role.ROLE_ID FROM user_role WHERE user_role.USER_ID = :id LIMIT 1", nativeQuery = true)
    Integer findRoleIdByUserId(@Param("id") Long id);
}
