package inflearnproject.anoncom.alarm.repository;

import inflearnproject.anoncom.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a from Alarm a where a.user.id = :id")
    List<Alarm> findByUserId(@Param("id") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Alarm a where a.user.id = :id")
    void deleteAllByUserId(@Param("id") Long id);
}
