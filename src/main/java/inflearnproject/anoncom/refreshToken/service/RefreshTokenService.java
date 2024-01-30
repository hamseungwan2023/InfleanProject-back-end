package inflearnproject.anoncom.refreshToken.service;


import inflearnproject.anoncom.domain.RefreshToken;
import inflearnproject.anoncom.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken addRefreshToken(RefreshToken refreshToken,Long userId) {
        RefreshToken findRefreshToken = refreshTokenRepository.findRefreshTokenByUserEntityId(userId);
        if(findRefreshToken == null){
            return refreshTokenRepository.save(refreshToken);
        }
        findRefreshToken.setTokenValue(refreshToken.getTokenValue());
        return null;
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findByTokenValue(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional(readOnly = true)
    public Optional<RefreshToken> findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByTokenValue(refreshToken);
    }
}
