package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.note.dto.NoteAddDto;
import inflearnproject.anoncom.note.dto.NoteDeleteDto;
import inflearnproject.anoncom.note.dto.NoteShowDto;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.note.repository.NoteDSLRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final NoteSenderService noteSenderService;
    private final NoteDSLRepository noteDSLRepository;
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

    public Page<NoteShowDto> findNotes(Long receiverId, Pageable pageable){
        return noteDSLRepository.findNotes(receiverId,pageable);
    }
}
