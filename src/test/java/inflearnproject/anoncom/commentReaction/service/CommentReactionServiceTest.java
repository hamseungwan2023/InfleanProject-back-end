package inflearnproject.anoncom.commentReaction.service;

import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
import inflearnproject.anoncom.postReaction.service.PostReactionService;
import inflearnproject.anoncom.reComment.repository.ReCommentRepository;
import inflearnproject.anoncom.reComment.service.ReCommentService;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

import static inflearnproject.anoncom.custom.TestServiceUtils.*;
import static inflearnproject.anoncom.custom.TestServiceUtils.addReComment;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentReactionServiceTest {
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
    EntityManager em;
    @Autowired
    ReCommentService reCommentService;
    @Autowired
    ReCommentRepository reCommentRepository;
    @Autowired
    CommentReactionService commentReactionService;

    private UserEntity user;
    private Post post;
    private Comment comment;
    private ReComment recomment;
    @BeforeEach
    void addUserPost() {
        user = addUser(userService);
        post = addPost(user,postService);
        comment = addComment(commentService,post,user);
        recomment = addReComment(user,comment,reCommentService);
    }

    @Test
    @DisplayName("다른 사용자가 좋아요를 누르면 댓글의 좋아요가 1상승하는지 확인")
    void add_reCommentReaction_success(){
        UserEntity user2 = signUpUser2();
        increaseReCommentReaction(commentReactionService,user2.getId(),comment.getId());

        assertEquals(comment.getUserLike(),1);
    }

    @Test
    @DisplayName("댓글을 작성한 본인이 댓글에 좋아요를 누를 시 에러가 발생")
    void add_reCommentReaction_fail(){
        assertThrows(NotIncreaseLikeSelfException.class,() ->increaseReCommentReaction(commentReactionService,user.getId(),comment.getId()));
    }

    @Test
    @DisplayName("다른 사용자가 싫어요를 누르면 댓글의 싫어요가 1상승하는지 확인")
    void decrease_reCommentReaction_success(){
        UserEntity user2 = signUpUser2();
        decreaseReCommentReaction(commentReactionService,user2.getId(),comment.getId());

        assertEquals(comment.getUserDisLike(),1);
    }

    @Test
    @DisplayName("댓글을 작성한 본인이 댓글에 싫어요를 누를 시 에러가 발생")
    void decrease_reCommentReaction_fail(){
        assertThrows(NotIncreaseLikeSelfException.class,() ->decreaseReCommentReaction(commentReactionService,user.getId(),comment.getId()));
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
}