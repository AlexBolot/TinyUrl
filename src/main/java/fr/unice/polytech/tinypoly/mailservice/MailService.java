package fr.unice.polytech.tinypoly.mailservice;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    String sendMail(String msg) throws UnsupportedEncodingException, MessagingException;

    String sendEmail(String dest, String sub, String message);

    String sendEmailTest();
}
