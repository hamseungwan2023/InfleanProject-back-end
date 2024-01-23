package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.note.dto.NoteAddDto;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final NoteSenderService noteSenderService;

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
}
