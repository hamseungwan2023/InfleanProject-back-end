package inflearnproject.anoncom.declareNote.repository;

import inflearnproject.anoncom.domain.DeclareNote;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeclareBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertNotes(List<DeclareNote> notes) {
        String sql = "INSERT INTO DECLARE_NOTE (note_id) VALUES (?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DeclareNote note = notes.get(i);
                ps.setLong(1, note.getNote().getId());

            }

            @Override
            public int getBatchSize() {
                return notes.size();
            }
        });
    }
}
