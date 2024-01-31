package inflearnproject.anoncom.spam.repository;

import inflearnproject.anoncom.domain.Spam;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpamBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertSpams(List<Spam> spams) {
        String sql = "INSERT INTO SPAM (declared_id, declaring_id) VALUES (?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Spam spam = spams.get(i);
                ps.setLong(1, spam.getDeclared().getId());
                ps.setLong(2, spam.getDeclaring().getId());
            }

            @Override
            public int getBatchSize() {
                return spams.size();
            }
        });
    }
}
