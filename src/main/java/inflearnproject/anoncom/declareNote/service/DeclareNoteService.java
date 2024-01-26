package inflearnproject.anoncom.declareNote.service;

import inflearnproject.anoncom.declareNote.repository.DeclareNoteRepository;
import inflearnproject.anoncom.domain.DeclareNote;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeclareNoteService {

    private final DeclareNoteRepository declareNoteRepository;

    public Page<DeclareNote> showAll(Pageable pageable){
        return declareNoteRepository.findAll(pageable);
    }
}
