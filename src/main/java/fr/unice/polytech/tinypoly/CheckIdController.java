package fr.unice.polytech.tinypoly;

import fr.unice.polytech.tinypoly.dao.AccountDao;
import fr.unice.polytech.tinypoly.dao.DatastoreDao;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.FAIL;
import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.SUCCESS;

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
    public HttpReply validateAccount(@PathVariable long id) {
        try {
            logger.info("> Asked if Account with id : " + id + " exists");
            boolean hasAccount = dao.hasAccount(id);
            String message = "Account with id " + id + (hasAccount ? " exists" : " doesn't exist");
            return new HttpReply(SUCCESS, message);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReply(FAIL, e.getMessage());
        }
    }
}
