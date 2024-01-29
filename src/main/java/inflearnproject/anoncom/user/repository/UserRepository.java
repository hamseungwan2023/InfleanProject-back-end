package inflearnproject.anoncom.user.repository;

import inflearnproject.anoncom.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    @Query("select u.profileImg from UserEntity u where u.id = :id")
    String findProfileImgById(@Param("id") Long id);

    Optional<UserEntity> findByNickname(String nickname);

    @Query("select u.nickname from UserEntity u where u.nickname LIKE :nickname and u.id not in (select s.declared.id from Spam s where s.declaring.id = :declaringId)")
    List<String> findNicknamesByNickname(@Param("nickname") String nickname, @Param("declaringId") Long userId);

    List<UserEntity> findAllByIsBlockedTrueAndBlockUntilBefore(LocalDateTime time);
}
