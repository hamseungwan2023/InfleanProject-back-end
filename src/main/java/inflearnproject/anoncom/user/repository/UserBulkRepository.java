package inflearnproject.anoncom.user.repository;

import inflearnproject.anoncom.domain.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertNotes(List<Note> notes) {
        String sql = "INSERT INTO NOTE (receiver_id, sender_id, content, is_receiver_read, is_sender_delete, is_receiver_delete, is_spam, is_declaration, is_keep,created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Note note = notes.get(i);
                ps.setLong(1, note.getReceiver().getId());
                ps.setLong(2, note.getSender().getId());
                ps.setString(3, note.getContent());
                ps.setBoolean(4, note.isReceiverRead());
                ps.setBoolean(5, note.isSenderDelete());
                ps.setBoolean(6, note.isReceiverDelete());
                ps.setBoolean(7, note.isSpam());
                ps.setBoolean(8, note.isDeclaration());
                ps.setBoolean(9, note.isKeep());
                ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            }

            @Override
            public int getBatchSize() {
                return notes.size();
            }
        });
    }

}
