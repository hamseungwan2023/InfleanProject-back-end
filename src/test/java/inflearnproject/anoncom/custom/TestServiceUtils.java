package inflearnproject.anoncom.custom;

import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;

public class TestServiceUtils {

    public static LoginUserDto buildUserDto(UserEntity user){
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getNickname());
        dto.setMemberId(user.getId());
        return dto;
    }

    public static UserEntity addUser(UserService userService){
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

    public static Post addPost(UserEntity user,PostService postService){
        Post post = Post.builder()
                .title("제목")
                .category("카테고리")
                .content("컨텐츠")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .comments(new ArrayList<>())
                .build();
        postService.savePost(user, post);
        return post;
    }

   public static Comment addComment(CommentService commentService, Post post, UserEntity user){
       Comment comment = Comment.builder()
               .post(post)
               .user(user)
               .userLike(0)
               .userDisLike(0)
               .content("댓글입니다.")
               .deleted(false)
               .build();
       commentService.saveComment(comment,user,post);
       return comment;
   }
}
