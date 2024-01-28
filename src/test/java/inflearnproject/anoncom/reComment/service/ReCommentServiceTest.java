package inflearnproject.anoncom.reComment.service;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.reComment.dto.ReqAddReCommentDto;
import inflearnproject.anoncom.reComment.repository.ReCommentRepository;
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
import java.util.List;
import java.util.Optional;

import static inflearnproject.anoncom.custom.TestServiceUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReCommentServiceTest {

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

    private UserEntity user;
    private Post post;
    private Comment comment;
    private ReComment recomment;
    @BeforeEach
    void addUserPost() {
        user = addUser(userService);
        post = addPost(user,postService);
        comment = addComment(commentService,post,user);
        recomment = addReComment(post,user,comment,reCommentService);
    }

    @Test
    @DisplayName("대댓글이 잘 작성되었나 확인, 댓글의 댓글(대댓글)의 사이즈가 1이 나오는지 확인")
    void add_reComment_success(){
        assertNotNull(reCommentRepository.findById(recomment.getId()));
        assertEquals(comment.getReComments().size(),1);
    }

    @Test
    @DisplayName("대댓글이 잘 수정되나 확인하기")
    void update_reComment_success(){
        ReqAddReCommentDto dto = new ReqAddReCommentDto("새대댓글");
        reCommentService.patchComment(buildUserDto(user),recomment.getId(),dto);

        assertEquals(reCommentRepository.findById(recomment.getId()).get().getContent(),"새대댓글");
    }

    @Test
    @DisplayName("계정이 다른 사용자가 대댓글을 수정하려고 하면 에러가 발생")
    void update_reComment_fail(){
        ReqAddReCommentDto dto = new ReqAddReCommentDto("새대댓글");
        assertThrows(NotSameUserException.class, () -> reCommentService.patchComment(buildUserDto(signUpUser2()),recomment.getId(),dto));
    }

    @Test
    @DisplayName("대댓글을 삭제하려고 하면 delete 필드가 true로 변경된다")
    void delete_reComment_success(){
        reCommentService.deleteReComment(buildUserDto(user),recomment.getId());
        assertTrue(recomment.isDeleted());
    }

    @Test
    @DisplayName("계정이 다른 사용자가 대댓글을 삭제하려고 하면 에러가 발생")
    void delete_reComment_fail(){
        assertThrows(NotSameUserException.class, () -> reCommentService.deleteReComment(buildUserDto(signUpUser2()),recomment.getId()));
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