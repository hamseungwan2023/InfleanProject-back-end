package inflearnproject.anoncom.note.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inflearnproject.anoncom.note.dto.NoteSendedShowDto;
import inflearnproject.anoncom.note.dto.NoteShowDto;
import inflearnproject.anoncom.note.dto.QNoteSendedShowDto;
import inflearnproject.anoncom.note.dto.QNoteShowDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static inflearnproject.anoncom.domain.QNote.*;
@Repository
public class NoteDSLRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public NoteDSLRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<NoteShowDto> findReceivedNotes(Long receiverId, Pageable pageable){
        JPAQuery<NoteShowDto> query = queryFactory
                .select(new QNoteShowDto(note.id,
                        note.sender.id,
                        note.sender.nickname,
                        note.content,
                        note.createdAt,
                        note.createdAt,
                        note.isReceiverRead))
                .from(note)
                .leftJoin(note.sender)
                .where(
                        note.receiver.id.eq(receiverId)
                );


        List<NoteShowDto> notes = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(note.count())
                        .from(note)
                        .where(
                                note.receiver.id.eq(receiverId)
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(notes, pageable, total);
    }

    public Page<NoteSendedShowDto> findSendedNotes(Long senderId, Pageable pageable) {
        JPAQuery<NoteSendedShowDto> query = queryFactory
                .select(new QNoteSendedShowDto(note.id,
                        note.receiver.id,
                        note.receiver.nickname,
                        note.content,
                        note.createdAt,
                        note.createdAt,
                        note.isReceiverRead))
                .from(note)
                .leftJoin(note.sender)
                .where(
                        note.sender.id.eq(senderId)
                );


        List<NoteSendedShowDto> notes = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(note.count())
                        .from(note)
                        .where(
                                note.sender.id.eq(senderId)
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(notes, pageable, total);
    }
}
