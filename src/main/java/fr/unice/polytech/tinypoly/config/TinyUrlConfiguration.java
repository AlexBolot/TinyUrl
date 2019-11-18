package fr.unice.polytech.tinypoly.config;

//import fr.unice.polytech.tinypoly.mailservice.MailServiceImpl;
import com.google.cloud.storage.Storage;
import fr.unice.polytech.tinypoly.log.Log;
import fr.unice.polytech.tinypoly.log.LogImpl;
import fr.unice.polytech.tinypoly.mailservice.MailService;
import fr.unice.polytech.tinypoly.mailservice.MailServiceImpl;
import fr.unice.polytech.tinypoly.servlet.LogServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

@Configuration
public class TinyUrlConfiguration {

//    @Bean
//    public ServletRegistrationBean exampleServletBean() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new LogController(), "/taskqueues/queue/*");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }

    @Bean
    public MailService mailService() {
        return new MailServiceImpl();
    }

    @Bean
    public Log log() {
        return new LogImpl();
    }

//    @Bean
//    public ServletRegistrationBean exampleServletBean() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(
//                new LogServlet(), "/taskqueues/queue/*");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
}
