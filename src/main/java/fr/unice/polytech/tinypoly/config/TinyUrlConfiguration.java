package fr.unice.polytech.tinypoly.config;

import fr.unice.polytech.tinypoly.mailservice.MailService;
import fr.unice.polytech.tinypoly.mailservice.MailServiceImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TinyUrlConfiguration {

    @Bean
    public MailService mailService() {
        return new MailServiceImpl();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
