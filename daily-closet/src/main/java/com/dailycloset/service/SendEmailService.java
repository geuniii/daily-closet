package com.dailycloset.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class SendEmailService {

    private JavaMailSender javaMailSender;

    /**
     * 이메일 보내기
     * @param to : 수신 이메일
     * @param body: 이메일 내용
     * @param topic: 이메일 제목
     */
    public void sendEmail(String to, String body, String topic) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(topic);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
            log.info("send");
            System.out.println("sending email!! ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
