package inflearnproject.anoncom.alarm.repository;

import inflearnproject.anoncom.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
