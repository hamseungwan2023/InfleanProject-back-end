package inflearnproject.anoncom.post.service;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.PostSearchCondition;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.post.exception.NoPostException;
import inflearnproject.anoncom.post.repository.PostDSLRepository;
import inflearnproject.anoncom.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static inflearnproject.anoncom.user.exception.ExceptionMessage.NO_POST_MESSAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostDSLRepository postDSLRepository;

    public void savePost(UserEntity user, Post post){
        post.putUser(user);
        post.putLocation(user.getLocation());
        postRepository.save(post);
        user.updateRank();
    }

    public Post update(Long postId, ReqAddPostDto postDto){
        Post post = findPostById(postId);
        post.updateValues(postDto.getTitle(), postDto.getCategory(), postDto.getContent());
        return post;
    }

    public Post findPostById(Long id){
        Post post = postRepository.findPostById(id);
        if(post == null){
            throw new NoPostException(NO_POST_MESSAGE);
        }
        post.addView();
        return post;
    }

    public Page<Post> findPostsByCategory(String category, Pageable pageable){
        return postRepository.findPostByCategory(category,pageable);
    }

    public Page<Post> findPosts(PostSearchCondition cond,Pageable pageable){
        return postDSLRepository.findPostsByCondition(cond,pageable);
    }

    public Page<Post> findPostsByCondByDSL(PostSearchCondition condition,Pageable pageable){
        return postDSLRepository.findPostsByCondition(condition,pageable);
    }

    public void delete(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostException(NO_POST_MESSAGE));
        if (!post.isOwnedBy(userId)){
            throw new NotSameUserException("동일한 유저가 아니라서 삭제가 불가능합니다.");
        }
        postRepository.delete(post);
    }

    public Page<Post> findPostsByLocation(String location, Pageable pageable) {
        return postRepository.findPostsByLocation(location,pageable);
    }

    public Page<Post> findPostsByLocationAndCategory(String location, String category, Pageable pageable) {
        return postRepository.findPostsByLocationAndCategory(location,category,pageable);
    }
}
