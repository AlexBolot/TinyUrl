package fr.unice.polytech.tinypoly.controller;

import fr.unice.polytech.tinypoly.dao.DatastoreDao;
import fr.unice.polytech.tinypoly.dto.PtitURequest;
import fr.unice.polytech.tinypoly.entities.PtitU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/ptitu", produces = "application/json")
public class RedirectionController {

    private static final Logger logger = LoggerFactory.getLogger(RedirectionController.class);

    private DatastoreDao dao = new DatastoreDao();

    private List<PtitU> ptituList = new ArrayList<>();

    @GetMapping("/{hash}")
    public RedirectView redirect(@PathVariable long hash, RedirectAttributes attributes) {
        try {
            PtitU ptitU = dao.readPtitU(hash);
            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
            attributes.addAttribute("attribute", "redirectWithRedirectView");
            return new RedirectView(ptitU.getUrl());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @PostMapping("/create")
    public String createPtitU(@RequestHeader(name="Host") final String host, @RequestBody PtitURequest request) {
        long hash = request.getUrl().hashCode();
        String shortUrl = host + "/ptitu/" + hash;
        PtitU ptitU = new PtitU(request.getUrl(), shortUrl, request.getEmail());

        try {
            logger.info("> Trying to create Account with id " + hash);
            PtitU p = dao.readPtitU(hash);
            if (!p.equals(ptitU)) {
                dao.createPtitU(ptitU);
                return shortUrl;
            }
            return "ERROR";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "FAILED";
        }
    }
}
