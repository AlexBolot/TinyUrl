package fr.unice.polytech.tinypoly.controller;

import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.dto.PtitURequest;
import fr.unice.polytech.tinypoly.entities.PtitU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController
@RequestMapping(value = "/ptitu", produces = "application/json")
public class RedirectionController {

    private static final Logger logger = LoggerFactory.getLogger(RedirectionController.class);

    @GetMapping("/{hash}")
    public RedirectView redirect(@PathVariable long hash, RedirectAttributes attributes) {
        try {
            PtitU ptitU = ObjectifyService.run(() -> ofy().load().type(PtitU.class).id(hash).now());
            ptitU.addCompteur();
            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
            attributes.addAttribute("attribute", "redirectWithRedirectView");
            //TODO : update petit u
            // TODO : dans admin controlleur voir liste
            //  ObjectifyService.run(() -> ofy().save().
            return new RedirectView(ptitU.getUrl());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @PostMapping("/create")
    public String createPtitU(@RequestHeader(name = "Host") final String host, @RequestBody PtitURequest request) {
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
            return "FAILED";
        }
    }
}
