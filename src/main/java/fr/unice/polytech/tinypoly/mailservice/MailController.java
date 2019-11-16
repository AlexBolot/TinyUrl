package fr.unice.polytech.tinypoly.mailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(path = "/mail", produces = "application/json")
public class MailController {

    @Autowired
    MailService mailService;

    @GetMapping("")
    public String sendMail() {
        try {
            return this.mailService.sendMail("Salut\n\nCeci est un test");
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
        return "Fail to send mail";
    }
}
