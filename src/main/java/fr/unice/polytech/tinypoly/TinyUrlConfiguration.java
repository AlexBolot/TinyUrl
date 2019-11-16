package fr.unice.polytech.tinypoly;

//import fr.unice.polytech.tinypoly.mailservice.MailServiceImpl;
import fr.unice.polytech.tinypoly.mailservice.MailServiceImpl;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TinyUrlConfiguration {

//    @Bean
//    public ServletRegistrationBean exampleServletBean() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new LogController(), "/taskqueues/queue/*");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }

    @Bean
    public MailServiceImpl mailService() {
        return new MailServiceImpl();
    }
}
