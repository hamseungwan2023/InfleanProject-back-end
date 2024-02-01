package inflearnproject.anoncom.note.repository;

import inflearnproject.anoncom.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Override
    @Query("select n from Note n join fetch n.sender where n.id = :id")
    Optional<Note> findById(@Param("id") Long id);

    @Query
    List<Note> findByIdIn(List<Long> noteIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n SET n.isSenderDelete = true WHERE n.id IN :noteIds")
    int bulkNoteSenderDelete(@Param("noteIds") List<Long> noteIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n SET n.isReceiverDelete = true WHERE n.id IN :noteIds")
    int bulkNoteReceiverDelete(@Param("noteIds") List<Long> noteIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n SET n.isKeep = true WHERE n.id IN :noteIds")
    int bulkNoteKeep(@Param("noteIds") List<Long> noteIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n set n.isSpam = true where n.id in :spamToTrues")
    void updateSpamTrue(@Param("spamToTrues") List<Long> spamToTrues);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n set n.isDeclaration = true where n.id in :noteIds")
    void updateDeclareTrue(@Param("noteIds") List<Long> noteIds);
}
