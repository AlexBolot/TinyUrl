package fr.unice.polytech.tinypoly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.tinypoly.dao.DatastoreDao;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.*;

@RestController
@RequestMapping(path = "/administration", produces = "application/json")
public class AdministrationController {

    private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);
    private DatastoreDao dao = new DatastoreDao();

    @GetMapping("/")
    public String hello() {
        return "hello administration!";
    }

    @GetMapping("/logs/{id}")
    public String getLogsById(@PathVariable String id) {
        return "liste des logs pour " + id;
    }

    @PostMapping("/account")
    public HttpReply createAccount(@RequestBody String body) {
        try {
            Account account = new ObjectMapper().readValue(body, Account.class);
            logger.info("> Trying to create Account with id " + account.getId());

            if (dao.hasAccount(account.getId()))
                return new HttpReply(ERROR, "Account with id " + account.getId() + " already exists");

            dao.createAccount(account);

            return new HttpReply(SUCCESS, "Created account " + account);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReply(FAIL, e.getMessage());
        }
    }
}
