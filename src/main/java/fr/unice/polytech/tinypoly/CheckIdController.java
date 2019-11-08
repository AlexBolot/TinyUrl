package fr.unice.polytech.tinypoly;

import fr.unice.polytech.tinypoly.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/checkid", produces = "application/json")
public class CheckIdController {

    private static final Logger logger = LoggerFactory.getLogger(CheckIdController.class);

    private List<Account> accounts = new ArrayList<>();

    /**
     * Checks if account with the given ID exists
     *
     * @param id Identifier of the Account
     * @return OK if exists, ERROR if doesn't exist, FAIL in case of internal exception
     */
    @GetMapping(path = "/account/{id}")
    public String validateAccount(@PathVariable long id) {
        try {
            boolean exists = false;
            for (Account a : accounts)
                if (a.getId() == id) {
                    exists = true;
                    break;
                }
            logger.info("> Asked CheckID with id : " + id);
            logger.info("Account with id " + id + (exists ? "exists" : "doesn't exist"));
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
            if (validateAccount(id).equals("ERROR")) {
                accounts.add(new Account(id));
                return "OK";
            }
            return "ERROR";
        } catch (Exception e) {
            return "FAILED";
        }
    }
}
