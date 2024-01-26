package inflearnproject.anoncom.email.controller;

import inflearnproject.anoncom.email.dto.EmailAuthRequestDto;
import inflearnproject.anoncom.email.dto.EmailVerificationDto;
import inflearnproject.anoncom.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/login/mailConfirm")
    public ResponseEntity<String> mailConfirm(@RequestBody EmailAuthRequestDto emailDto) {

        String authCode = emailService.sendEmail(emailDto.getEmail());
        return ResponseEntity.ok().body(authCode);
    }

    @PostMapping("/login/mailVerification")
    public ResponseEntity<Boolean> mailVerification(@RequestBody EmailVerificationDto emailVerificationDto){
        boolean verificate = emailService.verificate(emailVerificationDto);
        return ResponseEntity.ok().body(verificate);
    }
}