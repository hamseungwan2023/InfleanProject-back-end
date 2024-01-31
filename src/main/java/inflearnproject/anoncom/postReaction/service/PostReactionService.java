package inflearnproject.anoncom.postReaction.service;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.PostReaction;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.exception.NoPostException;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.postReaction.exception.AlreadyReactionExistsException;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
import inflearnproject.anoncom.postReaction.repository.PostReactionRepository;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static inflearnproject.anoncom.error.ExceptionMessage.NO_POST_MESSAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactionRepository postReactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void increaseLike(Long memberId, Long postId) {
        isAlreadyReactionExists(memberId, postId);
        UserEntity user = userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostException(NO_POST_MESSAGE));
        if (post.isOwnedBy(memberId)) {
            //자신이 작성한 게시글에는 스스로 추천할 수 없다.
            throw new NotIncreaseLikeSelfException("자신이 작성한 게시글에는 좋아요를 할 수 없습니다.");
        }
        PostReaction postReaction = PostReaction.builder()
                .user(user)
                .post(post)
                .liked(true)
                .build();
        post.addUserLike();
        post.buildFinalLike();
        postReactionRepository.save(postReaction);
    }

    public void increaseDisLike(Long memberId, Long postId) {
        isAlreadyReactionExists(memberId, postId);
        UserEntity user = userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostException(NO_POST_MESSAGE));
        if (post.isOwnedBy(memberId)) {
            //자신이 작성한 게시글에는 스스로 추천할 수 없다.
            throw new NotIncreaseLikeSelfException("자신이 작성한 게시글에는 싫어요를 할 수 없습니다.");
        }
        PostReaction postReaction = PostReaction.builder()
                .user(user)
                .post(post)
                .liked(false)
                .build();
        post.addUserDisLike();
        post.buildFinalLike();
        postReactionRepository.save(postReaction);
    }

    public void isAlreadyReactionExists(Long memberId, Long postId) {
        if (postReactionRepository.existsByUserIdAndPostId(memberId, postId)) {
            throw new AlreadyReactionExistsException("이미 좋아요/싫어요한 게시글에 좋아요/싫어요를 할 수 없습니다.");
        }
    }
}
