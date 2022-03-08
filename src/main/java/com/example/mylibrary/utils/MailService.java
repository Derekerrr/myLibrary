package com.example.mylibrary.utils;

import com.example.mylibrary.entity.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public String sendMail(String email) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("邮箱验证码");
        String code=CodeGenerator.codeGenerator(6);
        message.setText(code);

        try{
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }

        return code;
    }
}
