package fr.unice.polytech.tinypoly.controller;

import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.*;

@RestController
@RequestMapping(path = "/checkid", produces = "application/json")
public class CheckIdController {

    private static final Logger logger = LoggerFactory.getLogger(CheckIdController.class);

    //**
    // * Checks if account with the given ID exists
    // *
    // * @param id Identifier of the Account
    // * @return OK if exists, ERROR if doesn't exist, FAIL in case of internal exception
    // */
    /*@GetMapping(path = "/account/{id}")
    public HttpReply validateAccount(@PathVariable long id) {
        try {
            logger.info("> Asked if Account with id : " + id + " exists");
            boolean hasAccount = ObjectifyService.run(() -> ofy().load().type(Account.class).id(id).now()) != null;
            String message = "Account with id " + id + (hasAccount ? " exists" : " doesn't exist");
            return new HttpReply(hasAccount ? SUCCESS : ERROR, message);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReply(FAIL, e.getMessage());
        }
    }*/

    @PostMapping(path = "/account", consumes = MediaType.TEXT_PLAIN_VALUE)
    public HttpReply validateAccount(@RequestBody String email) {
        try {
            logger.info("> Asked if Account with id : " + email + " exists");
            boolean hasAccount = ObjectifyService.run(() -> ofy().load().type(Account.class).filter("email", email).first().now()) != null;
            String message = "Account with id " + email + (hasAccount ? " exists" : " doesn't exist");
            return new HttpReply(hasAccount ? SUCCESS : ERROR, message);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReply(FAIL, e.getMessage());
        }
    }
}
