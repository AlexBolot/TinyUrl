package fr.unice.polytech.tinypoly;

import fr.unice.polytech.tinypoly.dao.AccountDao;
import fr.unice.polytech.tinypoly.dao.DatastoreDao;
import fr.unice.polytech.tinypoly.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/checkid", produces = "application/json")
public class CheckIdController {

    private static final Logger logger = LoggerFactory.getLogger(CheckIdController.class);

    private AccountDao dao = new DatastoreDao();

    /**
     * Checks if account with the given ID exists
     *
     * @param id Identifier of the Account
     * @return OK if exists, ERROR if doesn't exist, FAIL in case of internal exception
     */
    @GetMapping(path = "/account/{id}")
    public String validateAccount(@PathVariable long id) {
        try {
            List<Account> accounts = dao.listAccounts(null);
            logger.info("> Asked if Account with id : " + id + " exists");
            boolean exists = accounts.stream().anyMatch(account -> account.getId() == id);
            logger.info("Account with id " + id + (exists ? " exists" : " doesn't exist"));
            return exists ? "OK" : "ERROR";
        } catch (Exception e) {
            return "FAILED";
        }
    }

    /**
     * Checks if account with the given ID exists
     *
     * @param id Identifier of the Account
     * @return OK if exists, ERROR if doesn't exist, FAIL in case of internal exception
     */
    @PostMapping(path = "/register/{id}")
    public String registerAccount(@PathVariable long id) {
        try {
            logger.info("> Trying to create Account with id "+ id);
            if (validateAccount(id).equals("ERROR")) {
                dao.createAccount(new Account(id, "aa@gmail.com"));
                return "OK";
            }
            return "ERROR";
        } catch (Exception e) {
            return "FAILED";
        }
    }
}
