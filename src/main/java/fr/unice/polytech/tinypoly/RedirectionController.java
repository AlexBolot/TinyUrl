package fr.unice.polytech.tinypoly;

import fr.unice.polytech.tinypoly.dto.PtitURequest;
import fr.unice.polytech.tinypoly.entities.PtitU;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/ptitu", produces = "application/json")
public class RedirectionController {

    private List<PtitU> ptituList = new ArrayList<>();

    @GetMapping("/{hash}")
    public RedirectView redirect(@PathVariable long hash, RedirectAttributes attributes) {
        PtitU ptitU = null;
        for (PtitU p : ptituList) if (p.getUrl().hashCode() == hash) ptitU = p;
        if (ptitU != null) {
            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
            attributes.addAttribute("attribute", "redirectWithRedirectView");
            return new RedirectView(ptitU.getUrl());
        }
        return null;
    }

    @PostMapping("/create")
    public String createPtitU(@RequestBody PtitURequest request) {
        long hash = request.getUrl().hashCode();
        String shortUrl = "https://tinypoly-257609.appspot.com/ptitu/" + hash;
        PtitU ptitU = new PtitU(request.getUrl(), shortUrl);
        ptituList.add(ptitU);
        return shortUrl;
    }
}
