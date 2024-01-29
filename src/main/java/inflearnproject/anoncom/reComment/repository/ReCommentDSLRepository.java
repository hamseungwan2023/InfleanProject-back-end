package inflearnproject.anoncom.reComment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inflearnproject.anoncom.domain.QComment;
import inflearnproject.anoncom.domain.QReComment;
import inflearnproject.anoncom.reComment.dto.QResReCommentDto;
import inflearnproject.anoncom.reComment.dto.ResReCommentDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static inflearnproject.anoncom.domain.QComment.*;
import static inflearnproject.anoncom.domain.QReComment.*;

@Repository
public class ReCommentDSLRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ReCommentDSLRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Map<Long, List<ResReCommentDto>> findReCommentsByPost(Long postId, List<Long> commentIds) {
        List<ResReCommentDto> reComments = queryFactory
                .select(new QResReCommentDto(
                        reComment.id,
                        reComment.comment.id,
                        reComment.comment.user.nickname,
                        reComment.user.isActive,
                        reComment.user.id,
                        reComment.user.nickname,
                        reComment.user.rank,
                        reComment.createdAt,
                        reComment.content,
                        reComment.deleted
                ))
                .from(reComment)
                .where(reComment.post.id.eq(postId), reComment.comment.id.in(commentIds))
                .fetch();

        // 대댓글을 부모 댓글 ID별로 그룹화
        return reComments.stream().collect(Collectors.groupingBy(reComment -> reComment.getParentCommentId()));
    }
}
