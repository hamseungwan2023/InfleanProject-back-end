package inflearnproject.anoncom.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.QPost;
import inflearnproject.anoncom.domain.QUserEntity;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.PostSearchCondition;
import inflearnproject.anoncom.post.dto.QResAddPostDto;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static inflearnproject.anoncom.domain.QPost.post;
import static inflearnproject.anoncom.domain.QUserEntity.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class PostDSLRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PostDSLRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ResAddPostDto> findPostsByCond(PostSearchCondition cond){
        return queryFactory
                .select(new QResAddPostDto(
                        post.id.as("postId"),
                        post.title,
                        post.category,
                        post.createdAt,
                        userEntity,
                        post.finalLike,
                        post.content
                ))
                .from(post)
                .join(post.user, userEntity)
                .where(
                        titleEq(cond.getTitle()),
                        categoryEq(cond.getCategory()),
                        contentEq(cond.getContent())
                )
                .fetch();
    }

    private BooleanExpression titleEq(String title){
        return hasText(title) ? post.title.contains(title) : null;
    }

    private BooleanExpression categoryEq(String category){
        return hasText(category) ? post.category.eq(category) : null;
    }

    private BooleanExpression contentEq(String content){
        return hasText(content) ? post.content.contains(content) : null;
    }
}
