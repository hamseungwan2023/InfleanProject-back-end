package inflearnproject.anoncom.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import inflearnproject.anoncom.post.dto.PostSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static inflearnproject.anoncom.domain.QPost.post;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class PostDSLRepository {

    public static final String ORDER_BY_CREATED_AT = "createdDate";
    public static final String ORDER_BY_FINAL_LIKE = "finalLike";

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PostDSLRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<Post> findPostsByCondition(PostSearchCondition cond, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.comments).fetchJoin()
                .leftJoin(post.user).fetchJoin()
                .where(
                        titleEq(cond.getTitle()),
                        contentEq(cond.getContent()),
                        categoryEq(cond.getCategory()),
                        locationEq(cond.getLocation()),
                        titleContentEq(cond.getTitleContent())
                );

        if (hasText(cond.getOrderBy())) {
            if (cond.getOrderBy().equals(ORDER_BY_CREATED_AT)) {
                query.orderBy(post.createdAt.desc());
            } else if (cond.getOrderBy().equals(ORDER_BY_FINAL_LIKE)) {
                query.orderBy(post.finalLike.desc());
            }
        }

        List<Post> posts = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(post.count())
                        .from(post)
                        .where(
                                titleEq(cond.getTitle()),
                                contentEq(cond.getContent()),
                                categoryEq(cond.getCategory()),
                                locationEq(cond.getLocation()),
                                titleContentEq(cond.getTitleContent())
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(posts, pageable, total);
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? post.title.contains(title) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression categoryEq(PostCategory category) {
        return category != null ? post.category.eq(category) : null;
    }

    private BooleanExpression contentEq(String content) {
        return hasText(content) ? post.content.contains(content) : Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression locationEq(LocationType location) {
        return location != null ? post.location.eq(location) : null;
    }

    private BooleanExpression titleContentEq(String titleContent) {
        return titleEq(titleContent).or(contentEq(titleContent));
    }
}
