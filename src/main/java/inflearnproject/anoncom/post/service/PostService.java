package inflearnproject.anoncom.post.service;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void savePost(UserEntity user, Post post){
        post.putUser(user);
        postRepository.save(post);
    }

    public Post update(Long postId, ReqAddPostDto postDto){
        Post post = findPostById(postId);
        post.updateValues(postDto.getTitle(), postDto.getCategory(), postDto.getContent());
        return post;
    }

    public Post findPostById(Long id){
        Post post = postRepository.findPostById(id);
        if(post == null){
            throw new NullPointerException("존재하지 않는 게시글입니다.");
        }
        return post;
    }
}
