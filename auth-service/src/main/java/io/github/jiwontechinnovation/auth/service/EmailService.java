package io.github.jiwontechinnovation.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public enum EmailType {
        SIGNUP("이메일 인증 코드", code -> String.format("""
                안녕하세요,

                회원가입을 위한 이메일 인증 코드입니다.

                인증 코드: %s

                이 코드는 10분간 유효합니다.

                감사합니다.
                """, code), "이메일 인증 코드 전송 성공", "이메일 인증 코드 전송 실패"),
        PASSWORD_RESET("비밀번호 재설정 인증 코드", code -> String.format("""
                안녕하세요,

                비밀번호 재설정을 위한 인증 코드입니다.

                인증 코드: %s

                이 코드는 10분간 유효합니다.

                감사합니다.
                """, code), "비밀번호 재설정 인증 코드 전송 성공", "비밀번호 재설정 인증 코드 전송 실패");

        private final String subject;
        private final Function<String, String> bodyTemplate;
        private final String successLog;
        private final String errorLog;

        EmailType(String subject, Function<String, String> bodyTemplate, String successLog, String errorLog) {
            this.subject = subject;
            this.bodyTemplate = bodyTemplate;
            this.successLog = successLog;
            this.errorLog = errorLog;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody(String code) {
            return bodyTemplate.apply(code);
        }

        public String getSuccessLog() {
            return successLog;
        }

        public String getErrorLog() {
            return errorLog;
        }
    }

    @Async("emailTaskExecutor")
    public void sendEmail(String email, String code, EmailType emailType) {
        try {
            // 개발 환경: 로그 출력
            log.info("========================================");
            log.info("📧 [DEV] Email Verification Code");
            log.info("To: {}", email);
            log.info("Code: {}", code);
            log.info("========================================");
            
            // 실제 메일 발송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(emailType.getSubject());
            message.setText(emailType.getBody(code));
            mailSender.send(message);
            
            log.info("{} - email: {}, code: {}", emailType.getSuccessLog(), email, code);
        } catch (Exception e) {
            log.error("{} - email: {}", emailType.getErrorLog(), email, e);
        }
    }

    public void sendVerificationCode(String email, String code) {
        sendEmail(email, code, EmailType.SIGNUP);
    }

    public void sendPasswordResetCode(String email, String code) {
        sendEmail(email, code, EmailType.PASSWORD_RESET);
    }
}
