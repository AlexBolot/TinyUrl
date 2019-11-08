package fr.unice.polytech.checkid;

import fr.unice.polytech.checkid.repositories.AccountRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/checkid", produces = "application/json")
public class CheckIdController {

    private static final Logger logger = LoggerFactory.getLogger(CheckIdController.class);

    private AccountRepo accountRepo;

    public CheckIdController(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Checks if account with the given ID exists
     *
     * @param id Identifier of the Account
     * @return OK if exists, ERROR if doesn't exist, FAIL in case of internal exception
     */
    @GetMapping(path = "/account/{id}")
    public String validateAccount(@PathVariable long id) {
        try {
            boolean exists = accountRepo.existsById(id);
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
                accountRepo.add(id);
                return "OK";
            }
            return "ERROR";
        } catch (Exception e) {
            return "FAILED";
        }
    }
}
