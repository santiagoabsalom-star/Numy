package com.surrogate.numy;



import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@Push
@Theme(value = "muny")
@EnableScheduling
@SpringBootApplication
public class NumyApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(NumyApplication.class, args);
    }

}

