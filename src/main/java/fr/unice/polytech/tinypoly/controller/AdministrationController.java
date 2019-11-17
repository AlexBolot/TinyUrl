package fr.unice.polytech.tinypoly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import fr.unice.polytech.tinypoly.dao.DatastoreDao;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.Account;
import fr.unice.polytech.tinypoly.mailservice.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;

import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.*;

/**
 * _
 * /\) _
 * _   / / (/\
 * /\) ( Y)  \ \
 * / /   ""   (Y )
 * ( Y)  _      ""
 * ""  (/\       _
 * \ \     /\)
 * (Y )   / /
 * ""   ( Y)
 * ""
 */
@RestController
@RequestMapping(path = "/administration", produces = "application/json")
public class AdministrationController {

    private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);
    private DatastoreDao dao = new DatastoreDao();

    @Autowired
    MailService mailService;

    @GetMapping("/")
    public String hello() {
        return "hello administration!";
    }

    @GetMapping("/logs/{id}")
    public String getLogsById(@PathVariable String id) {
        Queue q = QueueFactory.getQueue("generate-logs");
        q.add(
                TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(id.toString()));
        return "liste des logs pour " + id;
    }

    @PostMapping("/account")
    public HttpReply createAccount(@RequestBody String body) {
        try {
            Account account = new ObjectMapper().readValue(body, Account.class);
            account.setId(dao.getLastId() + 1);
            logger.info("> Trying to create Account with id " + account.getId());

            if (dao.hasAccount(account.getId()))
                return new HttpReply(ERROR, "Account with id " + account.getId() + " already exists");

            dao.createAccount(account);
            mailService.sendEmail(account.getEmail(), "Votre création de compte", "Votre compte tiny-poly a bien été crée " + account.getEmail() + " ! \n" +
                    "A partir d'aujourd'hui, le " + LocalDateTime.now() + " vous pouvez utiliser votre compte pour upload des contenus <3");

            return new HttpReply(SUCCESS, "Created account " + account);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReply(FAIL, e.getMessage());
        }
    }
}
