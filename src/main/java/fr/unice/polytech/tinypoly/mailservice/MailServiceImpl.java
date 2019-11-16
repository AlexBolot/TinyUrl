package fr.unice.polytech.tinypoly.mailservice;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailServiceImpl implements MailService{

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
}
