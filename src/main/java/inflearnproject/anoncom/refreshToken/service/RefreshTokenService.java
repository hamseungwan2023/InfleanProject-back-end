package inflearnproject.anoncom.refreshToken.service;


import inflearnproject.anoncom.domain.RefreshToken;
import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.refreshToken.exception.NoRefreshTokenException;
import inflearnproject.anoncom.refreshToken.repository.RefreshTokenRepository;
import inflearnproject.anoncom.security.jwt.util.JwtTokenizer;
import inflearnproject.anoncom.user.dto.ReqUserLoginDto;
import inflearnproject.anoncom.user.dto.ResUserLoginDto;
import inflearnproject.anoncom.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static inflearnproject.anoncom.refreshToken.exception.RefreshExceptionMessage.NO_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final PostRepository postRepository;

    public RefreshToken addRefreshToken(RefreshToken refreshToken, Long userId) {

        RefreshToken findRefreshToken = refreshTokenRepository.findRefreshTokenByUserEntityId(userId);
        if (findRefreshToken == null) {
            return refreshTokenRepository.save(refreshToken);
        }
        findRefreshToken.setTokenValue(refreshToken.getTokenValue());
        return null;
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findByTokenValue(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional(readOnly = true)
    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByTokenValue(refreshToken).orElseThrow(() -> new NoRefreshTokenException(NO_REFRESH_TOKEN));
    }

    @Transactional
    public ResUserLoginDto login(ReqUserLoginDto loginDto) {
        UserEntity user = userService.findUser(loginDto.getUsername(), loginDto.getPassword());
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getUsername(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getUsername(), roles);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .tokenValue(refreshToken)
                .userEntityId(user.getId())
                .build();

        addRefreshToken(refreshTokenEntity, user.getId());
        int postsCount = postRepository.findPostsCount(user.getId());
        return ResUserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(user.getId())
                .nickname(user.getUsername())
                .postsCount(postsCount)
                .build();
    }

    public ResUserLoginDto buildLoginedInfo(String refreshTokenValue, String accessToken, UserEntity userEntity) {
        ResUserLoginDto loginResponse = ResUserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .memberId(userEntity.getId())
                .nickname(userEntity.getUsername())
                .build();
        return loginResponse;
    }
}
