package fr.unice.polytech.tinypoly.mailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailServiceImpl implements MailService{

    @Autowired(required = false)
    private JavaMailSender javaMailSender;
    private String username = "tinyHuet@gmail.com";
    private String password = "pimamhgxphaborrr";
    private Session session;


    public MailServiceImpl(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public String sendMail(String message) throws UnsupportedEncodingException, MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("tinyHuet@gmail.com", "Tiny Huet"));
        msg.addRecipient(Message.RecipientType.TO,
                new InternetAddress("hugo.croenne@gmail.com", "Admin"));
        msg.setSubject("New mail from TinyPoly");
        msg.setText(message);
        Transport.send(msg);
        return "Your email have been sent";
    }

    public String sendEmailTest() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("hugo.croenne@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        javaMailSender.send(msg);
        return "Mail sent";
    }

    public String sendEmail(String dest, String sub, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(dest);
        msg.setSubject(sub);
        msg.setText(message);
        javaMailSender.send(msg);
        return "Mail sent";
    }
}
