package fr.unice.polytech.tinypoly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.dao.DatastoreDao;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.Account;
import fr.unice.polytech.tinypoly.entities.PtitU;
import fr.unice.polytech.tinypoly.mailservice.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.*;

@RestController
@RequestMapping(path = "/administration", produces = "application/json")
public class AdministrationController {

    private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);
    private DatastoreDao dao = new DatastoreDao();

    /**
     * Utilisateur enregistré :
     * Obtenir la liste de ses propres contenus (ptit-u -> contenu) avec le nombre de requêtes reçues : en cours
     * Obtenir le détail de tous les accès pour une de ses ptit-u
     * Administrateur :
     * Lister tous les contenus : 
     * Obtenir le détail de n'importe quel contenu
     */

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

    @GetMapping("/account/ptitu/")
    public String getPtitUByMail(@RequestBody String email) {
        List<PtitU> ptitUS = ObjectifyService.run(() -> ofy().load().type(PtitU.class).filter("email", email).list());
        return ptitUS.toString();
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

    @GetMapping(path = "/account/all")
    public HttpReply getAllAccounts() {
        try {
            logger.info("> Asked all accounts");
            List<Account> accounts = dao.listAccounts(null);
            return new HttpReply(SUCCESS, accounts.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReply(FAIL, e.getMessage());
        }
    }
}
