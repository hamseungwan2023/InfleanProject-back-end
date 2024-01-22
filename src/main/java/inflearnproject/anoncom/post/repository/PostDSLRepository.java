package inflearnproject.anoncom.post.repository;

import com.querydsl.core.QueryResults;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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


    public Page<Post> findPostsByCondition(PostSearchCondition cond, Pageable pageable){
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.comments).fetchJoin()
                .leftJoin(post.user).fetchJoin()
                .where(
                        titleEq(cond.getTitle()),
                        contentEq(cond.getContent()),
                        categoryEq(cond.getCategory()),
                        locationEq(cond.getLocation())
                )
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(post.count())
                        .from(post)
                        .where(
                                titleEq(cond.getTitle()),
                                contentEq(cond.getContent()),
                                categoryEq(cond.getCategory()),
                                locationEq(cond.getLocation())
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(posts, pageable, total);
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

    private BooleanExpression locationEq(String location){
        return hasText(location) ? post.location.eq(location) : null;
    }
}
