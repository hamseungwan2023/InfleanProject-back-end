package inflearnproject.anoncom.reComment.repository;

import inflearnproject.anoncom.domain.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReCommentRepository extends JpaRepository<ReComment, Long> {

}
