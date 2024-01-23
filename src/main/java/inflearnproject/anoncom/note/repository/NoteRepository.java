package inflearnproject.anoncom.note.repository;

import inflearnproject.anoncom.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {
}
