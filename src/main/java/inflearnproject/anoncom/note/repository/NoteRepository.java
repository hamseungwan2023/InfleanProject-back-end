package inflearnproject.anoncom.note.repository;

import inflearnproject.anoncom.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {

    @Override
    @Query("select n from Note n join fetch n.sender where n.id = :id")
    Optional<Note> findById(@Param("id") Long id);

}
