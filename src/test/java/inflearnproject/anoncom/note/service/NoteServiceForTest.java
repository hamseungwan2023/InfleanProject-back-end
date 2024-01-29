package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.note.dto.*;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.note.repository.NoteDSLRepository;
import inflearnproject.anoncom.note.repository.NoteRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
@Component
public class NoteServiceForTest {
    @Autowired
    private NoteSendServiceForTest noteSenderService;
    @Autowired
    private NoteDSLRepository noteDSLRepository;
    @Autowired
    private NoteRepository noteRepository;

    //TODO : BATCHSIZE로 나중에 일괄처리 해보기
    public List<String> addNote(LoginUserDto userDto, NoteAddDto noteDto) {
        List<String> erroredList = new ArrayList<>();
        for (String receiverNickname : noteDto.getReceiverNicknames()) {
            try {
                noteSenderService.sendNoteToReceiver(userDto, receiverNickname, noteDto.getContent());
            } catch (NoUserEntityException e) {
                erroredList.add(e.getMessage());
            }
        }
        return erroredList;
    }

    public List<Long> deleteSendNote(NoteDeleteDto noteDto) {
        List<Long> deletedList = new ArrayList<>();
        for (Long deleteNoteId : noteDto.getDeleteNoteIds()) {
            try{
                noteSenderService.deleteSendNote(deleteNoteId);
            }catch (NoSuchNoteException e){
                long errorId = Long.parseLong(e.getMessage());
                deletedList.add(errorId);
            }
        }
        return deletedList;
    }

    public List<Long> deleteReceiveNote(NoteDeleteDto noteDto) {
        List<Long> deletedList = new ArrayList<>();
        for (Long deleteNoteId : noteDto.getDeleteNoteIds()) {
            try{
                noteSenderService.deleteReceiveNote(deleteNoteId);
            }catch (NoSuchNoteException e){
                long errorId = Long.parseLong(e.getMessage());
                deletedList.add(errorId);
            }
        }
        return deletedList;
    }

    public Page<NoteShowDto> findReceivedNotes(Long receiverId, NoteSearchCond cond, Pageable pageable){
        return noteDSLRepository.findReceivedNotes(receiverId,cond, pageable);
    }

    public Page<NoteSendedShowDto> findSendedNotes(Long senderId, Pageable pageable) {
        return noteDSLRepository.findSendedNotes(senderId, pageable);
    }

    public List<Long> keepNote(NoteKeepDto noteKeepDto) {
        List<Long> keepList = new ArrayList<>();
        for (Long noteKeepId : noteKeepDto.getNoteKeeps()) {
            try{
                noteSenderService.keepNote(noteKeepId);
            }catch (NoSuchNoteException e){
                long errorId = Long.parseLong(e.getMessage());
                keepList.add(errorId);
            }
        }
        return keepList;
    }

    public List<Long> spamNote(NoteSpamDto noteSpamDto) {

        List<Long> spamList = new ArrayList<>();
        for (Long noteSpamId : noteSpamDto.getSpamNotes()) {
            try{
                noteSenderService.spamNote(noteSpamId);
            }catch (NoSuchNoteException e){
                long errorId = Long.parseLong(e.getMessage());
                spamList.add(errorId);
            }
        }
        return spamList;
    }

    public Note findById(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new NoSuchNoteException("해당 쪽지는 존재하지 않습니다.")
        );
        note.receiverReadTrue();
        return note;
    }

    public List<Long> declareNote(NoteDeclareDto noteDeclareDto) {
        List<Long> declareList = new ArrayList<>();
        for (Long declareNoteId : noteDeclareDto.getDeclareNotes()) {
            try{
                noteSenderService.declareNote(declareNoteId);
            }catch (NoSuchNoteException e){
                long errorId = Long.parseLong(e.getMessage());
                declareList.add(errorId);
            }
        }
        return declareList;
    }
}
