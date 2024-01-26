package inflearnproject.anoncom.email.repository;

import inflearnproject.anoncom.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {

    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT e.code from EmailVerification e where e.email = :email")
    String findCodeByEmail(@Param("email") String email);
}
