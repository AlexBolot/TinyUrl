package fr.unice.polytech.tinypoly;

import fr.unice.polytech.tinypoly.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/administration", produces = "application/json")
public class AdministrationController {

    private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);

    @GetMapping("/")
    public String hello() {
        return "hello administration!";
    }

    @GetMapping("/logs/{id}")
    public String getLogsById(@PathVariable String id) {
        return "liste des logs pour " + id;
    }

    @PostMapping("/accounts")
    public String createAccount(@RequestBody String body) {
        Account newAccount;
        newAccount = new Account(body);
    }
}
