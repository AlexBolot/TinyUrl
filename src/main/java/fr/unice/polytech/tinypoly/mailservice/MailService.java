package fr.unice.polytech.tinypoly.mailservice;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    String sendMail(String msg) throws UnsupportedEncodingException, MessagingException;
}
