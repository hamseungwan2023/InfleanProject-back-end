package inflearnproject.anoncom.spam.repository;

import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SpamRepository extends JpaRepository<Spam, Long> {

    @Query("SELECT s FROM Spam s WHERE s.declaring.id = :declaringId AND s.declared.id IN :declaredIds")
    List<Spam> findByDeclaringAndDeclaredIds(@Param("declaringId") Long declaringId, @Param("declaredIds") Set<Long> declaredIds);

    List<Spam> findAllByDeclaring(UserEntity declaring);

    @Query("SELECT s FROM Spam s where s.declaring.id = :userId and s.id in :spamIds")
    List<Spam> findByIn(@Param("userId") Long userId, @Param("spamIds") List<Long> spamIds);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Spam s where s.declaring.id = :userId and s.id in :spamIds")
    int deleteSpams(@Param("userId") Long userId, @Param("spamIds") List<Long> spamIds);

}
