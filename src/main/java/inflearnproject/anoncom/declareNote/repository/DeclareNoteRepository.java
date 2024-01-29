package inflearnproject.anoncom.declareNote.repository;

import inflearnproject.anoncom.domain.DeclareNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeclareNoteRepository extends JpaRepository<DeclareNote,Long> {

    @Query("SELECT dn FROM DeclareNote dn " +
            "JOIN FETCH dn.note n " +
            "JOIN FETCH n.receiver " +
            "JOIN FETCH n.sender ")
    Page<DeclareNote> findAllWithDetails(Pageable pageable);
}
