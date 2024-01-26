package inflearnproject.anoncom.post.aspect;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Aspect
@RequiredArgsConstructor
@Component
@Transactional
public class AdminRoleCheckAspect {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;

    @Around("execution(* inflearnproject.anoncom.post.service.PostService.delete(..)) && args(userId, postId)")
    public Object checkAdminRole(ProceedingJoinPoint joinPoint, Long userId, Long postId) throws Throwable {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다"));
        Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
        if (user.getRoles().contains(role.get())) {
            // 관리자인 경우, 바로 메소드 실행
            postRepository.deleteById(postId);
            return null;
        } else {
            // 관리자가 아닌 경우, 원래 메소드 로직 실행
            return joinPoint.proceed(new Object[] {userId, postId});
        }
    }
}