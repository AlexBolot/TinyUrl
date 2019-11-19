package fr.unice.polytech.tinypoly.controller;

import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.dto.PtitURequest;
import fr.unice.polytech.tinypoly.entities.LogEntry;
import fr.unice.polytech.tinypoly.entities.PtitU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/ptitu")
public class RedirectionController {

    private static final Logger logger = LoggerFactory.getLogger(RedirectionController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{hash}")
    public RedirectView redirect(@RequestHeader(name = "Host") final String host, @PathVariable long hash, RedirectAttributes attributes, HttpServletRequest request) {
        try {
            PtitU ptitU = ObjectifyService.run(() -> ofy().load().type(PtitU.class).id(hash).now());
            if (ptitU == null)
                throw new ResponseStatusException(NOT_FOUND, "This short URL does not exist.");
            ptitU.addCompteur();
            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
            attributes.addAttribute("attribute", "redirectWithRedirectView");
            ObjectifyService.run(() -> ofy().save().entity(ptitU).now());
            LogEntry logEntry = new LogEntry(String.valueOf(hash), ptitU.getEmail(), getClientIp(request), System.currentTimeMillis(), LogEntry.Type.IMAGE, HttpReply.Status.SUCCESS);
            restTemplate.postForObject("http://" + host + "/logs/add", logEntry, Void.class);
            return new RedirectView(ptitU.getUrl());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "There was an error while redirecting you.");
        }
    }

    @PostMapping(value = "/create", produces = MediaType.TEXT_PLAIN_VALUE)
    public String createPtitU(@RequestHeader(name = "Host") final String host, @RequestBody PtitURequest request) {
        if (restTemplate.postForObject("http://" + host + "/checkid/account", request.getEmail(), HttpReply.class).getStatus() != HttpReply.Status.SUCCESS)
            throw new ResponseStatusException(UNAUTHORIZED, "You need an account to create short url.");

        long hash = request.getUrl().hashCode();
        String shortUrl = host + "/ptitu/" + hash;
        PtitU ptitU = new PtitU(hash, request.getUrl(), request.getEmail(), 0);

        try {
            ObjectifyService.run(() -> {
                ofy().save().entities(ptitU).now();
                return ptitU;
            });
            logger.info("PtitU created");
            return shortUrl;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "There was an error while creating the short url.");
        }
    }


    private String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
