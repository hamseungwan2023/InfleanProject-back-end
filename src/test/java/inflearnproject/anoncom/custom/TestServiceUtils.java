package inflearnproject.anoncom.custom;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.user.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;

public class TestServiceUtils {

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
                .build();
        postService.savePost(user, post);
        return post;
    }
}
