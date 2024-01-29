package inflearnproject.anoncom.spam.repository;

import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpamRepository extends JpaRepository<Spam,Long> {

    Optional<Spam> findByDeclaringAndDeclared(UserEntity declaring, UserEntity declared);

    List<Spam> findAllByDeclaring(UserEntity declaring);
}
