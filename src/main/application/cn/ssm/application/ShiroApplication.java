package cn.ssm.application;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.ssm.shiro")
public class ShiroApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ShiroApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        SecurityManager securityManager = context.getBean(SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);
        //
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("test","123");
        try {
            subject.login(token);
        }catch (Exception e){
            System.out.println("登录失败");
            return;
        }
        System.out.println("登录成功");
        try {
            subject.checkPermissions("select", "add");
        }catch (Exception e){
            System.out.println("无权限");
            return;
        }
        System.out.println("有权限");
    }
}
