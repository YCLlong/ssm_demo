package cn.ssm.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "cn.ssm.login.controller" )
public class DownloadApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DownloadApplication.class);
        ConfigurableApplicationContext context = application.run(args);
    }
}
