package inflearnproject.anoncom.comment.service;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.reComment.service.ReCommentService;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static inflearnproject.anoncom.custom.TestServiceUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReCommentService reCommentService;

    @Autowired
    EntityManager em;
    private UserEntity user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void addUserPost() {
        user = addUser(userService);
        post = addPost(user, postService);
        comment = addComment(commentService, post, user);
    }

    @Test
    @DisplayName("댓글이 잘 저장되었나 확인, 게시글의 총 댓글수가 1증가 했는지 확인")
    void addComment_success() {
        Comment findComment = commentRepository.findCommentById(comment.getId());
        assertEquals(findComment.getContent(), "댓글입니다.");
        assertEquals(post.getComments().size(), 1);
    }

    @Test
    @DisplayName("댓글이 잘 업데이트되었는지 확인")
    void updateComment_success() {
        LoginUserDto dto = buildUserDto(user);
        Comment findComment = commentRepository.findCommentById(comment.getId());
        commentService.updateComment(dto, findComment.getId(), "수정");

        assertEquals(findComment.getContent(), "수정");
    }

    @Test
    @DisplayName("다른 계정의 사용자가 업데이트하려고하면 에러 발생")
    void updateComment_fail() {
        UserEntity user2 = signUpUser2();

        LoginUserDto dto = buildUserDto(user2);

        assertThrows(NotSameUserException.class, () -> commentService.updateComment(dto, comment.getId(), "수정"));
    }

    @Test
    @DisplayName("댓글을 삭제할 시 deleted라는 필드값이 true가 된다")
    void deleteComment_success() {
        LoginUserDto dto = buildUserDto(user);
        commentService.deleteComment(dto, comment.getId());
        assertTrue(comment.isDeleted());
    }

    @Test
    @DisplayName("다른 사용자가 삭제할 시 에러가 발생")
    void deleteComment_fail() {
        UserEntity user2 = signUpUser2();
        LoginUserDto dto = buildUserDto(user2);
        assertThrows(NotSameUserException.class, () -> commentService.deleteComment(dto, comment.getId()));
    }

    private UserEntity signUpUser2() {
        UserEntity user2 = UserEntity.builder()
                .email("2@naver.com")
                .username("username2")
                .nickname("nickname2")
                .password("password2")
                .roles(new HashSet<>())
                .location("seoul")
                .isActive(true)
                .posts(new ArrayList<>())
                .build();
        userService.joinUser(user2);
        return user2;
    }

    @Test
    @DisplayName("대댓글 포함한 댓글 리스트를 출력할 시 batchsize와 querydsl 시간비교하기")
    void show_commentsRecomment_scompare_querydsl_batchsize() {

        for (int i = 30; i < 40; i++) {
            Comment savedComment = commentService.saveComment(buildUserDto(user), post.getId(), "댓글");
            for (int j = 30; j < 40; j++) {
                reCommentService.addReComment(buildUserDto(user), savedComment.getId(), "대댓글");
            }
        }
        LocalDateTime before2 = LocalDateTime.now();
        commentService.findCommentsBatch(post.getId());
        LocalDateTime after2 = LocalDateTime.now();

        Duration duration2 = Duration.between(before2, after2);
        System.out.println("Duration2: " + duration2.getSeconds() + " seconds " + duration2.getNano() + " nanoseconds");

        em.clear();

        LocalDateTime before = LocalDateTime.now();
        commentService.findComments(post.getId());
        LocalDateTime after = LocalDateTime.now();

        Duration duration = Duration.between(before, after);
        System.out.println("Duration: " + duration.getSeconds() + " seconds " + duration.getNano() + " nanoseconds");


    }
}