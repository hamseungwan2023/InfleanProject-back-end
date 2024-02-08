package inflearnproject.anoncom.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inflearnproject.anoncom.domain.Notification;
import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import inflearnproject.anoncom.notification.dto.NotificationSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inflearnproject.anoncom.domain.QNotification.notification;

@Repository
public class NotificationDSLRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public NotificationDSLRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Notification> findNotificationsByCondition(NotificationSearchCondition cond) {

        List<Notification> notifications = queryFactory
                .select(notification)
                .from(notification)
                .where(
                        categoryEq(cond.getCategory()),
                        locationEq(cond.getLocation())
                ).fetch();

        return notifications;
    }

    private BooleanExpression categoryEq(PostCategory category) {
        return category != null ? notification.category.eq(category) : null;
    }

    private BooleanExpression locationEq(LocationType location) {
        return location != null ? notification.location.eq(location) : null;
    }
}
