package ru.shuffle.clining.dao.security;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.shuffle.clining.entity.security.ConfirmationCode;

public interface ConfirmationCodeDao extends JpaRepository<ConfirmationCode, Long> {
    ConfirmationCode findConfirmationCodeByAccountUser_Id (Long id);
}
