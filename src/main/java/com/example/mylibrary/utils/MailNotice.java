package com.example.mylibrary.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotice {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String toEmail,String text) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("!图书馆申请结果通知！");

        message.setText(text);

        try{
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
