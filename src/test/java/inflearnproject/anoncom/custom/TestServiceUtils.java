package inflearnproject.anoncom.custom;

import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.commentReaction.service.CommentReactionService;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.postReaction.service.PostReactionService;
import inflearnproject.anoncom.reComment.service.ReCommentService;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;

public class TestServiceUtils {

    public static LoginUserDto buildUserDto(UserEntity user) {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getNickname());
        dto.setMemberId(user.getId());
        return dto;
    }

    public static ReqAddPostDto buildPostDto(Post post) {
        ReqAddPostDto dto = new ReqAddPostDto();
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCategory(post.getCategory());
        return dto;
    }

    public static UserEntity addUser(UserService userService) {
        UserEntity user = UserEntity.builder()
                .email("1@naver.com")
                .username("username")
                .nickname("nickname")
                .password("password")
                .roles(new HashSet<>())
                .location("seoul")
                .isActive(true)
                .posts(new ArrayList<>())
                .build();
        userService.joinUser(user);
        return user;
    }

    public static UserEntity addAnotherUser(UserService userService, int i) {
        UserEntity user = UserEntity.builder()
                .email(i + "@naver.com")
                .username("username" + i)
                .nickname("nickname" + i)
                .password("password")
                .roles(new HashSet<>())
                .location("seoul")
                .isActive(true)
                .posts(new ArrayList<>())
                .build();
        userService.joinUser(user);
        return user;
    }

    public static Post addPost(UserEntity user, PostService postService) {
        Post post = Post.builder()
                .title("제목")
                .category("카테고리")
                .content("컨텐츠123123123")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .build();
        return postService.savePost(buildUserDto(user), buildPostDto(post));
    }

    public static Comment addComment(CommentService commentService, Post post, UserEntity user) {
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .userLike(0)
                .userDisLike(0)
                .content("댓글입니다.")
                .reComments(new ArrayList<>())
                .deleted(false)
                .build();
        commentService.saveComment(comment, user, post);
        return comment;
    }

    public static ReComment addReComment(UserEntity user, Comment comment, ReCommentService reCommentService) {
        String content = "대댓글입니다.";

        return reCommentService.addReComment(buildUserDto(user), comment.getId(), content);
    }

    public static void increasePostReaction(PostReactionService postReactionService, Long memberId, Long postId) {
        postReactionService.increaseLike(memberId, postId);
    }

    public static void decreasePostReaction(PostReactionService postReactionService, Long memberId, Long postId) {
        postReactionService.increaseDisLike(memberId, postId);
    }

    public static void increaseReCommentReaction(CommentReactionService commentReactionService, Long memberId, Long commentId) {
        commentReactionService.increaseLike(memberId, commentId);
    }

    public static void decreaseReCommentReaction(CommentReactionService commentReactionService, Long memberId, Long commentId) {
        commentReactionService.increaseDisLike(memberId, commentId);
    }

}
