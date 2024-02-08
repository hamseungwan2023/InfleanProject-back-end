package inflearnproject.anoncom.alarm.repository;

import inflearnproject.anoncom.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertAlarms(List<UserEntity> users, String content) {
        String sql = "INSERT INTO ALARM (user_id, message, is_read) VALUES ( ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long userId = users.get(i).getId();
                ps.setLong(1, userId);
                ps.setString(2, content);
                ps.setBoolean(3, false);
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

}
