package cn.ycl.ssm.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * spring mvc 联系入口程序
 */
@SpringBootApplication
@ComponentScan(value = {"cn.ycl.ssm.mvc","cn.ycl.ssm.compment"})
public class MvcApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MvcApplication.class);
        ConfigurableApplicationContext context = application.run(args);
    }
}
