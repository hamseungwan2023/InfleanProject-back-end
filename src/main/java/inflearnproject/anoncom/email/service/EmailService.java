package inflearnproject.anoncom.email.service;

import inflearnproject.anoncom.domain.EmailVerification;
import inflearnproject.anoncom.email.dto.EmailAuthRequestDto;
import inflearnproject.anoncom.email.dto.EmailVerificationDto;
import inflearnproject.anoncom.email.exception.NotSameCodeException;
import inflearnproject.anoncom.email.exception.UnknownMailException;
import inflearnproject.anoncom.email.repository.EmailVerificationRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

	//의존성 주입을 통해서 필요한 객체를 가져온다.
    private final JavaMailSender emailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    private String authNum; //랜덤 인증 코드

    @Value("${spring.mail.username}")
    private String username;


    //랜덤 인증 코드 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    //메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String setFrom = username; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; //받는 사람
        String title = "AnonCom 인증 번호"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(authNum), "utf-8", "html");

        return message;
    }

    //실제 메일 전송
    public String sendEmail(String toEmail){
    
        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = null;
        try {
            emailForm = createEmailForm(toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new UnknownMailException(e.getMessage());
        }
        //실제 메일 전송
        emailSender.send(emailForm);

        EmailVerification emailVerification = EmailVerification.builder()
                .email(toEmail)
                .code(authNum).build();
        emailVerificationRepository.deleteByEmail(toEmail); //이메일과 일치한 데이터를 db에서 미리 삭제한 이후
        emailVerificationRepository.save(emailVerification); //새로 값 추가
        return authNum; //인증 코드 반환
    }

    public String setContext(String code) {
        String htmlContent = "<h1>인증 코드</h1>"
                + "<p>귀하의 인증 코드는: <strong>" + code + "</strong> 입니다.</p>";
        return htmlContent;
    }

    public boolean verificate(EmailVerificationDto emailVerificationDto) {
        String codeByEmail = emailVerificationRepository.findCodeByEmail(emailVerificationDto.getEmail());
        if(!codeByEmail.equals(emailVerificationDto.getCode())){
            throw new NotSameCodeException("코드가 일치하지 않습니다");
        }
        return true;
    }

    public String verificateUserId(EmailVerificationDto emailVerificationDto){
        String codeByEmail = emailVerificationRepository.findCodeByEmail(emailVerificationDto.getEmail());
        if(!codeByEmail.equals(emailVerificationDto.getCode())){
            throw new NotSameCodeException("코드가 일치하지 않습니다");
        }
        return userRepository.findByEmail(emailVerificationDto.getEmail()).getUsername();
    }
}