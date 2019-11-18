package fr.unice.polytech.tinypoly.config;

import fr.unice.polytech.tinypoly.mailservice.MailService;
import fr.unice.polytech.tinypoly.mailservice.MailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TinyUrlConfiguration {

    @Bean
    public MailService mailService() {
        return new MailServiceImpl();
    }

}
