package fr.unice.polytech.tinypoly.mailservice;

import fr.unice.polytech.tinypoly.dto.MailRequest;
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
    public String sendMailTestJavax() {
        try {
            return this.mailService.sendMail("Salut\n\nCeci est un test");
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
        return "Fail to send mail";
    }

    @GetMapping("/test")
    public String sendMailTestSpring() {
        try {
            return this.mailService.sendEmailTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail to send mail";
    }

    @PostMapping("")
    public String sendMail(@RequestBody MailRequest mailRequest) {
        try {
            return this.mailService.sendEmail(mailRequest.getDestination(), mailRequest.getSubject(), mailRequest.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send mail";
        }
    }
}
