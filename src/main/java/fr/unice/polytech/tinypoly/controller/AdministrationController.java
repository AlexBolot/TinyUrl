package fr.unice.polytech.tinypoly.controller;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.Account;
import fr.unice.polytech.tinypoly.entities.Image;
import fr.unice.polytech.tinypoly.entities.PtitU;
import fr.unice.polytech.tinypoly.mailservice.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/administration", produces = "application/json")
public class AdministrationController {

    private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);

    @Autowired
    private MailService mailService;

    @GetMapping("/logs/{id}")
    public String getLogsById(@PathVariable String id) {
//        Queue q = QueueFactory.getQueue("generate-logs");
//        q.add(
//                TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(id.toString()));
        return "liste des logs pour " + id;
    }

    @PostMapping(value = "/account/ptitu", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String getPtitUByMail(@RequestBody String email) {
        List<PtitU> ptitUS = ObjectifyService.run(() -> ofy().load().type(PtitU.class).filter("email", email).list());
        List<Image> images = ObjectifyService.run(() -> ofy().load().type(Image.class).filter("email", email).list());
        StringBuilder listPtitU = new StringBuilder();
        for (PtitU u : ptitUS) {
            listPtitU.append(String.format("%d => %s used %d time%s%n", u.getHash(), u.getUrl(), u.getCompteur(), (u.getCompteur() > 1) ? "s" : ""));
        }
        for (Image u : images) {
            listPtitU.append(String.format("%d => %s used %d time%s%n", u.getHash(), u.getName(), u.getCompteur(), (u.getCompteur() > 1) ? "s" : ""));
        }
        return listPtitU.toString();
    }

    @GetMapping("/account/ptitu/details/{hash}")
    public String getPtitUById(@PathVariable long hash) {
        logger.info("----" + hash);
        String email = ObjectifyService.run(() -> ofy().load().type(PtitU.class).id(hash).now()).getEmail();
        if (email == null) email = ObjectifyService.run(() -> ofy().load().type(Image.class).id(hash).now()).getEmail();
        logger.info(email);
        mailService.sendEmail(email, "Votre détail de logs pour " + hash, "Voici l'url: " + "https://tinypoly-257609.appspot.com/logs/accessByPtitu/" + hash);
        return "You'll receive a mail soon.";
    }

    @PostMapping(value = "/drop")
    public String dropStorage() {
        Iterable<Key<PtitU>> allKeys = ObjectifyService.run(() -> ofy().load().type(PtitU.class).keys());
        ObjectifyService.run(() -> ofy().delete().keys(allKeys));
        Iterable<Key<Account>> allKeys2 = ObjectifyService.run(() -> ofy().load().type(Account.class).keys());
        ObjectifyService.run(() -> ofy().delete().keys(allKeys2));
        return "Databases have been cleared.";
    }

    @PostMapping(value = "/account", consumes = MediaType.TEXT_PLAIN_VALUE)
    public HttpReply createAccount(@RequestBody String email) {
        Account account = new Account(email);
        logger.info("Creating Account with email " + account.getEmail());

        if (ObjectifyService.run(() -> ofy().load().type(Account.class).filter("email", email).first().now()) != null)
            throw new ResponseStatusException(CONFLICT, "Account with email " + account.getEmail() + " already exists.");

        try {
            ObjectifyService.run(() -> {
                ofy().save().entities(account).now();
                return account;
            });
            logger.info("Account created");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "There was an error while creating the account.");
        }

        mailService.sendEmail(account.getEmail(), "Votre création de compte", "Votre compte tiny-poly a bien été crée " + account.getEmail() + " ! \n" +
                "A partir d'aujourd'hui, le " + LocalDateTime.now() + " vous pouvez utiliser votre compte pour upload des contenus <3");

        throw new ResponseStatusException(OK, "Account created for email " + email);
    }

    @GetMapping(path = "/account/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Account> getAllAccounts() {
        try {
            logger.info("> Asked all accounts");
            return ObjectifyService.run(() -> ofy().load().type(Account.class).list());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "There was an error while getting all the accounts.");
        }
    }
}
