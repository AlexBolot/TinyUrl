package fr.unice.polytech.tinypoly.servlet;

import fr.unice.polytech.tinypoly.log.Log;
import fr.unice.polytech.tinypoly.log.LogImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/taskqueues/queue", produces = "application/json")
public class LogServlet {

    private static final Logger logger = LoggerFactory.getLogger(LogServlet.class);

    private final Log log = new LogImpl();

    @PostMapping
    public void requestTask() throws IOException {
        logger.info("New message");
        log.sendTask();
    }
}
