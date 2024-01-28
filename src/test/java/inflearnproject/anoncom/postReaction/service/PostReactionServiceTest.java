package inflearnproject.anoncom.postReaction.service;

import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
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
class PostReactionServiceTest {

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
    PostReactionService postReactionService;

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
    @DisplayName("게시글 작성한 본인이 아닌 다른 사용자가 게시글을 좋아요 할 시 게시글의 userLike 필드가 1 증가한다")
    void increasePostReaction_success(){
        UserEntity user2 = signUpUser2();
        postReactionService.increaseLike(user2.getId(),post.getId());
        assertEquals(post.getUserLike(),1);
        assertEquals(post.getFinalLike(),1);
    }

    @Test
    @DisplayName("자신이 작성한 게시글에 좋아요를 누를 시 에러가 발생")
    void increase_postReaction_error(){
        assertThrows(NotIncreaseLikeSelfException.class, () -> postReactionService.increaseLike(user.getId(),post.getId()));
    }

    @Test
    @DisplayName("게시글 작성한 본인이 아닌 다른 사용자가 게시글을 싫어요 할 시 게시글의 userdisLike 필드가 1 증가한다")
    void increase_postReaction_disLike_success(){
        UserEntity user2 = signUpUser2();
        postReactionService.increaseDisLike(user2.getId(),post.getId());
        assertEquals(post.getUserDisLike(),1);
        assertEquals(post.getFinalLike(),-1);

    }

    @Test
    @DisplayName("자신이 작성한 게시글에 싫어요를 누를 시 에러가 발생")
    void increase_postReaction_disLike_fail(){
        assertThrows(NotIncreaseLikeSelfException.class, () -> postReactionService.increaseDisLike(user.getId(),post.getId()));
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