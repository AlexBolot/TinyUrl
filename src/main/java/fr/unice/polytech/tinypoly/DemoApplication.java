package fr.unice.polytech.tinypoly;

import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.entities.PtitU;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		ObjectifyService.init();
		ObjectifyService.register(PtitU.class);
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/healthcheck")
	public String hello() {
		return "Server Status GREEN";
	}

}
